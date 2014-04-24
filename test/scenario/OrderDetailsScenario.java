package scenario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.DateTime;
import domain.Manufacturer;
import domain.Model;
import domain.Option;
import domain.OptionCategory;
import domain.Specification;
import domain.TaskType;
import domain.handlers.CheckOrderDetailsHandler;
import domain.order.Order;
import domain.order.OrderContainer;
import domain.order.StandardOrder;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

/**
 * Use case scenario test: Check Order Details
 * 
 * @author Simon Slangen
 *
 */
public class OrderDetailsScenario {
	
	Manufacturer mockManufacturer;
	Order order1;
	Order order2;
	Order order3;
	Order order4;
	Order order5;
	Specification spec1;
	Specification spec2;
	Specification spec3;
	Specification spec4;
	Specification spec5;
	Model model;
	
	CheckOrderDetailsHandler orderDetailsHandler;
	
	@Before
	public void setUp() throws Exception {
		model = new Model("Super model", new ArrayList<OptionCategory>(), 60);
		
		ArrayList<OrderContainer> compList = new ArrayList<OrderContainer>();
		ArrayList<OrderContainer> pendList = new ArrayList<OrderContainer>();
		
		spec1 = new Specification(new ArrayList<Option>(Arrays
				.asList(new Option(TaskType.BODY, "what", "is"))));
		spec2 = new Specification(new ArrayList<Option>(Arrays
				.asList(new Option(TaskType.DRIVETRAIN, "love", "baby"))));
		spec3 = new Specification(new ArrayList<Option>(Arrays
				.asList(new Option(TaskType.ACCESSORIES, "don't", "hurt"))));
		spec4 = new Specification(new ArrayList<Option>(Arrays
				.asList(new Option(TaskType.BODY, "me", "don't"))));
		spec5 = new Specification(new ArrayList<Option>(Arrays
				.asList(new Option(TaskType.DRIVETRAIN, "hurt", "me"))));
		
		order1 = new StandardOrder(model, spec1, 0, new DateTime(2,3,4));
		order2 = new StandardOrder(model, spec1, 1, new DateTime(2,3,5));
		order3 = new StandardOrder(model, spec1, 2, new DateTime(1,2,3));
		order4 = new StandardOrder(model, spec1, 3, new DateTime(1,3,4));
		order5 = new StandardOrder(model, spec1, 4, new DateTime(1,4,5));
		
		order1.setAsCompleted(new DateTime(3, 0, 0));
		order2.setAsCompleted(new DateTime(3, 1, 0));
		
		//Creating order details handler with mock manufacturer.
		orderDetailsHandler = new CheckOrderDetailsHandler(mockManufacturer);
	}

	@Test
	public void normalFlowInitial_pendingOrder() {
		
		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderContainer> initPending = orderDetailsHandler.getPendingOrdersContainers();
		List<OrderContainer> initComplete = orderDetailsHandler.getCompletedOrdersContainers();
		
		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order1.
		orderDetailsHandler.selectPendingOrder(1);
		assertEquals(order3.getSubmissionTime(), orderDetailsHandler.getCurrentOrderSubmissionTime());
		assertEquals(order3.getSpecifications(), orderDetailsHandler.getCurrentOrderSpecification());
		assertTrue(orderDetailsHandler.getCurrentOrderEstimatedCompletionTime() != null);
		assertTrue(orderDetailsHandler.getEstimatedCompletionTime(order1) != null);
		
		
		//4. The user indicates he is finished viewing the details. ==HAPPENS IN UI==
	}
	
	@Test
	public void normalFlowInitial_completedOrder() {
		
		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderContainer> initPending = orderDetailsHandler.getPendingOrdersContainers();
		List<OrderContainer> initComplete = orderDetailsHandler.getCompletedOrdersContainers();
		
		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order1.
		
		orderDetailsHandler.selectCompletedOrder(1);
		assertEquals(order1.getSubmissionTime(), orderDetailsHandler.getCurrentOrderSubmissionTime());
		assertEquals(order1.getSpecifications(), orderDetailsHandler.getCurrentOrderSpecification());
		assertTrue(orderDetailsHandler.getCurrentOrderEstimatedCompletionTime() != null);
		assertTrue(orderDetailsHandler.getEstimatedCompletionTime(order1) != null);
		assertEquals(order1.getCompletionTime(), orderDetailsHandler.getCurrentOrderCompletionTime());
		
		//4. The user indicates he is finished viewing the details. ==HAPPENS IN UI==
		// Implementation of alternate flows is the responsibility of the UI.
	}
}
