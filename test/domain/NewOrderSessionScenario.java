package domain;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NewOrderSessionScenario {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	NewOrderSessionHandler orderHandler;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		orderHandler = init.getNewOrderHandler();
	}

	@Test
	public void normalFlowInitial_test() {
		exception.expect(NullPointerException.class); //TODO VERY BAD
		
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		List<OrderContainer> initPending = orderHandler.getIncompleteOrders();
		List<OrderContainer> initComplete = orderHandler.getCompletedOrders();
		assertTrue(initPending.isEmpty());
		assertTrue(initComplete.isEmpty());
		//2. The user indicates he wants to place a new car order. ==HAPPENS IN UI==
		//3. The system shows a list of available car models.
		List<Model> models = orderHandler.getNewOrderModels();	
		assertTrue(!models.isEmpty());
		//4. The user indicates the car model he wishes to order. ==HAPPENS IN UI==
		Model chosenModel = models.get(0);
		//5. The system displays the ordering form. ==HAPPENS IN UI==
		int amountOfOptions = chosenModel.getAmountOfOptions();
		assertTrue(amountOfOptions>0);
		for(int i = 0; i<amountOfOptions; i++){
			Option option = chosenModel.getModelOption(i);
			int amountOfChoices = option.getAmountOfChoices();
			assertTrue(amountOfChoices>0);
			for(int j = 0; j<amountOfChoices; j++){
				option.getChoiceName(j);
			}
		}
		//6. The user completes the ordering form.
		int[] choices = new int[amountOfOptions];
		Arrays.fill(choices, 0);
		Specification specs = new Specification(choices);
		//7. The system stores the new order and updates the production schedule.
		orderHandler.chooseModelAndSpecifications(chosenModel, specs);
		//8. The system presents an estimated completion date for the new order.
		initPending = orderHandler.getIncompleteOrders();
		initComplete = orderHandler.getCompletedOrders();
		assertTrue(initPending.size() == 1);
		assertTrue(initComplete.isEmpty());
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		List<OrderContainer> initPending = orderHandler.getIncompleteOrders();
		List<OrderContainer> initComplete = orderHandler.getCompletedOrders();
		assertTrue(initPending.isEmpty());
		assertTrue(initComplete.isEmpty());
		//1. (a) The user indicates he wants to leave the overview. ==HAPPENS IN UI==
		//2. The use case ends here.
	}
	
	@Test
	public void alternateFlow2Initial_test() {
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		List<OrderContainer> initPending = orderHandler.getIncompleteOrders();
		List<OrderContainer> initComplete = orderHandler.getCompletedOrders();
		assertTrue(initPending.isEmpty());
		assertTrue(initComplete.isEmpty());
		//2. The user indicates he wants to place a new car order. ==HAPPENS IN UI==
		//3. The system shows a list of available car models.
		List<Model> models = orderHandler.getNewOrderModels();	
		assertTrue(!models.isEmpty());
		//4. The user indicates the car model he wishes to order. ==HAPPENS IN UI==
		Model chosenModel = models.get(0);
		//5. The system displays the ordering form. ==HAPPENS IN UI==
		int amountOfOptions = chosenModel.getAmountOfOptions();
		assertTrue(amountOfOptions>0);
		for(int i = 0; i<amountOfOptions; i++){
			Option option = chosenModel.getModelOption(i);
			int amountOfChoices = option.getAmountOfChoices();
			assertTrue(amountOfChoices>0);
			for(int j = 0; j<amountOfChoices; j++){
				option.getChoiceName(j);
			}
		}
		//6. (a) The user indicates he wants to cancel placing the order. ==HAPPENS IN UI==
		//7. The use case returns to step 1.
		initPending = orderHandler.getIncompleteOrders();
		initComplete = orderHandler.getCompletedOrders();
		assertTrue(initPending.isEmpty());
		assertTrue(initComplete.isEmpty());
	}

}
