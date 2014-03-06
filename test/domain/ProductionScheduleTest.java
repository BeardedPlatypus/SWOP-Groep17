package domain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ProductionScheduleTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock AssemblyLine assemblyCokeLine;
	@Mock Manufacturer manMan;

	@Mock Order mockOrder1;
	@Mock Order mockOrder2;
	@Mock Order mockOrder3;
	@Mock Order mockOrder4;
	
	@Mock Model mockModel1;
	@Mock Model mockModel2;
	@Mock Model mockModel3;
	@Mock Model mockModel4;
	
	@Mock Specification mockSpec1;
	@Mock Specification mockSpec2;
	@Mock Specification mockSpec3;
	@Mock Specification mockSpec4;

	@Mock Specification mockSpecs;
	@Mock Model mockModel;

	@Mock DateTime dt0;
	@Mock DateTime dt1;
	@Mock DateTime dt2;
	@Mock DateTime dt3;
	@Mock DateTime dt4;
	
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
	public void test_addNewOrder_addOrder() throws Exception {
		// Setup all the mocked objects
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		Mockito.when(spiedProdSched.getCurrentTime()).thenReturn(dt0);

		Mockito.when(assemblyCokeLine.getSize()).thenReturn(3);
		
		Mockito.when(spiedProdSched.getEstimatedCompletionTime(3)).thenReturn(dt1);
		Mockito.when(spiedProdSched.getEstimatedCompletionTime(4)).thenReturn(dt2);
		Mockito.when(spiedProdSched.getEstimatedCompletionTime(5)).thenReturn(dt3);
		Mockito.when(spiedProdSched.getEstimatedCompletionTime(6)).thenReturn(dt4);

		Mockito.when(mockOrder1.getOrderNumber()).thenReturn(0);
		Mockito.when(mockOrder2.getOrderNumber()).thenReturn(1);
		Mockito.when(mockOrder3.getOrderNumber()).thenReturn(2);
		Mockito.when(mockOrder4.getOrderNumber()).thenReturn(3);
		
		Mockito.doReturn(mockOrder1).when(spiedProdSched).makeNewOrder(mockModel1, mockSpec1, 0, dt0, dt1);
		Mockito.doReturn(mockOrder2).when(spiedProdSched).makeNewOrder(mockModel2, mockSpec2, 1, dt0, dt2);
		Mockito.doReturn(mockOrder3).when(spiedProdSched).makeNewOrder(mockModel3, mockSpec3, 2, dt0, dt3);
		Mockito.doReturn(mockOrder4).when(spiedProdSched).makeNewOrder(mockModel4, mockSpec4, 3, dt0, dt4);
		
		// Do the actual Code
		spiedProdSched.addNewOrder(mockModel1, mockSpec1);
		spiedProdSched.addNewOrder(mockModel2, mockSpec2);
		spiedProdSched.addNewOrder(mockModel3, mockSpec3);
		spiedProdSched.addNewOrder(mockModel4, mockSpec4);
		
		// Verify code calls
		Mockito.verify(spiedProdSched, Mockito.times(4)).getCurrentTime();
		// Verify Results
		List<OrderContainer> result = spiedProdSched.getPendingOrderContainers();
		assertEquals(4, result.size());
		assertEquals(0, result.get(0).getOrderNumber());
		assertEquals(1, result.get(1).getOrderNumber());
		assertEquals(2, result.get(2).getOrderNumber());
		assertEquals(3, result.get(3).getOrderNumber());
	}
		
	@Test
	public void test_getEstimatedCompletionTimeOrderIncomplete() {
		fail();
	}

	@Test
	public void test_getEstimatedCompletionTimeOrderComplete() {
		Mockito.when(mockOrder1.isCompleted()).thenReturn(true);
		Mockito.when(mockOrder1.getEstimatedCompletionTime()).thenReturn(dt0);
		
		DateTime test = prodSched.getEstimatedCompletionTime(mockOrder1);
		assertEquals(dt0, test);
		
		Mockito.verify(mockOrder1).isCompleted();
		Mockito.verify(mockOrder1).getEstimatedCompletionTime();
	}

	@Test
	public void test_getEstimatedCompletionTimeOrderInvalid() {
		exception.expect(IllegalArgumentException.class);
		Mockito.when(mockOrder1.isCompleted()).thenReturn(false);
		
		prodSched.getEstimatedCompletionTime(mockOrder1);
	}

	@Test
	public void test_getEstimatedCompletionTimePosValid() {
		fail();
	}
	
	@Test
	public void test_getEstimatedCompletionTimePosInvalid() {
		exception.expect(IllegalArgumentException.class);

		prodSched.getEstimatedCompletionTime(-1);
	}
	
	@Test
	public void test_removeNextOrderToScheduleIndexOutOfBoundsException() {
		exception.expect(IndexOutOfBoundsException.class);
		
		prodSched.removeNextOrderToSchedule();
	}
	
//	@Test
//	public void test_removeNextOrderToScheduleValid() {
//		// TODO find way to remove dependency on addOrderMethod. 
//		// Setup all the mocked objects
//		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
//		Mockito.when(spiedProdSched.getCurrentTime()).then;
//
//	}
	
//	@Test
//	public void test_getPendingOrderContainers() {
//		List<OrderContainer> array = new ArrayList<OrderContainer>();
//		array.add(this.mockOrder1);
//		Mockito.when(this.mockProdSched.getPendingOrderContainers()).thenReturn(array);
//
//	}
}
