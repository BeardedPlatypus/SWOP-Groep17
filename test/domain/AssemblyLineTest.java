package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
public class AssemblyLineTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock ProductionSchedule schedule;
	
	@Mock AssemblyProcedure procedure1;
	@Mock AssemblyProcedure procedure2;
	@Mock AssemblyProcedure procedure3;
	
	AssemblyTask task1;
	AssemblyTask task2;
	AssemblyTask task3;
	
	@Mock Order order;
	
	List<WorkPostContainer> workPosts;
	
	AssemblyLine assemblyLine;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyProcedure> procedures = new ArrayList<AssemblyProcedure>();
		procedures.add(procedure1);
		procedures.add(procedure2);
		procedures.add(procedure3);
		
		task1 = Mockito.spy(new AssemblyTask("john", "doe", TaskType.BODY, 0));
		task2 = Mockito.spy(new AssemblyTask("jane", "doe", TaskType.DRIVETRAIN, 0));
		task3 = Mockito.spy(new AssemblyTask("bob", "carol", TaskType.ACCESSORIES, 0));
		
		assemblyLine = new AssemblyLine(schedule);
		workPosts = assemblyLine.getWorkPostContainers();
		
		for (int i = 0; i < assemblyLine.getAmountOfWorkPosts(); i++)
		{
			assemblyLine.getWorkPost(i).setAssemblyProcedure(procedures.get(i));
		}
		
		Mockito.when(procedure1.getAssemblyTasks(workPosts.get(0).getWorkPostType())).thenReturn(new ArrayList<AssemblyTaskContainer>(Arrays.asList(task1)));
		Mockito.when(procedure2.getAssemblyTasks(workPosts.get(1).getWorkPostType())).thenReturn(new ArrayList<AssemblyTaskContainer>(Arrays.asList(task2)));
		Mockito.when(procedure3.getAssemblyTasks(workPosts.get(2).getWorkPostType())).thenReturn(new ArrayList<AssemblyTaskContainer>(Arrays.asList(task3)));
	}

	@Test
	public void constructor_NullProductionSchedule() {
		expected.expect(IllegalArgumentException.class);
		new AssemblyLine(null);
	}
	
	@Test
	public void constructor_CheckWorkpostsInitialised() {
		AssemblyLine assemblyLine = new AssemblyLine(schedule);
		
		List<WorkPostContainer> workPosts = assemblyLine.getWorkPostContainers();
		
		int counter = 0;
		for (TaskType type : TaskType.values()) {
			assertEquals(workPosts.get(counter).getWorkPostType(), type);
			counter++;
		}
	}
	
	@Test
	public void getAssemblyOnEachWorkStation_test() {
		List<AssemblyProcedureContainer> assemblyLineProcedures = assemblyLine.getAssemblyOnEachWorkStation();
		assertTrue(assemblyLineProcedures.get(0) == procedure1);
		assertTrue(assemblyLineProcedures.get(1) == procedure2);
		assertTrue(assemblyLineProcedures.get(2) == procedure3);
	}
	
	@Test
	public void getAssemblyTasksAtPost_negativeWorkPostNumber() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.getAssemblyTasksAtPost(-1);
	}
	
	@Test
	public void getAssemblyTasksAtPost_tooBigWorkPostNumber() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.getAssemblyTasksAtPost(Integer.MAX_VALUE);
	}
	
	@Test
	public void getAssemblyTasksAtPost_valid() {
		List<AssemblyTaskContainer> containers = assemblyLine.getAssemblyTasksAtPost(0);
		assertTrue(containers.get(0) == task1);
	}
	
	@Test
	public void completeWorkPostTask_negativeWorkPostNum() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(-1, 0);
	}
	
	@Test
	public void completeWorkPostTask_tooBigWorkPostNum() {
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(Integer.MAX_VALUE, 0);
	}
	
	@Test
	public void completeWorkPostTask_negativeTaskNum() {
		Mockito.doThrow(new IllegalArgumentException()).when(procedure1).completeTask(-1, TaskType.BODY);
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(0, -1);
	}
	
	@Test
	public void completeWorkPostTask_tooBigTaskNum() {
		Mockito.doThrow(new IllegalArgumentException()).when(procedure1).completeTask(Integer.MAX_VALUE, TaskType.BODY);
		expected.expect(IllegalArgumentException.class);
		assemblyLine.completeWorkpostTask(0, Integer.MAX_VALUE);
	}

}
