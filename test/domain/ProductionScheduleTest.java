package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ProductionScheduleTest {
	//--------------------------------------------------------------------------
	// Test Fissures
	//--------------------------------------------------------------------------
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
		
		this.prodSched = new ProductionSchedule(manMan);
		this.prodSched.setAssemblyLine(assemblyCokeLine);
	}

	//==========================================================================
	// Tests 
	//==========================================================================
	//--------------------------------------------------------------------------
	// Add New Order
	//--------------------------------------------------------------------------
	@Test
	public void test_addNewOrder_addOrder() throws Exception {
		// Setup all the mocked objects
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		Mockito.doReturn(dt0).when(spiedProdSched).getCurrentTime();

		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(3);

		Mockito.doReturn(dt1).when(spiedProdSched).getEstimatedCompletionTime(3);
		Mockito.doReturn(dt2).when(spiedProdSched).getEstimatedCompletionTime(4);
		Mockito.doReturn(dt3).when(spiedProdSched).getEstimatedCompletionTime(5);
		Mockito.doReturn(dt4).when(spiedProdSched).getEstimatedCompletionTime(6);
		
		
		Mockito.when(mockOrder1.getOrderNumber()).thenReturn(0);
		Mockito.when(mockOrder2.getOrderNumber()).thenReturn(1);
		Mockito.when(mockOrder3.getOrderNumber()).thenReturn(2);
		Mockito.when(mockOrder4.getOrderNumber()).thenReturn(3);
		
		Mockito.doReturn(mockOrder1).when(spiedProdSched).makeNewOrder(mockModel1, mockSpec1, 0, dt1);
		Mockito.doReturn(mockOrder2).when(spiedProdSched).makeNewOrder(mockModel2, mockSpec2, 1, dt2);
		Mockito.doReturn(mockOrder3).when(spiedProdSched).makeNewOrder(mockModel3, mockSpec3, 2, dt3);
		Mockito.doReturn(mockOrder4).when(spiedProdSched).makeNewOrder(mockModel4, mockSpec4, 3, dt4);
		
		// Do the actual Code
		spiedProdSched.addNewOrder(mockModel1, mockSpec1);
		spiedProdSched.addNewOrder(mockModel2, mockSpec2);
		spiedProdSched.addNewOrder(mockModel3, mockSpec3);
		spiedProdSched.addNewOrder(mockModel4, mockSpec4);
		
		// Verify Results
		List<OrderContainer> result = spiedProdSched.getPendingOrderContainers();
		assertEquals(4, result.size());
		assertEquals(0, result.get(0).getOrderNumber());
		assertEquals(1, result.get(1).getOrderNumber());
		assertEquals(2, result.get(2).getOrderNumber());
		assertEquals(3, result.get(3).getOrderNumber());
	}

	//--------------------------------------------------------------------------
	// estimatedCompletionTime(int posLine)
	//--------------------------------------------------------------------------
	// FIXME should I completely mock the DateTime objects here?
	@Test
	public void test_getEstimatedCompletionTimePosValidAssemblyLine() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		DateTime time = new DateTime(1, 6, 12);
		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(0);
		assertEquals(1, res1.getDays());
		assertEquals(7, res1.getHours());
		assertEquals(12, res1.getMinutes());
		
		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(1);
		assertEquals(1, res2.getDays());
		assertEquals(8, res2.getHours());
		assertEquals(12, res2.getMinutes());
		
		DateTime res3 = spiedProdSched.getEstimatedCompletionTime(2);
		assertEquals(1, res3.getDays());
		assertEquals(9, res3.getHours());
		assertEquals(12, res3.getMinutes());

		
		DateTime res4 = spiedProdSched.getEstimatedCompletionTime(3);
		assertEquals(1, res4.getDays());
		assertEquals(10, res4.getHours());
		assertEquals(12, res4.getMinutes());
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void test_getEstimatedCompletionTimePosValidPendingToday() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		Mockito.doReturn(0).when(spiedProdSched).getOverTime();
		
		DateTime time = new DateTime(1, 6, 12);
		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		
		DateTime res = spiedProdSched.getEstimatedCompletionTime(6);
		assertEquals(1, res.getDays());
		assertEquals(13, res.getHours());
		assertEquals(12, res.getMinutes());
	}
	
	@Test
	public void test_getEstimatedCompletionTimePosValidPendingDayAfterNoOverTime() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);

		DateTime time = new DateTime(1, 20, 0);
		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(0).when(spiedProdSched).getOverTime();
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(4);
		assertEquals(2, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(11);
		assertEquals(2, res2.getDays());
		assertEquals(17, res2.getHours());
		assertEquals(0, res2.getMinutes());
	}
	
	@Test
	public void test_getEstimatedCompletionTimePosValidPendingDayAfterOverTimeToday() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		
		DateTime time = new DateTime(1, 18, 0);

		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(2 * 60).when(spiedProdSched).getOverTime();
		
		System.out.println("***");
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(4);
		assertEquals(2, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(11);
		assertEquals(2, res2.getDays());
		assertEquals(17, res2.getHours());
		assertEquals(0, res2.getMinutes());
	}

	@Test
	public void test_getEstimatedCompletionTimePosValidPendingDayAfterOverTimeTomorrow() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);

		DateTime time = new DateTime(1, 20, 0);

		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(6 * 60).when(spiedProdSched).getOverTime();

		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(4);
		assertEquals(2, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(13);
		assertEquals(3, res2.getDays());
		assertEquals(10, res2.getHours());
		assertEquals(0, res2.getMinutes());
	}
	
