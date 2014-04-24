package scenario;

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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import domain.AssemblyLine;
import domain.AssemblyProcedure;
import domain.AssemblyTask;
import domain.AssemblyTaskContainer;
import domain.DateTime;
import domain.Manufacturer;
import domain.Model;
import domain.Option;
import domain.OptionCategory;
import domain.ProcedureStatistics;
import domain.Specification;
import domain.StatisticsLogger;
import domain.TaskType;
import domain.WorkPost;
import domain.handlers.PerformAssemblyTaskHandler;
import domain.order.Order;
import domain.order.StandardOrder;

@RunWith(PowerMockRunner.class)
public class PerformAssemblyTaskScenario {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	// TODO: class that has pending orders should go here
	Manufacturer manufacturer;
	
	Order order;
	Model model;
	Specification spec;
	
	AssemblyLine assemblyLine;
	
	WorkPost workPost1;
	WorkPost workPost2;
	WorkPost workPost3;
	
	AssemblyProcedure procedure;
	
	
	Option option1;
	Option option2;
	AssemblyTask task1;
	AssemblyTask task2;
	
	PerformAssemblyTaskHandler handler;
	StatisticsLogger logger;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
	
	manufacturer = new Manufacturer();
	
	logger = new StatisticsLogger();
	
	assemblyLine = Mockito.spy(new AssemblyLine(manufacturer));
	
	this.handler = new PerformAssemblyTaskHandler(manufacturer);
	
	option1 = new Option(TaskType.ACCESSORIES, "john", "doe");
	option2 = new Option(TaskType.ACCESSORIES, "alice", "johnson");
	this.task1 = new AssemblyTask(option1, 0);
	this.task2 = new AssemblyTask(option2, 1);
	model = new Model("Super model", new ArrayList<OptionCategory>(), 60);
	spec = new Specification(new ArrayList<Option>(Arrays
			.asList(option1, option2)));
	order = new StandardOrder(model, spec, 0, new DateTime(1, 0, 0));
	
	List<AssemblyTask> tasks = new ArrayList<AssemblyTask>();
	tasks.add(task1);
	tasks.add(task2);
	
	workPost1 = Whitebox.invokeMethod(assemblyLine, "getWorkPost", 0);
	workPost2 = Whitebox.invokeMethod(assemblyLine, "getWorkPost", 1);
	workPost3 = Whitebox.invokeMethod(assemblyLine, "getWorkPost", 2);
	
	procedure = Mockito.spy(new AssemblyProcedure(order, tasks, 120));
	Whitebox.setInternalState(workPost3, "activeAssembly", procedure);
	
	// TODO: prepare order in pending orders
	}

	@Test
	public void scenarioTest_normal() {
		/*
		 * Step 1: The system asks which work post the user is residing at.
		 * Step 2: The user selects the corresponding work post.
		 * 
		 * !! The above are the responsibility of the UI !!
		 * 
		 * Step 3: The system presents an overview of the pending assembly tasks
		 * for the car at the current work post.
		 */
		List<AssemblyTaskContainer> tasks = handler.getAssemblyTasksAtWorkPost(2);
		assertEquals(tasks.get(0), task1);
		assertEquals(tasks.get(1), task1);
		assertFalse(task1.isCompleted());
		assertFalse(task2.isCompleted());
		/*
		 * Step 4: The user selects one of the assembly tasks.
		 * Step 5: The system shows the assembly task information, including
		 * the sequence of actions to perform.
		 * !! The above are the responsibility of the UI !!
		 * 
		 * Step 6: The user performs the assembly tasks and indicates
		 * when the assembly task is finished together with the time it took him
		 * to finish the job.
		 */
		handler.completeWorkpostTask(2, 0, 60);
		assertTrue(task1.isCompleted());
		assertFalse(task2.isCompleted());
		assertFalse(workPost3.isFinished());
		/* Step 7: The system stores the changes and presents an updated overview
		 * of pending assembly tasks for the car at the current work post.
		 * 
		 * !! The above is the responsibility of the UI !!
		 */
		StatisticsLogger spiedLogger = PowerMockito.spy(logger);
		Manufacturer spiedManufacturer = PowerMockito.spy(manufacturer);
		handler.completeWorkpostTask(2, 1, 60);
		assertTrue(task1.isCompleted());
		assertTrue(task2.isCompleted());
		assertTrue(workPost3.isEmpty());
		assertFalse(workPost1.isEmpty());
		Mockito.verify(spiedLogger).addStatistics(Matchers.isA(ProcedureStatistics.class));
		Mockito.verify(spiedManufacturer).addToCompleteOrders(order);
		// Exiting the overview is the responsibility of the user interface.
		// Use case ends here.
	}
	
	@Test
	public void scenarioTest_noOrderPending() {
		/*
		 * Step 1: The system asks which work post the user is residing at.
		 * Step 2: The user selects the corresponding work post.
		 * 
		 * !! The above are the responsibility of the UI !!
		 * 
		 * Step 3: The system presents an overview of the pending assembly tasks
		 * for the car at the current work post.
		 */
		List<AssemblyTaskContainer> tasks = handler.getAssemblyTasksAtWorkPost(2);
		assertEquals(tasks.get(0), task1);
		assertEquals(tasks.get(1), task2);
		assertFalse(task1.isCompleted());
		assertFalse(task2.isCompleted());
		/*
		 * Step 4: The user selects one of the assembly tasks.
		 * Step 5: The system shows the assembly task information, including
		 * the sequence of actions to perform.
		 * !! The above are the responsibility of the UI !!
		 * 
		 * Step 6: The user performs the assembly tasks and indicates
		 * when the assembly task is finished together with the time it took him
		 * to finish the job.
		 */
		handler.completeWorkpostTask(2, 0, 60);
		assertTrue(task1.isCompleted());
		assertFalse(task2.isCompleted());
		assertFalse(workPost3.isFinished());
		/* Step 7: The system stores the changes and presents an updated overview
		 * of pending assembly tasks for the car at the current work post.
		 * 
		 * !! The above is the responsibility of the UI !!
		 */
		handler.completeWorkpostTask(2, 1, 60);
		assertTrue(task1.isCompleted());
		assertTrue(task2.isCompleted());
		assertTrue(workPost3.isEmpty());
		assertTrue(workPost1.isEmpty());
		// Exiting the overview is the responsibility of the user interface.
		// Use case ends here.
	}

}
