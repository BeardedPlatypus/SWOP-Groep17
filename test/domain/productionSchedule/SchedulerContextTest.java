package domain.productionSchedule;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.DateTime;
import domain.TaskType;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.productionSchedule.strategy.BatchStrategy;
import domain.productionSchedule.strategy.FifoStrategy;

public class SchedulerContextTest {
	//--------------------------------------------------------------------------
	// Test variables. 
	//--------------------------------------------------------------------------
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock DateTime t1;
	@Mock DateTime t2;
	@Mock DateTime t3;
	
	@Mock FifoStrategy<StandardOrder> strat;
	@Mock BatchStrategy<StandardOrder> otherStrat;
	
	@Mock StandardOrder order1;
	@Mock StandardOrder order2;
	@Mock SingleTaskOrder order3;
	@Mock SingleTaskOrder order4;
	
	List<TaskType> types= new ArrayList<TaskType>(Arrays.asList(TaskType.ACCESSORIES, TaskType.BODY));
	
	SchedulerContext schedCon;
	//--------------------------------------------------------------------------
	// Setup
	//--------------------------------------------------------------------------
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(order1.getSubmissionTime()).thenReturn(new DateTime(1, 2, 3));
		Mockito.when(order2.getSubmissionTime()).thenReturn(new DateTime(4, 5, 6));
		Mockito.when(order3.getDeadline()).thenReturn(new DateTime(0, 2, 3));
		Mockito.when(order3.getSingleTaskOrderType()).thenReturn(TaskType.BODY);
		Mockito.when(order4.getDeadline()).thenReturn(new DateTime(0, 3, 3));
		Mockito.when(order4.getSingleTaskOrderType()).thenReturn(TaskType.ACCESSORIES);
		schedCon = new SchedulerContext(strat, types);		
	}

	//--------------------------------------------------------------------------
	// Constructor Test
	//--------------------------------------------------------------------------
	@Test
	public void testConstructorNullStrat() {
		exception.expect(IllegalArgumentException.class);
		new SchedulerContext(null, types);
	}
	
	@Test
	public void testConstructorNullList() {
		exception.expect(IllegalArgumentException.class);
		new SchedulerContext(strat, null);
	}
	
	@Test
	public void testConstructorNullInList() {
		exception.expect(IllegalArgumentException.class);
		List<TaskType> nullList = new ArrayList<>();
		nullList.add(null);
		new SchedulerContext(strat, nullList);
	}
	
	@Test
	public void testConstructorValid() {
		assertEquals(strat, schedCon.getCurrentSchedulingStrategy());
	}

	@Test
	public void testSetSchedulingStrategy() {
		assertEquals(strat, schedCon.getCurrentSchedulingStrategy());
		schedCon.setSchedulingStrategy(otherStrat);
		assertEquals(otherStrat, schedCon.getCurrentSchedulingStrategy());
	}
	
	@Test
	public void testSetSchedulingStrategyNull() {
		exception.expect(IllegalArgumentException.class);
		assertEquals(strat, schedCon.getCurrentSchedulingStrategy());
		schedCon.setSchedulingStrategy(null);
	}

	@Test
	public void testGetDefaultStrategy() {
		assertEquals(strat, schedCon.getCurrentSchedulingStrategy());
		assertEquals(strat, schedCon.getDefaultStrategy());
		schedCon.setSchedulingStrategy(otherStrat);
		assertEquals(otherStrat, schedCon.getCurrentSchedulingStrategy());
		assertEquals(strat, schedCon.getDefaultStrategy());
	}

	@Test
	public void testContainsOrder() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		assertFalse(schedCon.containsOrder(order1));
		schedCon.addNewStandardOrder(order1);
		assertTrue(schedCon.containsOrder(order1));
	}

	@Test
	public void testGetAllPendingOrders() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewStandardOrder(order1);
		schedCon.addNewStandardOrder(order2);
		schedCon.addNewSingleTaskOrder(order3);
		assertTrue(schedCon.getAllPendingOrders().contains(order1));
		assertTrue(schedCon.getAllPendingOrders().contains(order2));
		assertTrue(schedCon.getAllPendingOrders().contains(order3));
	}

	@Test
	public void testGetNextScheduledOrderContainer() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewStandardOrder(order1);
		schedCon.addNewStandardOrder(order2);
		assertEquals(order1, schedCon.getNextScheduledOrderContainer());
	}

	@Test
	public void testGetNextScheduledOrder() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewStandardOrder(order1);
		schedCon.addNewStandardOrder(order2);
		assertEquals(order1, schedCon.getNextScheduledOrder());
	}

	@Test
	public void testGetPendingStandardOrders() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewStandardOrder(order1);
		schedCon.addNewStandardOrder(order2);
		assertTrue(schedCon.getPendingStandardOrders().contains(order1));
		assertTrue(schedCon.getPendingStandardOrders().contains(order2));
	}

	@Test
	public void testGetOrderQueue() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewStandardOrder(order1);
		schedCon.addNewStandardOrder(order2);
		assertTrue(schedCon.getOrderQueue().contains(order1));
		assertTrue(schedCon.getOrderQueue().contains(order2));
	}

	@Test
	public void testGetPendingSingleTaskOrders() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewStandardOrder(order1);
		schedCon.addNewStandardOrder(order2);
		schedCon.addNewSingleTaskOrder(order3);
		assertTrue(schedCon.getPendingSingleTaskOrders().contains(order3));
	}

	@Test
	public void testGetNextSingleTaskOrderOfType() {
		FifoStrategy<StandardOrder> realStrat = new FifoStrategy<>();
		schedCon.setSchedulingStrategy(realStrat);
		schedCon.addNewSingleTaskOrder(order3);
		schedCon.addNewSingleTaskOrder(order4);
		assertEquals(order3,schedCon.getNextSingleTaskOrderOfType(TaskType.BODY));
		assertEquals(order4,schedCon.getNextSingleTaskOrderOfType(TaskType.ACCESSORIES));
	}

	@Test
	public void testPopNextSingleTaskOrderOfType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSingleTaskOrdersOfType() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewStandardOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewSingleTaskOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsValidPendingOrder() {
		fail("Not yet implemented");
	}
}
