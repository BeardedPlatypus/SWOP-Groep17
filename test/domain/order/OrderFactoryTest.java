package domain.order;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Model;
import domain.car.Option;
import domain.car.Specification;

public class OrderFactoryTest {
	//--------------------------------------------------------------------------
	// variables.
	//--------------------------------------------------------------------------
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock List<Option> l1;
	@Mock Manufacturer manMock1;
	@Mock Manufacturer manMock2;
	
	@Mock DateTime dt1;
	
	@Mock Model m1;
	@Mock Model m2;
	
	@Mock Specification s1;
	@Mock Specification s2;
	
	@Mock Option mockOption1;
	
	OrderFactory orderFactory;
	OrderFactory of;
	
	//--------------------------------------------------------------------------
	// Setup
	//--------------------------------------------------------------------------
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		orderFactory = new OrderFactory();
		
		of = new OrderFactory();
		Mockito.doReturn(of).when(manMock1).getOrderFactory();
		of.setManufacturer(manMock1);
	}

	
	//--------------------------------------------------------------------------
	// Constructor-Related Tests
	//--------------------------------------------------------------------------
	
	@Test
	public void testGetManufacturerIllegalStateException() {
		exception.expect(IllegalStateException.class);
		
		orderFactory.getManufacturer();
	}

	@Test
	public void testSetManufacturerNull() {
		exception.expect(IllegalArgumentException.class);
		
		orderFactory.setManufacturer(null);
	}
	
	@Test
	public void testSetManufacturerNotSetAtMan() {
		exception.expect(IllegalStateException.class);
		Mockito.doReturn(null).when(manMock1).getOrderFactory();
		
		orderFactory.setManufacturer(manMock1);
	}
	
	@Test
	public void testSetManufacturerAlreadySet() {
		exception.expect(IllegalStateException.class);
		Mockito.doReturn(orderFactory).when(manMock1).getOrderFactory();
		Mockito.doReturn(orderFactory).when(manMock2).getOrderFactory();
		
		orderFactory.setManufacturer(manMock1);
		orderFactory.setManufacturer(manMock2);
	}
	
	@Test
	public void testSetManufacturerValid() {
		Mockito.doReturn(orderFactory).when(manMock1).getOrderFactory();
		orderFactory.setManufacturer(manMock1);
		
		assertEquals(manMock1, orderFactory.getManufacturer());
	}
	
	//--------------------------------------------------------------------------
	// StandardOrder functions.
	//--------------------------------------------------------------------------
	@Test
	public void isValidInputStandardOrderNullModel() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();

		assertFalse(orderFactory.isValidInputStandardOrder(null, s1));
	}
	
	@Test
	public void isValidInputStandardOrderNullSpecification() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();
		
		assertFalse(orderFactory.isValidInputStandardOrder(m1, null));
	}
	
	@Test
	public void isValidInputStandardOrderModelNotContained() {
		Mockito.doReturn(false).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();		
		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}

	@Test
	public void isValidInputStandardOrderOptionsNull() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(null).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();		

		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}

	@Test
	public void isValidInputStandardOrderOptionsContainsNull() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(null).when(s1).getOptions();
		Mockito.doReturn(true).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();		
		
		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}

	@Test
	public void isValidInputStandardOrderOptionsIsEmpty() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(0).when(s1).getAmountOfOptions();
		
		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}
	
	@Test
	public void isValidInputStandardOrderOptionsModelOptionsInvalid() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();
		
		Mockito.doReturn(false).when(m1).checkOptionsValidity(l1);
		Mockito.doReturn(true).when(manMock1).checkSpecificationRestrictions(m1, s1);
		
		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}

	@Test
	public void isValidInputStandardOrderRestrictionInvalid() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();
		
		Mockito.doReturn(true).when(m1).checkOptionsValidity(l1);
		Mockito.doReturn(false).when(manMock1).checkSpecificationRestrictions(m1, s1);
		
		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}
	
	@Test
	public void isValidInputStandardOrderValid() {
		Mockito.doReturn(true).when(manMock1).modelCatalogContains(m1);
		Mockito.doReturn(l1).when(s1).getOptions();
		Mockito.doReturn(false).when(l1).contains(null);
		Mockito.doReturn(5).when(s1).getAmountOfOptions();
		
		Mockito.doReturn(true).when(m1).checkOptionsValidity(l1);
		Mockito.doReturn(true).when(manMock1).checkSpecificationRestrictions(m1, s1);
		
		assertFalse(of.isValidInputStandardOrder(m1, s2));
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void makeNewStandardOrderNonValid() {
		exception.expect(IllegalArgumentException.class);
		
		OrderFactory spiedOrderFactory = Mockito.spy(of);
		Mockito.doReturn(false).when(spiedOrderFactory).isValidInputStandardOrder(m1, s1);
		Mockito.doReturn(dt1).when(spiedOrderFactory).getCurrentTime();
		
		spiedOrderFactory.makeNewStandardOrder(m1, s1);
	}
	
	@Test
	public void makeNewStandardOrderValid() {
		OrderFactory spiedOrderFactory = Mockito.spy(of);
		Mockito.doReturn(true).when(spiedOrderFactory).isValidInputStandardOrder(m1, s1);
		Mockito.doReturn(dt1).when(spiedOrderFactory).getCurrentTime();
		
		StandardOrder o = spiedOrderFactory.makeNewStandardOrder(m1, s1);
		assertEquals(o.getModel(), m1);
		assertEquals(o.getSpecifications(), s1);
		assertEquals(o.getSubmissionTime(), dt1);	
	}
	
	//--------------------------------------------------------------------------
	// SingleOrder Tasks
	//--------------------------------------------------------------------------
	@Test
	public void isValidInputSingleTaskOrderSpecNull() {
		assertFalse(of.isValidInputSingleTaskOrder(dt1, null));
	}

	@Test
	public void isValidInputSingleTaskOrderDeadlineNull() {
		assertFalse(of.isValidInputSingleTaskOrder(null, s1));
	}

	@Test
	public void isValidInputSingleTaskOrderSpecNotOne() {
		Mockito.doReturn(5).when(s1).getAmountOfOptions();
		assertFalse(of.isValidInputSingleTaskOrder(dt1, s1));
	}
	
	@Test
	public void isValidInputSingleTaskOrderNotContained() {
		Mockito.doReturn(false).when(manMock1).singleTaskCatalogContains(mockOption1);
		Mockito.doReturn(1).when(s1).getAmountOfOptions();
		Mockito.doReturn(mockOption1).when(s1).getOption(0);
		
		assertFalse(of.isValidInputSingleTaskOrder(dt1, s1));
	}
	
	@Test
	public void isValidInputSingleTaskOrderValid() {
		Mockito.doReturn(true).when(manMock1).singleTaskCatalogContains(mockOption1);
		Mockito.doReturn(1).when(s1).getAmountOfOptions();
		Mockito.doReturn(mockOption1).when(s1).getOption(0);
		
		assertTrue(of.isValidInputSingleTaskOrder(dt1, s1));
		
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void makeNewSingleTaskOrderNonValid() {
		exception.expect(IllegalArgumentException.class);
		
		OrderFactory spiedOrderFactory = Mockito.spy(of);
		Mockito.doReturn(false).when(spiedOrderFactory).isValidInputSingleTaskOrder(dt1, s1);
		Mockito.doReturn(dt1).when(spiedOrderFactory).getCurrentTime();
		Mockito.doReturn(m1).when(manMock1).getSingleTaskModel();
		
		spiedOrderFactory.makeNewSingleTaskOrder(dt1, s1);
	}
	
	@Test
	public void makeNewSingleTaskOrderValid() {
		OrderFactory spiedOrderFactory = Mockito.spy(of);
		Mockito.doReturn(true).when(spiedOrderFactory).isValidInputSingleTaskOrder(dt1, s1);
		Mockito.doReturn(dt1).when(spiedOrderFactory).getCurrentTime();
		Mockito.doReturn(m1).when(manMock1).getSingleTaskModel();
		
		SingleTaskOrder o = spiedOrderFactory.makeNewSingleTaskOrder(dt1, s1);
		assertEquals(s1, o.getSpecifications());
		assertEquals(dt1, o.getSubmissionTime());	
		Optional<DateTime> tdl = o.getDeadline();
		assertTrue(tdl.isPresent());
		assertEquals(dt1, tdl.get());
	}
	
	
	//--------------------------------------------------------------------------
	// TimeObserver functions.
	//--------------------------------------------------------------------------
	@Test
	public void testTimeObserverNotSetGet() {
		exception.expect(IllegalStateException.class);
		orderFactory.getCurrentTime();
	}
	
	@Test 
	public void testTimeObserverUpdateNull() {
		exception.expect(IllegalArgumentException.class);
		orderFactory.update(null);
	}
	
	@Test
	public void testTimeObserverUpdateValid() {
		orderFactory.update(dt1);
		
		assertEquals(dt1, orderFactory.getCurrentTime());
	}
	
}
