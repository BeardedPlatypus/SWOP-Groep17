package domain.handlers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.handlers.OrderSingleTaskHandler;
import domain.order.SingleOrderSession;
import exceptions.OrderDoesNotExistException;
/**
 * @author Simon Slangen
 *
 */
@RunWith(MockitoJUnitRunner.class )
public class OrderSingleTaskHandlerTest {
	@Mock Manufacturer mockManufacturer;
	@Mock OrderContainer orderContainer;
	@Mock Order order;
	@Mock Option option;
	@Mock SingleOrderSession singleOrderSession;
	@Mock OptionCategory optionCategory;
	DateTime dateTime1;
	
	OrderSingleTaskHandler sessionHandler1;
	
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
		Mockito.when(this.mockManufacturer.startNewSingleTaskOrderSession()).thenReturn(singleOrderSession);
		
		dateTime1 = new DateTime(1,2,3);
		
		this.sessionHandler1 = new OrderSingleTaskHandler(this.mockManufacturer);
		sessionHandler1.startNewOrderSession();
	}
	
	@Test
	public void test_startOrderSession() {
		OrderSingleTaskHandler sessionHandler2 = new OrderSingleTaskHandler(this.mockManufacturer);
		assertFalse(sessionHandler2.isRunningOrderSession());
		sessionHandler2.startNewOrderSession();
		assertTrue(sessionHandler2.isRunningOrderSession());
	}
	
	@Test
	public void test_getPossibleTasks() {
		ArrayList<OptionCategory> possibleTasks = new ArrayList<>();
		possibleTasks.add(optionCategory);
		
		Mockito.when(singleOrderSession.getPossibleTasks()).thenReturn(possibleTasks);
		
		assertEquals(sessionHandler1.getPossibleTasks(), possibleTasks);
	}
	
	@Test
	public void test_selectOption() {
		sessionHandler1.selectOption(option);
		Mockito.verify(singleOrderSession).selectOption(option);
	}
	
	@Test
	public void test_specifyDeadline() {
		sessionHandler1.specifyDeadline(1,2,3);
		Mockito.verify(singleOrderSession).specifyDeadline(1,2,3);
	}
	
	@Test
	public void test_getEstimatedCompletionTime() throws OrderDoesNotExistException {
		Mockito.when(mockManufacturer.getEstimatedCompletionTime(order)).thenReturn(dateTime1);
		
		assertEquals(sessionHandler1.getEstimatedCompletionTime(order),dateTime1);
	}
	
	@Test
	public void test_submitOrder() {
		Mockito.when(singleOrderSession.submitSingleTaskOrder()).thenReturn(order);
		assertEquals(sessionHandler1.submitSingleTaskOrder(),order);
	}
}
