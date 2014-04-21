package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import exceptions.OrderDoesNotExistException;
/**
 * @author Simon Slangen
 *
 */
@RunWith(MockitoJUnitRunner.class )
public class SingleOrderSessionTest {
	@Mock Manufacturer mockManufacturer;
	@Mock OrderContainer orderContainer;
	@Mock SingleOrderSession singleOrderSession;
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
		Mockito.when(mockManufacturer.getEstimatedCompletionTime((Order) orderContainer)).thenReturn(dateTime1);
		
		this.sessionHandler1 = new OrderSingleTaskHandler(this.mockManufacturer);
	}
	
	@Test
	public void test_startOrderSession() {
		OrderSingleTaskHandler sessionHandler2 = new OrderSingleTaskHandler(this.mockManufacturer);
		assertFalse(sessionHandler2.isRunningOrderSession());
		sessionHandler2.startNewOrderSession();
		assertTrue(sessionHandler2.isRunningOrderSession());
	}
	
	@Test
	public void test_getEstimatedCompletionTime() throws OrderDoesNotExistException {
		assertEquals(sessionHandler1.getEstimatedCompletionTime(orderContainer),dateTime1);
	}
}
