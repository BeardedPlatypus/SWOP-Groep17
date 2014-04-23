package domain.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.DateTime;
import domain.Model;
import domain.Specification;

public class SingleTaskOrderTest {
	//--------------------------------------------------------------------------
	// Test variables.
	//--------------------------------------------------------------------------
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock DateTime dt1;
	@Mock DateTime dt2;
	@Mock DateTime dt3;
	@Mock Specification spectacles;
	@Mock Model mockSuperModel;

	@Mock Specification spectacles2;
	@Mock Model mockSuperModel2;
	
	@Mock DateTime submission1;
	@Mock DateTime submission2;
	@Mock DateTime submission3;
	
	@Mock DateTime deadline1;
	@Mock DateTime deadline2;
	@Mock DateTime deadline3;
	
	SingleTaskOrder order1;
	SingleTaskOrder order2;
	SingleTaskOrder order3;
	
	//--------------------------------------------------------------------------
	// Setup Test
	//--------------------------------------------------------------------------
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	
		order1 = new SingleTaskOrder(mockSuperModel2, spectacles2, 0, submission1, deadline1);
		order2 = new SingleTaskOrder(mockSuperModel2, spectacles2, 0, submission1, deadline2);
		order3 = new SingleTaskOrder(mockSuperModel2, spectacles2, 1, submission1, deadline3);
	}

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	@Test 
	public void test_constructorNullPointerExceptionModel() {
		exception.expect(NullPointerException.class);
		Order test = new SingleTaskOrder(null, spectacles, 0, submission1, deadline1);
	}
	
	@Test
	public void test_constructorNullPointerExceptionSpecs() {
		exception.expect(NullPointerException.class);
		Order test = new SingleTaskOrder(mockSuperModel, null, 0, submission1, deadline1);
	}

	@Test
	public void test_constructorNullPointerExceptionSubmissiontime() {
		exception.expect(NullPointerException.class);
		Order test = new SingleTaskOrder(mockSuperModel, spectacles, 0, null, deadline1);
	}
	
	@Test
	public void test_constructorNullPointerExceptionDeadline() {
		exception.expect(NullPointerException.class);
		Order test = new SingleTaskOrder(mockSuperModel, spectacles, 0, submission1, null);
	}
	
	@Test
	public void test_constructorValidInput() {
		
		SingleTaskOrder test = new SingleTaskOrder(mockSuperModel, spectacles, 0, submission1, deadline1);
		
		assertEquals(false, test.isCompleted());
		assertEquals(mockSuperModel, test.getModel());
		assertEquals(spectacles, test.getSpecifications());
		assertEquals(0, test.getOrderNumber());
		assertEquals(submission1, test.getSubmissionTime());
		assertEquals(deadline1, test.getDeadline());
	}
	
	
	//--------------------------------------------------------------------------
	// Completion methods.
	//--------------------------------------------------------------------------
	@Test
	public void test_setAsCompletedIfCompleted() {
		SingleTaskOrder spiedOrder = Mockito.spy(order1);
		
		Mockito.when(submission1.getDays()).thenReturn(0);
		Mockito.when(submission1.getHours()).thenReturn(0);
		Mockito.when(submission1.getMinutes()).thenReturn(0);
		
		Mockito.when(dt2.getDays()).thenReturn(0);
		Mockito.when(dt2.getHours()).thenReturn(0);
		Mockito.when(dt2.getMinutes()).thenReturn(1);
		
		spiedOrder.setAsCompleted(dt2);
		assertEquals(true, spiedOrder.isCompleted());
	}
	
	@Test
	public void testMinutesAtPost() {
		Mockito.when(mockSuperModel2.getMinsPerWorkPost()).thenReturn(30);
		assertTrue(order1.getMinutesPerPost() == 30);
	}
	
	//--------------------------------------------------------------------------
	// Completion Time setter and getters.
	//--------------------------------------------------------------------------
	@Test
	public void test_getCompletedFromIncompletedOrder() {
		exception.expect(IllegalStateException.class);
		
		order1.getCompletionTime();
	}
	
	@Test
	public void test_setCompletedValid() {

		Mockito.when(submission1.getDays()).thenReturn(0);
		Mockito.when(submission1.getHours()).thenReturn(0);
		Mockito.when(submission1.getMinutes()).thenReturn(0);
		
		Mockito.when(dt2.getDays()).thenReturn(0);
		Mockito.when(dt2.getHours()).thenReturn(0);
		Mockito.when(dt2.getMinutes()).thenReturn(1);
		
		order1.setAsCompleted(dt2);
		
		assertEquals(dt2, order1.getCompletionTime());
	}
	
	@Test
	public void test_setCompletedNullTime() {
		exception.expect(NullPointerException.class);
		
		Mockito.when(submission1.getDays()).thenReturn(0);
		Mockito.when(submission1.getHours()).thenReturn(0);
		Mockito.when(submission1.getMinutes()).thenReturn(0);
		
		order1.setAsCompleted(null);
	}
	
	@Test
	public void test_setCompletedAlreadyCompleted() {
		exception.expect(IllegalStateException.class);
		
		Mockito.when(submission1.getDays()).thenReturn(0);
		Mockito.when(submission1.getHours()).thenReturn(0);
		Mockito.when(submission1.getMinutes()).thenReturn(0);
		Mockito.when(submission1.getInMinutes()).thenReturn((long) 0);
		
		Mockito.when(dt2.getDays()).thenReturn(0);
		Mockito.when(dt2.getHours()).thenReturn(0);
		Mockito.when(dt2.getMinutes()).thenReturn(1);
		Mockito.when(dt2.getInMinutes()).thenReturn((long) 1);
		
		Mockito.when(dt3.getDays()).thenReturn(0);
		Mockito.when(dt3.getHours()).thenReturn(0);
		Mockito.when(dt3.getMinutes()).thenReturn(1);
		Mockito.when(dt3.getInMinutes()).thenReturn((long) 1);

		order1.setAsCompleted(dt2);
		order1.setAsCompleted(dt3);
	}

	@Test
	public void test_setCompletedBeforeSubmission() {
		exception.expect(IllegalArgumentException.class);
		
		Mockito.when(dt2.compareTo(submission1)).thenReturn(-1);

		order1.setAsCompleted(dt2);
	}
	
	//--------------------------------------------------------------------------
	// Equals
	//--------------------------------------------------------------------------
	@Test
	public void test_orderCompletedItself() {
		assertEquals(true, order1.equals(order1));
	}
	
	@Test
	public void test_orderCompletedDifferentButSame() {
		assertEquals(true, order1.equals(order2));
	}
	
	@Test
	public void test_orderCompletedNull() {
		assertEquals(false, order1.equals(null));
	}
	
	@Test
	public void test_orderCompletedDifClass() {
		assertEquals(false, order1.equals("Ik lust opzich wel een peanutbutter sandwich"));
	}
	
	@Test
	public void test_orderCompletedDifOrder() {
		assertEquals(false, order1.equals(order3));
	}
}