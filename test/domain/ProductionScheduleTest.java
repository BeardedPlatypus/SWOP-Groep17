package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
	public void test_addNewOrder_nullpointer1() {
		exception.expect(NullPointerException.class);
		
		
	}
	
	@Test
	public void test_getPendingOrderContainers() {
		List<OrderContainer> array = new ArrayList<OrderContainer>();
		array.add(this.orderContainer);
		Mockito.when(this.mockProdSched.getPendingOrderContainers()).thenReturn(array);

	}

}
