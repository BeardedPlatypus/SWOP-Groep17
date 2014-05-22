package scenario;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import domain.InteractionSimulator;
import domain.assembly_line.AssemblyTaskView;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

/**
 * 
 * Use case scenario test: Perform Assembly Task
 * 
 * @author Simon Slangen
 *
 */
@RunWith(PowerMockRunner.class)
public class PerformAssemblyTaskScenario {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	DomainFacade facade;
	InteractionSimulator sim;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
		sim = new InteractionSimulator(facade);
		
		sim.simulatePlaceOrder(10);
		sim.simulateCompleteAllTasksOnAssemblyLine(5);
	}

	@Test
	public void scenarioTest_normal() {
		/*
		 * Step 1: The system asks which work post the user is residing at.     ==HAPPENS IN UI==
		 * Step 2: The user selects the corresponding work post.                ==HAPPENS IN UI==
		 * 
		 * Step 3: The system presents an overview of the pending assembly tasks
		 *         for the car at the current work post.
		 */
		List<AssemblyTaskView> tasks = facade.getAssemblyTasksAtWorkPost(2);
		assertTrue(tasks.size() > 0);
		for(AssemblyTaskView task : tasks){
			assertFalse(task.isCompleted());
		}
		/*
		 * Step 4: The user selects one of the assembly tasks.					==HAPPENS IN UI==
		 * Step 5: The system shows the assembly task information, including    ==HAPPENS IN UI==
		 *         the sequence of actions to perform.
		 * Step 6: The user performs the assembly tasks and indicates
		 *         when the assembly task is finished together with the time it took him
		 *         to finish the job.
		 */
		AssemblyTaskView firstTask = tasks.get(0);
		facade.completeWorkpostTask(2, firstTask.getTaskNumber(), 60);
		tasks = facade.getAssemblyTasksAtWorkPost(2);
		assertTrue(tasks.get(0).isCompleted());
		for(int i = 1; i < tasks.size(); i++){
			assertFalse(tasks.get(i).isCompleted());
		}
		
		/* 7. If all the assembly tasks at the assembly line are finished, 
		 *    the assembly line is shifted automatically and the production 
		 *    schedule is updated The system presents an updated overview of 
		 *    pending assembly tasks for the car at the current work post.
		 */
		assertFalse(facade.getWorkPost(2).isFinished());
		
		// Exiting the overview is the responsibility of the user interface.
		// Use case ends here.
	}
	
	@Test
	public void scenarioTest_allTasksFinished() {
		//Complete all tasks at the first two workposts...
		for(int i = 0; i < 2; i++){
			for(AssemblyTaskView task : facade.getAssemblyTasksAtWorkPost(i)){
				assertFalse(task.isCompleted());
				facade.completeWorkpostTask(i, task.getTaskNumber(), 60);
				assertTrue(task.isCompleted());
			}
			assertTrue(facade.getWorkPost(i).isFinished());
		}
		//Complete all tasks at the second work post except the second one.
		List<AssemblyTaskView> tasks = facade.getAssemblyTasksAtWorkPost(2);
		for(int i = 0; i < tasks.size(); i++){
			AssemblyTaskView assTask = tasks.get(i);
			if(i != 1 && (!assTask.isCompleted())){
				facade.completeWorkpostTask(2, assTask.getTaskNumber(), 60);
			}
		}
		
		
		/*
		 * Step 1: The system asks which work post the user is residing at.     ==HAPPENS IN UI==
		 * Step 2: The user selects the corresponding work post.                ==HAPPENS IN UI==
		 * 
		 * Step 3: The system presents an overview of the pending assembly tasks
		 *         for the car at the current work post.
		 */
		tasks = facade.getAssemblyTasksAtWorkPost(2);
		assertTrue(tasks.size() > 0);
		for(int i = 0; i < tasks.size(); i++){
			if(i != 2){
				assertTrue(tasks.get(i).isCompleted());
			}else{
				assertFalse(tasks.get(i).isCompleted());
			}
		}
		/*
		 * Step 4: The user selects one of the assembly tasks.					==HAPPENS IN UI==
		 * Step 5: The system shows the assembly task information, including    ==HAPPENS IN UI==
		 *         the sequence of actions to perform.
		 * Step 6: The user performs the assembly tasks and indicates
		 *         when the assembly task is finished together with the time it took him
		 *         to finish the job.
		 */
		AssemblyTaskView secondTask = tasks.get(1);
		facade.completeWorkpostTask(2, secondTask.getTaskNumber(), 60);
		assertTrue(secondTask.isCompleted());
		
		/* 7. If all the assembly tasks at the assembly line are finished, 
		 *    the assembly line is shifted automatically and the production 
		 *    schedule is updated The system presents an updated overview of 
		 *    pending assembly tasks for the car at the current work post.
		 */
		assertFalse(facade.getWorkPost(2).isFinished());
		for(AssemblyTaskView task: facade.getAssemblyTasksAtWorkPost(2)){
			assertFalse(task.isCompleted());
		}
		
		
		// Exiting the overview is the responsibility of the user interface.
		// Use case ends here.
	}

}
