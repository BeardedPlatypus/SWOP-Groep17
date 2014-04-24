package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import domain.car.Option;
import domain.car.OptionCategory;
import domain.order.SingleOrderSession;
import domain.order.SingleTaskCatalog;
/**
 * @author Simon Slangen
 *
 */
@RunWith(MockitoJUnitRunner.class )
public class SingleOrderSessionTest {
	@Mock Manufacturer mockManufacturer;
	@Mock SingleTaskCatalog singleTaskCatalog;
	@Mock OptionCategory optionCategory;
	@Mock Option option;
	
	SingleOrderSession singleOrderSession;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		this.singleOrderSession = new SingleOrderSession(mockManufacturer, singleTaskCatalog);
	}
	
	@Test
	public void test_getPossibleTasks() {
		ArrayList<OptionCategory> possibleTasks = new ArrayList<>();
		possibleTasks.add(optionCategory);
		
		Mockito.when(singleTaskCatalog.getPossibleTasks()).thenReturn(possibleTasks);
		
		assertEquals(singleOrderSession.getPossibleTasks(), possibleTasks);
	}
	
	@Test
	public void test_orderDetailsSpecified_0() {
		SingleOrderSession singleOrderSessionTemp = new SingleOrderSession(mockManufacturer, singleTaskCatalog);
		
		assertFalse(singleOrderSessionTemp.orderDetailsSpecified());
	}
	
	@Test
	public void test_orderDetailsSpecified_1() {
		SingleOrderSession singleOrderSessionTemp = new SingleOrderSession(mockManufacturer, singleTaskCatalog);
		
		singleOrderSessionTemp.selectOption(option);
		assertFalse(singleOrderSessionTemp.orderDetailsSpecified());
	}
	
	@Test
	public void test_orderDetailsSpecified_2() {
		SingleOrderSession singleOrderSessionTemp = new SingleOrderSession(mockManufacturer, singleTaskCatalog);
		
		singleOrderSessionTemp.selectOption(option);
		singleOrderSessionTemp.specifyDeadline(1, 2, 3);
		
		assertTrue(singleOrderSessionTemp.orderDetailsSpecified());
	}
	
	@Test
	public void test_orderDetailsSpecified_3() {
		SingleOrderSession singleOrderSessionTemp = new SingleOrderSession(mockManufacturer, singleTaskCatalog);
		
		singleOrderSessionTemp.specifyDeadline(1, 2, 3);
		
		assertFalse(singleOrderSessionTemp.orderDetailsSpecified());
	}
	
	@Test
	public void test_submitSingleTaskOrder() {
		singleOrderSession.selectOption(option);
		singleOrderSession.specifyDeadline(1, 2, 3);
		
		singleOrderSession.submitSingleTaskOrder();
		
		Mockito.verify(mockManufacturer).submitSingleTaskOrder(option, new DateTime(1,2,3));
	}
}
