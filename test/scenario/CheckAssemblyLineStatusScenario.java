package scenario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import domain.AssemblyLine;
import domain.AssemblyProcedure;
import domain.AssemblyTask;
import domain.AssemblyTaskContainer;
import domain.Manufacturer;
import domain.Option;
import domain.TaskType;
import domain.WorkPost;
import domain.WorkPostContainer;
import domain.handlers.AssemblyLineStatusHandler;
import domain.order.Order;

@RunWith(PowerMockRunner.class)
public class CheckAssemblyLineStatusScenario {
	
	AssemblyLine assemblyLine;
	
	List<WorkPost> workPosts;
	
	AssemblyProcedure procedure1;
	AssemblyProcedure procedure2;
	
	AssemblyTask task1;
	AssemblyTask task2;
	AssemblyTask task3;
	AssemblyTask task4;
	
	@Mock Manufacturer manufacturer;
	@Mock Order order1;
	@Mock Order order2;
	
	AssemblyLineStatusHandler handler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		assemblyLine = new AssemblyLine(manufacturer);
		workPosts = Whitebox.invokeMethod(assemblyLine, "getWorkPosts");
		
		task1 = new AssemblyTask(new Option(TaskType.BODY, "john", "doe"), 0);
		task1.setCompleted(true);
		task2 = new AssemblyTask(new Option(TaskType.BODY, "jane", "doe"), 1);
		
		task3 = new AssemblyTask(new Option(TaskType.ACCESSORIES, "wut", "wot"), 3);
		task3.setCompleted(true);
		task4 = new AssemblyTask(new Option(TaskType.ACCESSORIES, "yes", "no"), 4);
		
		procedure1 = new AssemblyProcedure(order1, new ArrayList<AssemblyTask>(Arrays.asList(task1, task2)), 120);
		procedure2 = new AssemblyProcedure(order2, new ArrayList<AssemblyTask>(Arrays.asList(task3, task4)), 120);
		
		Whitebox.setInternalState(workPosts.get(0), "activeAssembly", procedure1);
		Whitebox.setInternalState(workPosts.get(2), "activeAssembly", procedure2);
		
		PowerMockito.doReturn(assemblyLine).when(manufacturer, "getAssemblyLine");
		
		
	}

	@Test
	public void normal_flow() {
		// Step 1: The user wants to check the current status of the assembly line
		// --- Responsibility of the UI ---
		// Step 2: The system presents an overview of the current assembly line status,
		// including pending and nished tasks at each work post.
		// --- Only getting the information is tested, formatting is the responsibility 
		// of the UI ---
		assertEquals(3, handler.getAmountOfWorkPosts());
		List<AssemblyTaskContainer> tasks;
		List<WorkPostContainer> containers = handler.getWorkPosts();
		assertEquals(handler.getWorkPost(0), containers.get(0));
		assertFalse(workPosts.get(0).isFinished());
		assertTrue(workPosts.get(1).isEmpty());
		assertFalse(workPosts.get(2).isFinished());
		assertEquals(2, handler.getAmountOfTasksAtWorkPost(0));
		tasks = handler.getTasksAtWorkPost(0);
		assertEquals(tasks.get(0), task1);
		assertEquals(tasks.get(1), task2);
		assertTrue(tasks.get(0).isCompleted());
		assertFalse(tasks.get(1).isCompleted());
		
		tasks = handler.getTasksAtWorkPost(2);
		assertEquals(tasks.get(0), task3);
		assertEquals(tasks.get(1), task4);
		assertTrue(tasks.get(0).isCompleted());
		assertFalse(tasks.get(1).isCompleted());
	}

}
