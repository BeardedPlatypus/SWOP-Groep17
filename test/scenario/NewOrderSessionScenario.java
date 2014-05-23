package scenario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.DateTime;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Model;
import domain.handlers.InitialisationHandler;
import domain.handlers.NewOrderSessionHandler;
import domain.order.OrderView;
import exceptions.IllegalVehicleOptionCombinationException;
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
	
	NewOrderSessionHandler orderHandler;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		orderHandler = init.getDomainFacade().getNewOrderSessionHandler();
	}

	@Test
	public void normalFlowInitial_test() {
		
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		// System is currently empty
		List<OrderView> initPending = orderHandler.getPendingOrders();
		int pendingSize = initPending.size();
		Random rand = new Random();

		boolean accepted = false;
		while(!accepted){
			//2. The user indicates he wants to place a new car order. ==HAPPENS IN UI==
			orderHandler.startNewOrderSession();


			//3 and 4. The system shows a list of available car models. User selects one.
			List<Model> models = orderHandler.getVehicleModels();	
			assertTrue(!models.isEmpty());
			orderHandler.chooseModel(models.get(0));

			//5. The system displays the ordering form. ==HAPPENS IN UI==
			
			//6. The user completes the ordering form.
			List<Option> options = new ArrayList<Option>();
			while(orderHandler.hasUnfilledOptions()){
				OptionCategory optCat = orderHandler.getNextOptionCategory();
				Option opt = optCat.getOption(rand.nextInt(optCat.getAmountOfOptions()));
				options.add(opt);
				orderHandler.selectOption(opt);
			}
			try{
				//7. The system stores the new order and updates the production schedule.
				orderHandler.submitOrder();
				accepted = true;
			} catch (OptionRestrictionException e) {
				accepted = false;
			}
		}
		
		//8. The system presents an estimated completion date for the new order.
		assertTrue(orderHandler.getNewOrderETA() != null);
		initPending = orderHandler.getPendingOrders();
		assertTrue(initPending.size() == pendingSize + 1);
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		//1. The system presents an overview of the orders placed by the user, divided into two parts. The first part shows a list of pending orders, 
		// with estimated completion times, and the second part shows a history	of completed orders, sorted most recent first.
		List<OrderView> initPending = orderHandler.getPendingOrders();
		List<OrderView> initComplete = orderHandler.getCompletedOrders();
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
		List<OrderView> initPending = orderHandler.getPendingOrders();
		int pendingSize = initPending.size();
		
		
		//2. The user indicates he wants to place a new car order. ==HAPPENS IN UI==
		orderHandler.startNewOrderSession();
		//3. The system shows a list of available car models.
		List<Model> models = orderHandler.getVehicleModels();	
		assertTrue(!models.isEmpty());
		
		//4. The user indicates the car model he wishes to order.
		Model chosenModel = models.get(0);
		orderHandler.chooseModel(chosenModel);
		
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
		initPending = orderHandler.getPendingOrders();
		assertTrue(initPending.size() == pendingSize);
	}

}
