package domain.assembly_line;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import domain.assembly_line.AssemblyProcedure;
import domain.assembly_line.AssemblyTask;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.TaskType;
import domain.car.Option;
import domain.order.Order;
import domain.statistics.ProcedureStatistics;

@RunWith(PowerMockRunner.class)
public class AssemblyProcedureTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock Order order;
	
	@Mock Option option1;
	@Mock Option option2;
	@Mock Option option3;
	
	AssemblyProcedure procedure;
	
	AssemblyTask color;
	AssemblyTask body;
	AssemblyTask engine;
	
	List<AssemblyTask> tasks;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(option1.getType()).thenReturn(TaskType.BODY);
		Mockito.when(option2.getType()).thenReturn(TaskType.BODY);
		Mockito.when(option3.getType()).thenReturn(TaskType.DRIVETRAIN);
		
		color = new AssemblyTask(option1, 0);
		body = new AssemblyTask(option2, 1);
		engine = new AssemblyTask(option3, 2);
		
		tasks = new ArrayList<AssemblyTask>();
		tasks.add(color);
		tasks.add(body);
		tasks.add(engine);
		
		procedure = Mockito.spy(new AssemblyProcedure(order, tasks, 180));
	}
	
	@Test
	public void constructor_NullOrderTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(null, tasks, 180);
	}
	
	@Test
	public void constructor_NullTasksTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(order, null, 180);
	}
	
	@Test
	public void constructor_NegativeExpectedMinutesTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(order, tasks, -1);
	}
	
	@Test
	public void constructor_valid() {
		AssemblyProcedure procedure = PowerMockito.spy(new AssemblyProcedure(order, tasks, 180));
		assertEquals(order, procedure.getOrder());
		assertEquals(tasks, procedure.getAssemblyTasks());
		assertEquals(180, Whitebox.getInternalState(procedure, "expectedMinutes"));
	}
	
	@Test
	public void completeTask_negativeIntTask() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(-1, TaskType.BODY);
		
	}
	
	@Test
	public void addToElapsedMinutes_negativeMins() {
		exception.expect(IllegalArgumentException.class);
		procedure.addToElapsedMinutes(-1);
	}
	
	@Test
	public void addToElapsedMinutes_valid() {
		AssemblyProcedure procedure = PowerMockito.spy(new AssemblyProcedure(order, tasks, 180));
		procedure.addToElapsedMinutes(60);
		assertEquals(60, (int) Whitebox.getInternalState(procedure, "elapsedMinutes"));
	}
	
	@Test
	public void isFinished_noTaskCompleted() {
		assertFalse(procedure.isFinished());
	}
	
	@Test
	public void isFinished_partlyCompleted() {
		tasks.get(0).setCompleted(true);
		assertFalse(procedure.isFinished());
	}
	
	@Test
	public void isFinished_whollyCompleted() {
		for (AssemblyTask task : tasks) {
			task.setCompleted(true);
		}
		assertTrue(procedure.isFinished());
	}
	
	@Test
	public void isFinishedType_noTaskCompleted() {
		assertFalse(procedure.isFinished(TaskType.BODY));
	}
	
	@Test
	public void isFinishedType_partlyCompleted() {
		tasks.get(0).setCompleted(true);
		assertFalse(procedure.isFinished(TaskType.BODY));
	}
	
	@Test
	public void isFinishedType_whollyCompleted() {
		tasks.get(0).setCompleted(true);
		tasks.get(1).setCompleted(true);
		assertTrue(procedure.isFinished(TaskType.BODY));
	}
	
	@Test
	public void taskIsFinished_false() {
		assertFalse(procedure.taskIsFinished(0));
	}
	
	@Test
	public void taskIsFinished_true() {
		procedure.completeTask(0, TaskType.BODY);
		assertTrue(procedure.taskIsFinished(0));
	}
	
	@Test
	public void taskIsFinished_negativeTaskNum() {
		exception.expect(IllegalArgumentException.class);
		procedure.taskIsFinished(-1);
	}
	
	@Test
	public void taskIsFinished_tooBigIntTask() {
		exception.expect(IllegalArgumentException.class);
		procedure.taskIsFinished(Integer.MAX_VALUE);
	}
	
	@Test
	public void completeTask_TooBigIntTask() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(3, TaskType.BODY);
	}
	
	@Test
	public void completeTask_WrongTaskType() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(2, TaskType.BODY);
	}
	
	@Test
	public void completeTask_valid() {
		assertFalse(body.isCompleted());
		procedure.completeTask(1, TaskType.BODY);
		assertTrue(body.isCompleted());
	}
	
	@Test
	public void getTasksOfType_test() {
		List<AssemblyTaskView> containers = procedure.getAssemblyTasks(TaskType.BODY);
		assertEquals(containers.size(), 2);
		assertSame(containers.get(0), color);
		assertSame(containers.get(1), body);
		assertFalse(containers.contains(engine));
	}
	
	@Test
	public void getTask_NegativeNumber() {
		exception.expect(IllegalArgumentException.class);
		procedure.getTask(-1);
	}
	
	@Test
	public void getTask_TooBigNumber() {
		exception.expect(IllegalArgumentException.class);
		procedure.getTask(100);
	}
	
	@Test
	public void getTask_valid() {
		AssemblyTask task = procedure.getTask(0);
		assertTrue(task == color);
	}
	
	@Test
	public void makeStatistics_notYetFinished() {
		exception.expect(IllegalStateException.class);
		procedure.makeStatisticsEvent();
	}
	
	@Test
	public void makeStatistics_finished() {
		AssemblyProcedure procedure = PowerMockito.spy(new AssemblyProcedure(order, tasks, 180));
		procedure.addToElapsedMinutes(200);
		@SuppressWarnings("unchecked")
		List<AssemblyTask> tasks = (ArrayList<AssemblyTask>) Whitebox.getInternalState(procedure, "tasks");
		for (AssemblyTask task : tasks) {
			task.setCompleted(true);
		}
		ProcedureStatistics stats = procedure.makeStatisticsEvent();
		assertEquals(20, stats.getDelay());
	}

}
