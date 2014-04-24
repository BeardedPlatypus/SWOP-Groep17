package scenario;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.DateTime;
import domain.Option;
import domain.OptionCategory;
import exceptions.OrderDoesNotExistException;

/**
 * Use case scenario test: Order Single Task
 * 
 * @author Simon Slangen
 *
 */
public class OrderSingleTaskScenario {
	OrderSingleTaskHandler orderSingleTaskHandler;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		orderSingleTaskHandler = init.getNewOrderSingleTaskHandler();
	}

	@Test
	public void normalFlowInitial_test() {
		
		//1. The user wants to order a single task.
		orderSingleTaskHandler.startNewOrderSession();
		assertTrue(orderSingleTaskHandler.isRunningOrderSession());
		
		//2. The system shows the list of available tasks.
		List<OptionCategory> possibleTasks = orderSingleTaskHandler.getPossibleTasks();
		assertTrue(!possibleTasks.isEmpty());
		
		//3. The user selects the task he wants to order.
		Option selectedOption = possibleTasks.get(0).getOption(0);
		assertNotNull(selectedOption);
		
		//4. The system asks the user for a deadline, as well as the required task options (e.g. color). ==HAPPENS IN UI==
		//5. The user enters the required details.
		orderSingleTaskHandler.selectOption(selectedOption);
		orderSingleTaskHandler.specifyDeadline(1, 2, 3);
		
		//6. The system stores the new order and updates the production schedule.
		OrderContainer submittedOrder = orderSingleTaskHandler.submitSingleTaskOrder();
		assertNotNull(submittedOrder);
		
		//7. The system presents an estimated completion date for the new order.
		DateTime eta = null;
		try {
			eta = orderSingleTaskHandler.getEstimatedCompletionTime(submittedOrder);
		} catch (OrderDoesNotExistException e) {
			fail();
		}
		assertNotNull(eta);
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		
		//1. The user wants to order a single task.
		orderSingleTaskHandler.startNewOrderSession();
		assertTrue(orderSingleTaskHandler.isRunningOrderSession());
		
		//2. The system shows the list of available tasks.
		List<OptionCategory> possibleTasks = orderSingleTaskHandler.getPossibleTasks();
		assertTrue(!possibleTasks.isEmpty());
		
		//3. The user selects the task he wants to order.
		Option selectedOption = possibleTasks.get(0).getOption(0);
		assertNotNull(selectedOption);
		
		//4. The system asks the user for a deadline, as well as the required task options (e.g. color). ==HAPPENS IN UI==
		//5. The user indicates he wants to cancel placing the order.
		//6. The use case returns to step 1. ==SEE MAIN SCENARIO TEST==
		orderSingleTaskHandler.startNewOrderSession();
		assertTrue(orderSingleTaskHandler.isRunningOrderSession());
	}

}
