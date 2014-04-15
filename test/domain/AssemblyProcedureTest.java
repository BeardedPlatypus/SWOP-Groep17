package domain;

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
import org.powermock.modules.junit4.PowerMockRunner;

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
		
		procedure = Mockito.spy(new AssemblyProcedure(order, tasks));
	}
	
	@Test
	public void constructor_NullOrderTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(null, tasks);
	}
	
	@Test
	public void constructor_NullTasksTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(order, tasks);
	}
	
	@Test
	public void completeTask_negativeIntTask() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(-1, TaskType.BODY);
		
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
		List<AssemblyTaskContainer> containers = procedure.getAssemblyTasks(TaskType.BODY);
		assertEquals(containers.size(), 2);
		assertSame(containers.get(0), color);
		assertSame(containers.get(1), body);
		assertFalse(containers.contains(engine));
	}

}
