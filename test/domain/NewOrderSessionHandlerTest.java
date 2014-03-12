/**
 * 
 */
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
/**
 * @author Month
 *
 */
@RunWith(MockitoJUnitRunner.class )
public class NewOrderSessionHandlerTest {
	@Mock Manufacturer mockManufacturer;
	@Mock OrderContainer orderContainer;
	
	NewOrderSessionHandler sessionHandler1;
	
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
		
		this.sessionHandler1 = new NewOrderSessionHandler(this.mockManufacturer);
	}

	@Test
	public void test_getPendingOrder() {
		List<OrderContainer> array = new ArrayList<OrderContainer>();
		array.add(this.orderContainer);
		Mockito.when(this.mockManufacturer.getPendingOrderContainers()).thenReturn(array);
		
		assertEquals(array, this.sessionHandler1.getPendingOrders());
		Mockito.verify(this.mockManufacturer).getPendingOrderContainers();
	}
}
