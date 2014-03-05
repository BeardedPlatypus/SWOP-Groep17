package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ManufacturerTest {
	@Mock ProductionSchedule mockProdSched;
	@Mock OrderContainer orderContainer;
	@Mock ModelCatalog mockMod;

	Manufacturer man;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		this.man = new Manufacturer(mockProdSched, mockMod);

	}

	@Test
	public void test_getPendingOrders() {
		List<OrderContainer> array = new ArrayList<OrderContainer>();
		array.add(this.orderContainer);
		Mockito.when(this.mockProdSched.getPendingOrderContainers()).thenReturn(array);
		
		assertEquals(array, this.man.getPendingOrderContainers());
		Mockito.verify(this.mockProdSched, Mockito.times(1));
	}

}
