package scenario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.DateTime;
import domain.Manufacturer;
import domain.assembly_line.TaskType;
import domain.car.CarModel;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Specification;
import domain.car.Model;
import domain.handlers.CheckOrderDetailsHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
import domain.order.Order;
import domain.order.OrderView;
import domain.order.StandardOrder;

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
	InitialisationHandler init;
	DomainFacade facade;

	@Before
	public void setUp() throws Exception {
		init = new InitialisationHandler();
		facade = init.getDomainFacade();
		orderDetailsHandler = facade.getCheckOrderDetailsHandler();
	}

	@Test
	public void normalFlowInitial_pendingOrder() {

		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderView> initPending = orderDetailsHandler.getPendingOrdersContainers();
		List<OrderView> initComplete = orderDetailsHandler.getCompletedOrdersContainers();

		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order1.
		orderDetailsHandler.selectPendingOrder(initPending.size() - 1);
		assertNotNull(orderDetailsHandler.getCurrentOrderSubmissionTime());
		assertFalse(orderDetailsHandler.currentOrderIsComplete());
		assertTrue(orderDetailsHandler.getCurrentOrderEstimatedCompletionTime() != null);
		assertTrue(orderDetailsHandler.getEstimatedCompletionTime(order1) != null);


		//4. The user indicates he is finished viewing the details. ==HAPPENS IN UI==
	}

	@Test
	public void normalFlow_onAssemblyLine() {
		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderView> initPending = orderDetailsHandler.getPendingOrdersContainers();
		List<OrderView> initComplete = orderDetailsHandler.getCompletedOrdersContainers();

		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order1.
		orderDetailsHandler.selectPendingOrder(0);
		assertNotNull(orderDetailsHandler.getCurrentOrderSubmissionTime());
		assertFalse(orderDetailsHandler.currentOrderIsComplete());
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
		List<OrderView> initPending = orderDetailsHandler.getPendingOrdersContainers();
		List<OrderView> initComplete = orderDetailsHandler.getCompletedOrdersContainers();

		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order1.

		orderDetailsHandler.selectCompletedOrder(0);
		assertNotNull(orderDetailsHandler.getCurrentOrderSubmissionTime());
		assertTrue(orderDetailsHandler.getCurrentOrderEstimatedCompletionTime() != null);
		assertTrue(orderDetailsHandler.getEstimatedCompletionTime(order1) != null);
		assertTrue(orderDetailsHandler.currentOrderIsComplete());

		//4. The user indicates he is finished viewing the details. ==HAPPENS IN UI==
		// Implementation of alternate flows is the responsibility of the UI.
	}
}
