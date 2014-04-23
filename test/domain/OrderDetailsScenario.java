package domain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
import domain.order.OrderContainer;

import exceptions.OrderDoesNotExistException;

/**
 * Use case scenario test: Check Order Details
 * 
 * @author Simon Slangen
 *
 */
public class OrderDetailsScenario {
	
	DomainFacade facade;
	InteractionSimulator sim;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
		
		sim = new InteractionSimulator(facade);
		
		//Simulate placing 5 orders.
		sim.simulatePlaceOrder(5);
		//Complete all 5 orders.
		sim.simulateCompleteAllOrders();
		//Simulate placing 3 more orders.
		sim.simulatePlaceOrder(3);
		//Partially complete these orders, but none entirely.
		sim.simulateCompleteAllTasksOnAssemblyLine(2);
	}

	@Test
	public void normalFlowInitial_test() {
		
		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderContainer> initPending = facade.getPendingOrders();
		assertEquals(initPending.size(), 3);
		
		List<OrderContainer> initComplete = facade.getCompletedOrders();
		assertEquals(initComplete.size(), 5);
		
		for(OrderContainer oc : initPending){
			assertTrue(!oc.isCompleted());
			DateTime eta = null;
			try {
				eta = facade.getEstimatedCompletionTime(oc);
			} catch (OrderDoesNotExistException e) {
				fail();
			}
			assertNotNull(eta);
		}
		
		for(OrderContainer oc : initComplete){
			assertTrue(oc.isCompleted());
			DateTime eta = oc.getCompletionTime();
			assertNotNull(eta);
		}
		
		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order3.
		OrderContainer order = initPending.get(0);
		order.getOrderNumber();
		assertNotNull(order.getSpecifications());
		assertNotNull(order.getModel());
		
		//4. The user indicates he is finished viewing the details. ==HAPPENS IN UI==
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderContainer> initPending = facade.getPendingOrders();
		assertEquals(initPending.size(), 3);
		
		List<OrderContainer> initComplete = facade.getCompletedOrders();
		assertEquals(initComplete.size(), 5);
		
		for(OrderContainer oc : initPending){
			assertTrue(!oc.isCompleted());
			DateTime eta = null;
			try {
				eta = facade.getEstimatedCompletionTime(oc);
			} catch (OrderDoesNotExistException e) {
				fail();
			}
			assertNotNull(eta);
		}
		
		for(OrderContainer oc : initComplete){
			assertTrue(oc.isCompleted());
			DateTime eta = oc.getCompletionTime();
			assertNotNull(eta);
		}
		
		//2. The use case ends here.
	}

}
