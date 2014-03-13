package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AdvanceAssemblyLineScenario {

	@Rule public ExpectedException exception = ExpectedException.none();
	
	AdvanceAssemblyLineHandler advanceHandler;
	NewOrderSessionHandler orderHandler;
	PerformAssemblyTaskHandler taskHandler;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		advanceHandler = init.getAdvanceHandler();
		orderHandler = init.getNewOrderHandler();
		taskHandler = init.getTaskHandler();
		
		int[] choices = { 0, 0, 0, 0, 0, 0, 0 };
		
		Model model = orderHandler.getNewOrderModels().get(0);
		Specification specs = new Specification(choices);
		orderHandler.chooseModelAndSpecifications(model, specs);
		
	}
	
	@Test
	public void normalFlowInitial_test() {
		//1. The user indicates he wants to advance the assembly line.
		
		//2. The system presents an overview of the current assembly line status,
		//as well as a view of the future assembly line status (as it would be after completing this use case),
		//including pending and finished tasks at each work post.
		advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		advanceHandler.getFutureWorkpostsAndActiveAssemblies();
		//3. The user confirms the decision to move the assembly line forward, 
		//and enters the time that was spent during the current phase (e.g. 45 minutes instead of the scheduled hour).
		advanceHandler.tryAdvance(45);
		assertEquals(orderHandler.currentTime(), new DateTime(0, 6, 45));
		//4. The system moves the assembly line forward one work post according to the scheduling rules.
		//5. The system presents an overview of the new assembly line status.
		advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		taskHandler.completeWorkpostTask(0, 0);
		taskHandler.completeWorkpostTask(0, 1);
		
		advanceHandler.tryAdvance(45);
		
		taskHandler.completeWorkpostTask(1, 2);
		taskHandler.completeWorkpostTask(1, 3);
		
		advanceHandler.tryAdvance(45);
		
		taskHandler.completeWorkpostTask(2, 4);
		taskHandler.completeWorkpostTask(2, 5);
		taskHandler.completeWorkpostTask(2, 6);
		
		advanceHandler.tryAdvance(45);
		
		// an order has been completed
		assertTrue(orderHandler.getCompletedOrders().get(0).isCompleted());
		
		//6. The user indicates he is done viewing the status.
		
	}
	
	@Test
	public void notAllTasksCompleted_test() {
		exception.expect(IllegalStateException.class);
		
		advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		advanceHandler.getFutureWorkpostsAndActiveAssemblies();
		advanceHandler.tryAdvance(45);
		assertEquals(orderHandler.currentTime(), new DateTime(0, 6, 45));
		advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		taskHandler.completeWorkpostTask(0, 0);

		// not all tasks have been completed

		advanceHandler.tryAdvance(45);

		//6. The user indicates he is done viewing the status.
	}

}
