package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.order.Order;
import domain.order.OrderContainer;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

//TODO: Not a good scenario test atm, more like a unit test of the handler...
/**
 * Use case scenario test: Check Order Details
 * 
 * @author Simon Slangen
 *
 */
public class OrderDetailsScenario {
	
	@Mock Manufacturer mockManufacturer;
	@Mock Order order1;
	@Mock Order order2;
	@Mock Order order3;
	@Mock Order order4;
	@Mock Order order5;
	@Mock Specification spec1;
	@Mock Specification spec2;
	@Mock Specification spec3;
	@Mock Specification spec4;
	@Mock Specification spec5;
	
	OrderDetailsHandler orderDetailsHandler;
	
	@Before
	public void setUp() throws Exception {
		ArrayList<OrderContainer> compList = new ArrayList<OrderContainer>();
		ArrayList<OrderContainer> pendList = new ArrayList<OrderContainer>();
		
		//Mocking completed orders.
		Mockito.when(order1.isCompleted()).thenReturn(true);
		Mockito.when(order1.getCompletionTime()).thenReturn(new DateTime(2,3,4));
		Mockito.when(order1.getSpecifications()).thenReturn(spec1);
		compList.add(order1);
		
		Mockito.when(order2.isCompleted()).thenReturn(true);
		Mockito.when(order2.getCompletionTime()).thenReturn(new DateTime(2,4,5));
		Mockito.when(order2.getSpecifications()).thenReturn(spec2);
		compList.add(order2);
		
		//Mocking pending orders.
		Mockito.when(order3.isCompleted()).thenReturn(true);
		Mockito.when(mockManufacturer.getEstimatedCompletionTime(order3)).thenReturn(new DateTime(1,2,3));
		Mockito.when(order3.getSpecifications()).thenReturn(spec3);
		pendList.add(order3);
		
		Mockito.when(order4.isCompleted()).thenReturn(true);
		Mockito.when(mockManufacturer.getEstimatedCompletionTime(order4)).thenReturn(new DateTime(1,3,4));
		Mockito.when(order4.getSpecifications()).thenReturn(spec4);
		pendList.add(order4);
		
		Mockito.when(order5.isCompleted()).thenReturn(true);
		Mockito.when(mockManufacturer.getEstimatedCompletionTime(order5)).thenReturn(new DateTime(1,4,5));
		Mockito.when(order5.getSpecifications()).thenReturn(spec5);
		pendList.add(order5);
		
		//Adding to mock manufacturer.
		Mockito.when(mockManufacturer.getCompletedOrderContainers()).thenReturn(compList);
		Mockito.when(mockManufacturer.getPendingOrderContainers()).thenReturn(pendList);
		
		//Creating order details handler with mock manufacturer.
		OrderDetailsHandler orderDetailsHandler = new orderDetailsHandler(mockManufacturer);
	}

	@Test
	public void normalFlowInitial_test() {
		
		//1. The system presents an overview of the orders placed by the user,
		//   divided into two parts. The first part shows a list of pending orders,
		//   with estimated completion times, and the second part shows a history
		//   of completed orders, sorted most recent first.
		List<OrderContainer> initPending = orderDetailsHandler.getPendingOrders();
		List<OrderContainer> initComplete = orderDetailsHandler.getCompletedOrders();
		
		for(OrderContainer oc : initPending){
			assertTrue(!oc.isCompleted());
			DateTime eta = orderDetailsHandler.getEstimatedCompletionTime(oc);
			assertNotNull(eta);
		}
		
		for(OrderContainer oc : initComplete){
			assertTrue(oc.isCompleted());
			DateTime eta = orderDetailsHandler.getCompletionTime(oc);
			assertNotNull(eta);
		}
		
		//2. The user indicates the order he wants to check the details for. ==HAPPENS IN UI==
		//3. The system shows the details of the order3.
		assertEquals(initComplete.get(0), spec1);
		assertEquals(initComplete.get(1), spec2);
		
		assertEquals(initPending.get(0), spec3);
		assertEquals(initPending.get(1), spec4);
		assertEquals(initPending.get(2), spec5);
		
		//4. The user indicates he is finished viewing the details. ==HAPPENS IN UI==
	}
	
	@Test
	public void alternateFlow1Initial_test() {
		//1. (a) The user indicates he wants to leave the overview.
		List<OrderContainer> initPending = orderDetailsHandler.getPendingOrders();
		List<OrderContainer> initComplete = orderDetailsHandler.getCompletedOrders();
		
		for(OrderContainer oc : initPending){
			assertTrue(!oc.isCompleted());
			DateTime eta = orderDetailsHandler.getEstimatedCompletionTime(oc);
			assertNotNull(eta);
		}
		
		for(OrderContainer oc : initComplete){
			assertTrue(oc.isCompleted());
			DateTime eta = orderDetailsHandler.getCompletionTime(oc);
			assertNotNull(eta);
		}
		//2. The use case ends here.
	}

}
