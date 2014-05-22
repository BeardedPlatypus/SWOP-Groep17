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
import domain.assembly_line.AssemblyLineView;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.WorkPostView;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
import domain.handlers.PerformAssemblyTaskHandler;

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
	PerformAssemblyTaskHandler handler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
		handler = facade.getPerformAssemblyTaskHandler();
	}

	@Test
	public void scenarioTest_normal() {
		/*
		 * Step 1: The system asks which assembly line and which work post the user is residing at.     ==HAPPENS IN UI==
		 * Step 2: The user selects the corresponding assembly line work post.                ==HAPPENS IN UI==
		 * 
		 * Step 3: The system presents an overview of the pending assembly tasks
		 *         for the vehicle at the current work post.
		 */
		List<AssemblyTaskView> tasks = handler.getAssemblyTasksAtWorkPost(0, 0);
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
		facade.completeWorkpostTask(0, firstTask.getTaskNumber(), 60);
		tasks = handler.getAssemblyTasksAtWorkPost(0, 0);
		assertTrue(tasks.get(0).isCompleted());
		for(int i = 1; i < tasks.size(); i++){
			assertFalse(tasks.get(i).isCompleted());
		}
		
		/* 7. If all the assembly tasks at the assembly line are finished, 
		 *    the assembly line is shifted automatically and the production 
		 *    schedule is updated The system presents an updated overview of 
		 *    pending assembly tasks for the car at the current work post.
		 */
		assertFalse(handler.getWorkPosts(0).get(0).isFinished());
		
		
		
		// Exiting the overview is the responsibility of the user interface.
		// Use case ends here.
	}
	
	@Test
	public void scenarioTest_allTasksFinished() {
		this.completeAllTasks();
		List<WorkPostView> workPosts = handler.getWorkPosts(0);
		assertFalse(workPosts.get(0).isEmpty());
		assertFalse(workPosts.get(0).isFinished());
		assertFalse(workPosts.get(1).isEmpty());
		assertFalse(workPosts.get(1).isFinished());
		assertTrue(workPosts.get(2).isEmpty());
	}
	
	public void completeAllTasks() {
		List<AssemblyLineView> lines = handler.getAssemblyLines();
		for (int i = 0; i < lines.size(); i++) {
			AssemblyLineView line = lines.get(i);
			List<WorkPostView> workPosts = line.getWorkPostViews();
			for (int j = 0; j < workPosts.size(); j++) {
				List<AssemblyTaskView> tasks = handler.getAssemblyTasksAtWorkPost(i, j);
				for (int k = 0; k < tasks.size(); k++) {
					handler.completeWorkpostTask(i, j, k, 60);
				}
			}
		}
	}

}
