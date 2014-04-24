package scenario;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.DateTime;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.handlers.InitialisationHandler;
import domain.handlers.DomainFacade;
import domain.order.OrderContainer;
import exceptions.OrderDoesNotExistException;

/**
 * Use case scenario test: Order Single Task
 * 
 * @author Simon Slangen
 *
 */
public class OrderSingleTaskScenario {
	DomainFacade facade;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
	}

	@Test
	public void normalFlowInitial_test() {
		
		//1. The user wants to order a single task.
		facade.startNewSingleTaskOrderSession();
		assertTrue(facade.isRunningOrderSession());
		
		//2. The system shows the list of available tasks.
		List<OptionCategory> possibleTasks = facade.getPossibleTasks();
		assertTrue(!possibleTasks.isEmpty());
		
		//3. The user selects the task he wants to order.
		Option selectedOption = possibleTasks.get(0).getOption(0);
		assertNotNull(selectedOption);
		
		//4. The system asks the user for a deadline, as well as the required task options (e.g. color). ==HAPPENS IN UI==
		//5. The user enters the required details.
		facade.selectSingleTaskOption(selectedOption);
		facade.specifyDeadline(1, 2, 3);
		
		//6. The system stores the new order and updates the production schedule.
		OrderContainer submittedOrder = facade.submitSingleTaskOrder();
		assertNotNull(submittedOrder);
		
		//7. The system presents an estimated completion date for the new order.
		DateTime eta = null;
		try {
			eta = facade.getEstimatedCompletionTime(submittedOrder);
		} catch (OrderDoesNotExistException e) {
			fail();
		}
		assertNotNull(eta);
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		
		//1. The user wants to order a single task.
		facade.startNewSingleTaskOrderSession();
		assertTrue(facade.isRunningOrderSession());
		
		//2. The system shows the list of available tasks.
		List<OptionCategory> possibleTasks = facade.getPossibleTasks();
		assertTrue(!possibleTasks.isEmpty());
		
		//3. The user selects the task he wants to order.
		Option selectedOption = possibleTasks.get(0).getOption(0);
		assertNotNull(selectedOption);
		
		//4. The system asks the user for a deadline, as well as the required task options (e.g. color). ==HAPPENS IN UI==
		//5. The user indicates he wants to cancel placing the order.
		//6. The use case returns to step 1. ==SEE MAIN SCENARIO TEST==
		facade.startNewSingleTaskOrderSession();
		assertTrue(facade.isRunningOrderSession());
	}

}
