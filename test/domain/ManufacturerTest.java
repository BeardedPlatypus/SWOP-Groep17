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

public class ManufacturerTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock ProductionSchedule mockProdSched;
	@Mock OrderContainer orderContainer;
	@Mock ModelCatalog mockModCat;
	@Mock Model mockMod;
	@Mock Specification mockSpec;
	@Mock Order mockOrder;
	

	Manufacturer man;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		this.man = new Manufacturer(mockModCat);
		man.setProductionSchedule(mockProdSched);

	}

	@Test
	public void test_getPendingOrders() {
		List<OrderContainer> array = new ArrayList<OrderContainer>();
		array.add(this.orderContainer);
		Mockito.when(this.mockProdSched.getPendingOrderContainers()).thenReturn(array);
		
		assertEquals(array, this.man.getPendingOrderContainers());
		Mockito.verify(this.mockProdSched).getPendingOrderContainers();
	}
	
//	@Test
//	public void constructorNormalTest() {
//		new Manufacturer(mockProdSched, mockModCat);
//		// TODO Te triviale test? Hoe maken we die avontuurlijker?
//	}
	
//	@Test
//	public void constructorNullScheduleTest() {
//		exception.expect(IllegalArgumentException.class);
//		new Manufacturer(null, mockModCat);
//	}
	
	@Test
	public void constructorNullModelCatalogTest() {
		exception.expect(IllegalArgumentException.class);
		new Manufacturer(null);
	}

	@Test
	public void testGetCompletedOrderContainers() {
		Mockito.when(mockOrder.isCompleted()).thenReturn(true);
		List<OrderContainer> complOrders = man.getCompletedOrderContainers();
		assertTrue(complOrders.size() == 0);
		
		man.addCompleteOrder(mockOrder);
		
		complOrders = man.getCompletedOrderContainers();
		
		assertTrue(complOrders.contains(mockOrder));
		assertTrue(complOrders.size() == 1);
	}

	@Test
	public void testGetPendingOrderContainers() {
		List<OrderContainer> pendOrders = new ArrayList<>();
		pendOrders.add(orderContainer);
		Mockito.when(mockProdSched.getPendingOrderContainers()).thenReturn(pendOrders);
		
		assertTrue(man.getPendingOrderContainers().equals(pendOrders));
	}

	@Test
	public void testGetModels() {
		List<Model> models = new ArrayList<>();
		Mockito.when(mockModCat.getModels()).thenReturn(models);
		
		assertTrue(man.getModels().equals(models));
	}

	@Test
	public void testCreateOrder() {
		man.createOrder(mockMod, mockSpec);
	}
	
	@Test
	public void testCreateOrderNullModel() {
		exception.expect(IllegalArgumentException.class);
		man.createOrder(null, mockSpec);
	}
	
	@Test
	public void testCreateOrderNullSpecs() {
		exception.expect(IllegalArgumentException.class);
		man.createOrder(mockMod, null);
	}

	@Test
	public void testAddCompleteOrder() {
		Mockito.when(mockOrder.isCompleted()).thenReturn(true);
		List<OrderContainer> complOrders = man.getCompletedOrderContainers();
		assertTrue(complOrders.size() == 0);
		
		man.addCompleteOrder(mockOrder);
		
		complOrders = man.getCompletedOrderContainers();
		
		assertTrue(complOrders.size() == 1);
	}
	
	@Test
	public void testAddCompleteOrderNotCompleteOrder() {
		exception.expect(IllegalStateException.class);
		Mockito.when(mockOrder.isCompleted()).thenReturn(false);
		man.addCompleteOrder(mockOrder);
	}

}
