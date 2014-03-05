package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ProductionScheduleTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock AssemblyLine assemblyCokeLine;
	@Mock Manufacturer manMan;

	@Mock Order mockOrder1;
	@Mock Order mockOrder2;
	@Mock Order mockOrder3;
	

	@Mock Specifications mockSpecs;
	@Mock Model mockModel;

	ProductionSchedule prodSched;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		this.prodSched = new ProductionSchedule(manMan, assemblyCokeLine);
	}

	@Test
	public void test_addNewOrder_ {

	}
	
	@Test
	public void test_addNewOrder_nullpointer2() {
		exception.expect(NullPointerException.class);
		this.prodSched.addNewOrder(mockModel, null);
	}
	
	@Test
	public void test_addNewOrder_IllegalArgument1() {
		exception.expect(IllegalArgumentException.class);
				
		Mockito.when(this.mockModel.isValidSpecification(mockSpecs)).thenReturn(false);
		this.prodSched.addNewOrder(mockModel, mockSpecs);
		
		Mockito.verify(this.mockModel, Mockito.times(1));
	}
	
	@Test
	public void test_getPendingOrderContainers() {
		List<OrderContainer> array = new ArrayList<OrderContainer>();
		array.add(this.mockOrder1);
		Mockito.when(this.mockProdSched.getPendingOrderContainers()).thenReturn(array);

	}

}