//	@Test
//	public void test_getEstimatedCompletionTimePosValidPendingDayAfterOverTimeMultipleDays() {
//		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
//		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
//		
//		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
//
//		DateTime time = new DateTime(1, 20, 12);
//
//		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
//		Mockito.doReturn(36 * 60).when(spiedProdSched).getOverTime();
//		
//		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(4);
//		assertEquals(4, res1.getDays());
//		assertEquals(10, res1.getHours());
//		assertEquals(0, res1.getMinutes());
//
//		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(13);
//		assertEquals(4, res2.getDays());
//		assertEquals(19, res2.getHours());
//		assertEquals(0, res2.getMinutes());
//
//		DateTime res3 = spiedProdSched.getEstimatedCompletionTime(15);
//		assertEquals(5, res3.getDays());
//		assertEquals(10, res3.getHours());
//		assertEquals(0, res3.getMinutes());
//	}

	//--------------------------------------------------------------------------
	@Test
	public void test_getEstimatedCompletionTimePosInvalid() {
		exception.expect(IllegalArgumentException.class);

		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		prodSched.getEstimatedCompletionTime(-1);
	}
	
	//--------------------------------------------------------------------------
	// estimatedCompletionTime(OrderContainer order)
	//--------------------------------------------------------------------------
	@Test
	public void test_getEstimatedCompletionTimeOrderInvalid() {
		exception.expect(IllegalArgumentException.class);

		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);

		Mockito.doReturn(new ArrayList<OrderContainer>()).when(spiedProdSched).getPendingOrderContainers();
		
		Mockito.when(assemblyCokeLine.getOrderPositionOnAssemblyLine(mockOrder1)).thenThrow(new IllegalArgumentException());
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(3);
		
		spiedProdSched.getEstimatedCompletionTime(mockOrder1);
	}

	//--------------------------------------------------------------------------
	@Test
	public void test_getEstimatedCompletionTimeOrderValidAssemblyLine() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		Mockito.when(assemblyCokeLine.getOrderPositionOnAssemblyLine(mockOrder1)).thenReturn(3);
		Mockito.when(assemblyCokeLine.getOrderPositionOnAssemblyLine(mockOrder2)).thenReturn(2);
		Mockito.when(assemblyCokeLine.getOrderPositionOnAssemblyLine(mockOrder3)).thenReturn(1);
		Mockito.when(assemblyCokeLine.getOrderPositionOnAssemblyLine(mockOrder4)).thenReturn(0);
		
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		DateTime time = new DateTime(1, 6, 12);
		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(mockOrder1);
		assertEquals(1, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(12, res1.getMinutes());
		
		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(mockOrder2);
		assertEquals(1, res2.getDays());
		assertEquals(9, res2.getHours());
		assertEquals(12, res2.getMinutes());
		
		DateTime res3 = spiedProdSched.getEstimatedCompletionTime(mockOrder3);
		assertEquals(1, res3.getDays());
		assertEquals(8, res3.getHours());
		assertEquals(12, res3.getMinutes());

		
		DateTime res4 = spiedProdSched.getEstimatedCompletionTime(mockOrder4);
		assertEquals(1, res4.getDays());
		assertEquals(7, res4.getHours());
		assertEquals(12, res4.getMinutes());
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void test_getEstimatedCompletionTimeOrderValidPendingToday() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		Mockito.doReturn(0).when(spiedProdSched).getOverTime();

		List<OrderContainer> resAss = new ArrayList<OrderContainer>();
		resAss.add(mockOrder1);
		resAss.add(mockOrder2);
		resAss.add(mockOrder3);
		
		Mockito.doReturn(resAss).when(spiedProdSched).getPendingOrderContainers();

		
		DateTime time = new DateTime(1, 6, 12);
		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		
		DateTime res = spiedProdSched.getEstimatedCompletionTime(mockOrder3);
		assertEquals(1, res.getDays());
		assertEquals(13, res.getHours());
		assertEquals(12, res.getMinutes());
	}
	
	@Test
	public void test_getEstimatedCompletionTimeOrderValidPendingDayAfterNoOverTime() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);

		DateTime time = new DateTime(1, 16, 0);
		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(0).when(spiedProdSched).getOverTime();

		List<OrderContainer> resAss = new ArrayList<OrderContainer>();
		resAss.add(mockOrder1);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(mockOrder2);

		Mockito.doReturn(resAss).when(spiedProdSched).getPendingOrderContainers();
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(mockOrder1);
		assertEquals(1, res1.getDays());
		assertEquals(21, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(mockOrder2);
		assertEquals(2, res2.getDays());
		assertEquals(14, res2.getHours());
		assertEquals(0, res2.getMinutes());
	}
	
	@Test
	public void test_getEstimatedCompletionTimeOrderValidPendingDayAfterOverTimeToday() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		
		DateTime time = new DateTime(1, 18, 12);

		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(2 * 60).when(spiedProdSched).getOverTime();

		List<OrderContainer> resAss = new ArrayList<OrderContainer>();
		resAss.add(mockOrder1);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(mockOrder2);

		Mockito.doReturn(resAss).when(spiedProdSched).getPendingOrderContainers();
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(mockOrder1);
		assertEquals(2, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(mockOrder2);
		assertEquals(2, res2.getDays());
		assertEquals(17, res2.getHours());
		assertEquals(0, res2.getMinutes());
	}

	@Test
	public void test_getEstimatedCompletionTimeOrderValidPendingDayAfterOverTimeTomorrow() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);

		DateTime time = new DateTime(1, 20, 0);

		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(6 * 60).when(spiedProdSched).getOverTime();

		List<OrderContainer> resAss = new ArrayList<OrderContainer>();
		resAss.add(mockOrder1);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(mockOrder2);

		Mockito.doReturn(resAss).when(spiedProdSched).getPendingOrderContainers();
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(mockOrder1);
		assertEquals(2, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(mockOrder2);
		assertEquals(3, res2.getDays());
		assertEquals(10, res2.getHours());
		assertEquals(0, res2.getMinutes());
	}
	
	@Test
	public void test_getEstimatedCompletionTimeOrderValidPendingDayAfterOverTimeMultipleDays() {
		// ASSUMES THAT HOURS TO MOVE A WORKSTATION IS ONE.
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(4);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);

		DateTime time = new DateTime(1, 20, 0);

		Mockito.doReturn(time).when(spiedProdSched).getCurrentTime();
		Mockito.doReturn(8 * 60).when(spiedProdSched).getOverTime();

		List<OrderContainer> resAss = new ArrayList<OrderContainer>();
		resAss.add(mockOrder1);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(null);
		resAss.add(mockOrder2);
		resAss.add(null);
		resAss.add(mockOrder3);

		Mockito.doReturn(resAss).when(spiedProdSched).getPendingOrderContainers();		
		
		DateTime res1 = spiedProdSched.getEstimatedCompletionTime(mockOrder1);
		assertEquals(2, res1.getDays());
		assertEquals(10, res1.getHours());
		assertEquals(0, res1.getMinutes());

		DateTime res2 = spiedProdSched.getEstimatedCompletionTime(mockOrder2);
		assertEquals(3, res2.getDays());
		assertEquals(12, res2.getHours());
		assertEquals(0, res2.getMinutes());

		DateTime res3 = spiedProdSched.getEstimatedCompletionTime(mockOrder3);
		assertEquals(3, res3.getDays());
		assertEquals(14, res3.getHours());
		assertEquals(0, res3.getMinutes());
	}
	
	//--------------------------------------------------------------------------
	// endDay
	//--------------------------------------------------------------------------
	@Test
	public void test_endDayIllegalArgumentException() {
		exception.expect(IllegalArgumentException.class);
		
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		Mockito.doReturn(0).when(spiedProdSched).getOverTime();

		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(3);
		
		Mockito.when(dt1.getDays()).thenReturn(0);
		Mockito.when(dt1.getHours()).thenReturn(0);
		Mockito.when(dt1.getMinutes()).thenReturn(0);
		
		spiedProdSched.endDay(dt1);
	}

	@Test
	public void test_endDayValidNoOverTime() throws Exception {
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		
		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(3);		
		Mockito.when(dt1.getDays()).thenReturn(0);
		Mockito.when(dt1.getHours()).thenReturn(20);
		Mockito.when(dt1.getMinutes()).thenReturn(51);

		Mockito.doReturn(dt2).when(spiedProdSched).makeNewDateTime(1, 6, 0);
		
		spiedProdSched.endDay(dt1);

		Mockito.verify(spiedProdSched).makeNewDateTime(1, 6, 0);
		
		assertEquals(dt2, spiedProdSched.getCurrentTime());
		assertEquals(0, spiedProdSched.getOverTime());		
	}
	
	@Test
	public void test_endDayValidOverTimeDecrease() throws Exception {
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		spiedProdSched.setOverTime(6*60);

		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(3);
		Mockito.when(dt1.getDays()).thenReturn(0);
		Mockito.when(dt1.getHours()).thenReturn(18);
		Mockito.when(dt1.getMinutes()).thenReturn(0);

		Mockito.doReturn(dt2).when(spiedProdSched).makeNewDateTime(1, 6, 0);
		
		spiedProdSched.endDay(dt1);

		Mockito.verify(spiedProdSched).makeNewDateTime(1, 6, 0);
		
		assertEquals(dt2, spiedProdSched.getCurrentTime());
		assertEquals(2 * 60, spiedProdSched.getOverTime());		
	}
	
	@Test
	public void test_endDayValidOverTimeIncrease() throws Exception {
		ProductionSchedule spiedProdSched = Mockito.spy(prodSched);
		spiedProdSched.setOverTime(0);

		Mockito.when(assemblyCokeLine.getAmountOfWorkPosts()).thenReturn(3);
		Mockito.when(dt1.getDays()).thenReturn(0);
		Mockito.when(dt1.getHours()).thenReturn(23);
		Mockito.when(dt1.getMinutes()).thenReturn(0);

		Mockito.doReturn(dt2).when(spiedProdSched).makeNewDateTime(1, 6, 0);
		
		spiedProdSched.endDay(dt1);

		Mockito.verify(spiedProdSched).makeNewDateTime(1, 6, 0);

		assertEquals(dt2, spiedProdSched.getCurrentTime());
		assertEquals(1 * 60, spiedProdSched.getOverTime());		
	}
	//--------------------------------------------------------------------------
	// removeNextOrder
	//--------------------------------------------------------------------------
	@Test
	public void test_removeNextOrderToScheduleIndexOutOfBoundsException() {
		exception.expect(IndexOutOfBoundsException.class);
		
		prodSched.removeNextOrderToSchedule();
	}
}
