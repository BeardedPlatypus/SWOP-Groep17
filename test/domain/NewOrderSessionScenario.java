package domain;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
import domain.order.OrderContainer;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;

/**
 * Use case scenario test: Order New Car
 * 
 * @author Simon Slangen
 *
 */
public class NewOrderSessionScenario {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	DomainFacade facade;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
	}

	@Test
	public void normalFlowInitial_test() {
		
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		// System is currently empty
		List<OrderContainer> initPending = facade.getPendingOrders();
		List<OrderContainer> initComplete = facade.getCompletedOrders();
		
		//2. The user indicates he wants to place a new car order.
		facade.startNewOrderSession();
		
		//3. The system shows a list of available car models.
		List<Model> models = facade.getCarModels();	
		assertTrue(!models.isEmpty());
		
		//4. The user indicates the car model he wishes to order.
		Model chosenModel = models.get(0);
		facade.chooseModel(chosenModel);
		
		//5. The system displays the ordering form. ==HAPPENS IN UI==
		int amountOfOptionCategories = chosenModel.getAmountOfOptionCategories();
		assertTrue(amountOfOptionCategories>0);
		for(int i = 0; i<amountOfOptionCategories; i++){
			OptionCategory optionCategory = chosenModel.getModelOptionCategory(i);
			int amountOfOptions = optionCategory.getAmountOfOptions();
			assertTrue(amountOfOptions>0);
			for(int j = 0; j<amountOfOptions; j++){
				optionCategory.getOption(j);
			}
		}
		
		//6. The user completes the ordering form.
		LinkedList<Option> options = new LinkedList<Option>();
		while(facade.orderHasUnfilledOptions()){
			OptionCategory optCat = null;
			try {
				optCat = facade.getNextOptionCategory();
			} catch (IllegalStateException
					| NoOptionCategoriesRemainingException e1) {
				fail();
			}
			assertNotNull(optCat);
			boolean validSelection = false;
			assertTrue(optCat.getAmountOfOptions() > 0);
			
			for(int i = 0; i < optCat.getAmountOfOptions() && validSelection; i++){
				Option opt = optCat.getOption(i);
				assertNotNull(opt);
				
				options.add(opt);
				
				if(chosenModel.checkOptionsValidity(options)){
					try {
						facade.selectOption(opt);
					} catch (IllegalStateException
							| NoOptionCategoriesRemainingException e) {
						fail();
					}
					validSelection = true;
				} else {
					options.removeLast();
				}
			}
		}
		assertTrue(!options.isEmpty());
		
		//7. The system stores the new order and updates the production schedule.
		try {
			facade.submitOrder();
		} catch (IllegalStateException | IllegalArgumentException
				| IllegalCarOptionCombinationException
				| OptionRestrictionException e) {
			fail();
		}
		DateTime eta = null;
		try {
			eta = facade.getNewOrderETA();
		} catch (IllegalStateException | OrderDoesNotExistException e) {
			fail();
		}
		assertNotNull(eta);
		
		//8. The system presents an estimated completion date for the new order.
		initPending = facade.getPendingOrders();
		initComplete = facade.getCompletedOrders();
		assertTrue(initPending.size() == 1);
		assertTrue(initComplete.isEmpty());
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		List<OrderContainer> initPending = facade.getPendingOrders();
		List<OrderContainer> initComplete = facade.getCompletedOrders();
		assertTrue(initPending.isEmpty());
		assertTrue(initComplete.isEmpty());
		//1. (a) The user indicates he wants to leave the overview. ==HAPPENS IN UI==
		//2. The use case ends here.
	}
	
	@Test
	public void alternateFlow2Initial_test() {
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		// System is currently empty
		List<OrderContainer> initPending = facade.getPendingOrders();
		List<OrderContainer> initComplete = facade.getCompletedOrders();
		
		//2. The user indicates he wants to place a new car order. ==HAPPENS IN UI==
		//3. The system shows a list of available car models.
		List<Model> models = facade.getCarModels();	
		assertTrue(!models.isEmpty());
		
		//4. The user indicates the car model he wishes to order.
		Model chosenModel = models.get(0);
		facade.chooseModel(chosenModel);
		
		//5. The system displays the ordering form. ==HAPPENS IN UI==
		int amountOfOptionCategories = chosenModel.getAmountOfOptionCategories();
		assertTrue(amountOfOptionCategories>0);
		for(int i = 0; i<amountOfOptionCategories; i++){
			OptionCategory optionCategory = chosenModel.getModelOptionCategory(i);
			int amountOfOptions = optionCategory.getAmountOfOptions();
			assertTrue(amountOfOptions>0);
			for(int j = 0; j<amountOfOptions; j++){
				optionCategory.getOption(j);
			}
		}
		
		//6. (a) The user indicates he wants to cancel placing the order. ==HAPPENS IN UI==
		//7. The use case returns to step 1.
		initPending = facade.getPendingOrders();
		initComplete = facade.getCompletedOrders();
		assertTrue(initPending.isEmpty());
		assertTrue(initComplete.isEmpty());
	}

}
