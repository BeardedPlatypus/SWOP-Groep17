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
public class PerformAssemblyTaskHandlerTest {

	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock ProductionSchedule schedule;
	@Mock Order order;
	
	AssemblyLine assemblyLine;
	
	WorkPost workPost1;
	WorkPost workPost2;
	
	AssemblyProcedure procedure1;
	AssemblyProcedure procedure2;
	
	AssemblyTask task1;
	AssemblyTask task2;
	
	PerformAssemblyTaskHandler handler;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		assemblyLine = Mockito.spy(new AssemblyLine(schedule));
		
		this.handler = new PerformAssemblyTaskHandler(this.assemblyLine);
		this.task1 = new AssemblyTask("john", "doe", TaskType.BODY, 0);
		this.task2 = new AssemblyTask("jane", "doe", TaskType.DRIVETRAIN, 0);
		
		List<AssemblyTask> tasks1 = new ArrayList<AssemblyTask>();
		List<AssemblyTask> tasks2 = new ArrayList<AssemblyTask>();
		tasks1.add(task1);
		tasks2.add(task2);
		
		workPost1 = Mockito.spy(new WorkPost(TaskType.BODY, 0));
		workPost2 = Mockito.spy(new WorkPost(TaskType.DRIVETRAIN, 1));
		
		procedure1 = Mockito.spy(new AssemblyProcedure(order, tasks1));
		procedure2 = Mockito.spy(new AssemblyProcedure(order, tasks2));
		workPost1.setAssemblyProcedure(procedure1);
		workPost2.setAssemblyProcedure(procedure2);
		
		Mockito.when(procedure1.getAssemblyTasks(TaskType.BODY)).thenReturn(new ArrayList<AssemblyTaskContainer>(tasks1));
		Mockito.when(procedure2.getAssemblyTasks(TaskType.DRIVETRAIN)).thenReturn(new ArrayList<AssemblyTaskContainer>(tasks2));
		
		Mockito.when(assemblyLine.getAssemblyTasksAtPost(0)).thenReturn(new ArrayList<AssemblyTaskContainer>(tasks1));
		Mockito.when(assemblyLine.getAssemblyTasksAtPost(1)).thenReturn(new ArrayList<AssemblyTaskContainer>(tasks2));
		
		Mockito.when(assemblyLine.getWorkPost(0)).thenReturn(workPost1);
		Mockito.when(assemblyLine.getWorkPost(1)).thenReturn(workPost2);
	}
	
	@Test
	public void constructor_NullAssemblyLineTest() {
		this.exception.expect(IllegalArgumentException.class);
		new PerformAssemblyTaskHandler(null);
	}
	
	@Test
	public void getWorkPostsTest() {
		List<WorkPostContainer> array = new ArrayList<WorkPostContainer>();
		array.add(this.workPost1);
		Mockito.when(this.assemblyLine.getWorkPostContainers()).thenReturn(array);
		
		List<WorkPostContainer> returnVal = handler.getWorkPosts();
		assertTrue(returnVal.contains(workPost1));
		assertFalse(returnVal.contains(workPost2));
	}
	
	@Test
	public void getTasksAtWorkPost_negativeInt() {
		exception.expect(IllegalArgumentException.class);
		handler.getAssemblyTasksAtPost(-1);
	}
	
	@Test
	public void getTasksAtWorkPost_outOfBounds() {
		exception.expect(IllegalArgumentException.class);
		handler.getAssemblyTasksAtPost(100);
	}
	
	@Test
	public void getTasksAtWorkPost_valid() {
		List<AssemblyTaskContainer> tasks = handler.getAssemblyTasksAtPost(0);
		assertTrue(tasks.contains(task1));
		assertFalse(tasks.contains(task2));
	}
	
	@Test
	public void completeTask_negativeWorkPostNumber() {
		exception.expect(IllegalArgumentException.class);
		handler.completeWorkpostTask(-1, 0);
	}
	
	@Test
	public void completeTask_TooLargeWorkPostNumber() {
		exception.expect(IllegalArgumentException.class);
		handler.completeWorkpostTask(100, 0);
	}
	
	@Test
	public void completeTask_negativeTaskNumber() {
		exception.expect(IllegalArgumentException.class);
		handler.completeWorkpostTask(0, -1);
	}
	
	@Test
	public void completeTask_TooLargeTaskNumber() {
		exception.expect(IllegalArgumentException.class);
		handler.completeWorkpostTask(0, 100);
	}
	
	@Test
	public void completeTask_validArgs() {
		handler.completeWorkpostTask(0, 0);
		assertTrue(task1.isCompleted());
		assertFalse(task2.isCompleted());
		handler.completeWorkpostTask(1, 0);
		assertTrue(task2.isCompleted());
	}

}
