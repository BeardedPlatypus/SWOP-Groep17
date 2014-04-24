package domain.productionSchedule.strategy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.*;
import org.powermock.reflect.Whitebox;

import domain.DateTime;
import domain.Model;
import domain.Specification;
import domain.order.Order;
import domain.order.StandardOrder;

@RunWith(PowerMockRunner.class)
public class FifoStrategyTest {
	
	FifoStrategy<StandardOrder> strat = new FifoStrategy<>();
	
	StandardOrder order1;
	StandardOrder order2;
	StandardOrder order3;
	StandardOrder order4;
	
	StandardOrder newOrder1;
	StandardOrder newOrder2;
	StandardOrder newOrder3;
	
	@Mock Model model;
	@Mock Specification spec;
	
	List<StandardOrder> orderQueue;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		strat = new FifoStrategy<StandardOrder>();
		
		order1 = new StandardOrder(model, spec, 0, new DateTime(1, 0, 0));
		order2 = new StandardOrder(model, spec, 1, new DateTime(2, 0, 0));
		order3 = new StandardOrder(model, spec, 2, new DateTime(3, 0, 0));
		order4 = new StandardOrder(model, spec, 3, new DateTime(4, 0, 0));
		
		newOrder1 = new StandardOrder(model, spec, 4, new DateTime(0, 0, 0));
		newOrder2 = new StandardOrder(model, spec, 5, new DateTime(5, 0, 0));
		newOrder3 = new StandardOrder(model, spec, 6, new DateTime(3, 12, 0));
		
		orderQueue = new ArrayList<StandardOrder>(Arrays.asList(order1, order2, order3, order4));
	}

	@Test
	public void binarySearch_test() {
		int index;
		try {
			index = Whitebox.invokeMethod(strat, "binarySearch", newOrder1, orderQueue);
			assertEquals(0, index);
			
			index = Whitebox.invokeMethod(strat, "binarySearch", order1, orderQueue);
			assertEquals(0, index);
			
			index = Whitebox.invokeMethod(strat, "binarySearch", order2, orderQueue);
			assertEquals(1, index);
			
			index = Whitebox.invokeMethod(strat, "binarySearch", order3, orderQueue);
			assertEquals(2, index);
			
			index = Whitebox.invokeMethod(strat, "binarySearch", order4, orderQueue);
			assertEquals(3, index);
			
			index = Whitebox.invokeMethod(strat, "binarySearch", newOrder2, orderQueue);
			assertEquals(3, index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sortTest() {
		List<StandardOrder> otherQueue = new ArrayList<>(Arrays.asList(order3, order1, order2, order4));
		assertFalse(this.orderQueue.equals(otherQueue));
		strat.sort(otherQueue);
		assertEquals(otherQueue, this.orderQueue);
	}
	
	@Test
	public void addToTest_emptyList() {
		List<StandardOrder> orderQueue = new ArrayList<StandardOrder>();
		strat.addTo(order1, orderQueue);
		assertEquals(order1, orderQueue.get(0));
	}
	
	@Test
	public void addToTest_singleElement() {
		List<StandardOrder> orderQueue = new ArrayList<StandardOrder>(Arrays.asList(order2));
		strat.addTo(order1, orderQueue);
		assertEquals(orderQueue.get(0), order1);
		
		orderQueue = new ArrayList<StandardOrder>(Arrays.asList(order2));
		strat.addTo(order3, orderQueue);
		assertEquals(orderQueue.get(0), order2);
	}
	
	@Test
	public void addToTest_beginning() {
		strat.addTo(newOrder1, orderQueue);
		assertEquals(newOrder1, orderQueue.get(0));
	}
	
	@Test
	public void addToTest_middle() {
		StandardOrder order = new StandardOrder(model, spec, 6, new DateTime(3, 0, 0));
		strat.addTo(order, orderQueue);
		assertEquals(order, orderQueue.get(2));
	}
	
	@Test
	public void addToTest_end() {
		strat.addTo(newOrder2, orderQueue);
		assertEquals(newOrder2, orderQueue.get(orderQueue.size() - 1));
		
		orderQueue = new ArrayList<StandardOrder>(Arrays.asList(order1, order2, order3, order4));
		strat.addTo(newOrder3, orderQueue);
		assertEquals(newOrder3, orderQueue.get(orderQueue.size() - 2));
		assertEquals(order4, orderQueue.get(orderQueue.size() - 1));
	}
	
	@Test
	public void isDoneTest() {
		assertFalse(strat.isDone(orderQueue));
	}

}
