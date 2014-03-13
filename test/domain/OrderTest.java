package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OrderTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock DateTime dt1;
	@Mock DateTime dt2;
	@Mock DateTime dt3;
	@Mock Specification spectacles;
	@Mock Model mockSuperModel;

	@Mock Specification spectacles2;
	@Mock Model mockSuperModel2;
	
	Order order1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	
		Mockito.when(this.mockSuperModel2.isValidSpecification(spectacles2)).thenReturn(true);
		order1 = new Order(mockSuperModel2, spectacles2, 0);
	}

	@Test 
	public void test_constructorInvalidSpecifications() {
		exception.expect(IllegalArgumentException.class);
		
		Mockito.when(this.mockSuperModel.isValidSpecification(spectacles)).thenReturn(false);
		Order test = new Order(mockSuperModel, spectacles, 0);
		
		Mockito.verify(this.mockSuperModel, Mockito.times(1));
	}
	
	@Test 
	public void test_constructorNullPointerExceptionModel() {
		exception.expect(NullPointerException.class);
		Order test = new Order(null, spectacles, 0);
	}
	
	@Test
	public void test_constructorNullPointerExceptionSpecs() {
		exception.expect(NullPointerException.class);
		Order test = new Order(mockSuperModel, null, 0);
	}

	@Test
	public void test_constructorValidInput() {
		Mockito.when(this.mockSuperModel.isValidSpecification(spectacles)).thenReturn(true);
		
		Order test = new Order(mockSuperModel, spectacles, 0);
		
		assertEquals(false, test.isCompleted());
		assertEquals(mockSuperModel, test.getModel());
		assertEquals(spectacles, test.getSpecifications());
		assertEquals(0, test.getOrderNumber());
	} 
	
	@Test
	public void test_setAsCompletedIfCompleted() {
		Order spiedOrder = Mockito.spy(order1);
		
		spiedOrder.setAsCompleted();
		assertEquals(true, spiedOrder.isCompleted());
	}
}
