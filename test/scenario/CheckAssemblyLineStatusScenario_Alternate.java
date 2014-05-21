package scenario;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.InteractionSimulator;
import domain.assemblyLine.AssemblyTaskView;
import domain.assemblyLine.WorkPostView;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

/**
 * Use case scenario test: Check Assembly Line Status
 * 
 * @author Simon Slangen
 *
 */
public class CheckAssemblyLineStatusScenario_Alternate {
	
	DomainFacade facade;
	InteractionSimulator sim;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
		
		sim = new InteractionSimulator(facade);
		
	}
	
	@Test
	public void emptyAssemblyLine_test() {
		for(WorkPostView wp : facade.getWorkPosts()){
			assertTrue(wp.isEmpty());
		}
	}

	@Test
	public void normalFlowInitial_test() {
		//We start from a situation with a few pending and a few partially completed 
		//Place 5 orders.
		sim.simulatePlaceOrder(5);
		//Complete 2 assembly line iterations.
		sim.simulateCompleteAllTasksOnAssemblyLine(2);
		
		//1. The user wants to check the current status of the assembly line. ==HAPPENS IN UI==
		//2. The system presents an overview of the current assembly line status,
		//   including pending and finished tasks at each work post.
		
		//Not all work posts are empty.
		int nonEmptyWorkPosts = 0;
		int nonFinishedWorkPosts = 0;
		for(WorkPostView wp : facade.getWorkPosts()){
			if(!wp.isEmpty()) nonEmptyWorkPosts++;
			if(!wp.isFinished()) nonFinishedWorkPosts++;
		}
		assertTrue(nonEmptyWorkPosts > 0);
		assertTrue(nonFinishedWorkPosts > 0);
		assertEquals(nonEmptyWorkPosts, nonFinishedWorkPosts);
		
		//No tasks are completed.
		for(WorkPostView wp : facade.getWorkPosts()){
			for(AssemblyTaskView task : wp.getMatchingAssemblyTasks()){
				assertFalse(task.isCompleted());
			}
		}
	}
	
	@Test
	public void partiallyFinishedWorkStations_test(){
		//We start from a situation with a few pending and a few partially completed 
		//Place 5 orders.
		sim.simulatePlaceOrder(5);
		//Complete 2 assembly line iterations.
		sim.simulateCompleteAllTasksOnAssemblyLine(2);
		
		//Find a work post with more than one unfinished task.
		int workPostNumber = 0;
		int unfinishedTasks = 0;
	out:for(WorkPostView wp : facade.getWorkPosts()){
			List<AssemblyTaskView> tasks =  wp.getMatchingAssemblyTasks();
			if(tasks.size() > 1){
				workPostNumber = wp.getWorkPostNum();
				unfinishedTasks = tasks.size();
				break out;
			}
		}
		//If there was such a case, complete all but one of those tasks.
		if(unfinishedTasks > 0){
			sim.simulateCompleteTasksOnWorkPost(unfinishedTasks-1, workPostNumber);
		}
		
		//SCENARIO:
		//1. The user wants to check the current status of the assembly line. ==HAPPENS IN UI==
		//2. The system presents an overview of the current assembly line status,
		//   including pending and finished tasks at each work post.
		
		//Not all work posts are empty.
		int nonEmptyWorkPosts = 0;
		int nonFinishedWorkPosts = 0;
		for(WorkPostView wp : facade.getWorkPosts()){
			if(!wp.isEmpty()) nonEmptyWorkPosts++;
			if(!wp.isFinished()) nonFinishedWorkPosts++;
		}
		assertTrue(nonEmptyWorkPosts > 0);
		assertTrue(nonFinishedWorkPosts > 0);
		assertEquals(nonEmptyWorkPosts, nonFinishedWorkPosts);
		
		//Find the work posts with completed tasks
		HashSet<WorkPostView> partiallyCompletedWorkPosts = new HashSet<WorkPostView>();
		for(WorkPostView wp : facade.getWorkPosts()){
			for(AssemblyTaskView task : wp.getMatchingAssemblyTasks()){
				if(task.isCompleted()) partiallyCompletedWorkPosts.add(wp);
			}
		}
		//Assert that such a work post exists.
		assertFalse(partiallyCompletedWorkPosts.isEmpty());
		//Assert that there is but one such a work post.
		assertEquals(partiallyCompletedWorkPosts.size(), 1);
		
		//Assert that the right number of tasks are marked completed.
		WorkPostView wp = partiallyCompletedWorkPosts.iterator().next();
		int completedTasks = 0;
		for(AssemblyTaskView task : wp.getMatchingAssemblyTasks()){
			if(task.isCompleted()) completedTasks++;
		}
		assertEquals(completedTasks,unfinishedTasks-1);
		
	}

}
