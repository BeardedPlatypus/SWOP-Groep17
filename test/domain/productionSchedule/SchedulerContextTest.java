package domain.productionSchedule;

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

public class SchedulerContextTest {
	//--------------------------------------------------------------------------
	// Test variables. 
	//--------------------------------------------------------------------------
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock DateTime t1;
	@Mock DateTime t2;
	@Mock DateTime t3;
	
	SchedulerContext schedCon;
	//--------------------------------------------------------------------------
	// Setup
	//--------------------------------------------------------------------------
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		schedCon = new SchedulerContext(t1, 0);		
	}

	//--------------------------------------------------------------------------
	// Constructor Test
	//--------------------------------------------------------------------------
	@Test
	public void testConstructorNullInitTime() {
		exception.expect(IllegalArgumentException.class);
		new SchedulerContext(null, 0);
	}
	
	@Test
	public void testConstructorValid() {
		SchedulerContext schedCon = new SchedulerContext(t1, 0);
		
		assertEquals(t1, schedCon.getCurrentTime());
		assertEquals(0, schedCon.getOverTime());
	}

	//--------------------------------------------------------------------------
	// TimeObserver related methods.
	//--------------------------------------------------------------------------
	@Test
	public void testUpdateNull() {
		exception.expect(IllegalArgumentException.class);
		schedCon.update(null);
	}
	
	@Test 
	public void testUpdateNormal() {
		Mockito.when(t1.getDays()).thenReturn(0);
		Mockito.when(t1.getHours()).thenReturn(6);
		Mockito.when(t1.getMinutes()).thenReturn(0);

		Mockito.when(t2.getDays()).thenReturn(0);
		Mockito.when(t2.getHours()).thenReturn(10);
		Mockito.when(t2.getMinutes()).thenReturn(0);

		schedCon.update(t2);
		
		assertEquals(0, schedCon.getOverTime());
		assertEquals(t2, schedCon.getCurrentTime());
	}
	
	@Test
	public void testUpdateNextDayMoreOverTime() {
		Mockito.when(t1.getDays()).thenReturn(0);
		Mockito.when(t1.getHours()).thenReturn(22);
		Mockito.when(t1.getMinutes()).thenReturn(30);

		Mockito.when(t2.getDays()).thenReturn(1);
		Mockito.when(t2.getHours()).thenReturn(6);
		Mockito.when(t2.getMinutes()).thenReturn(0);

		schedCon.update(t2);
		
		assertEquals(30, schedCon.getOverTime());
		assertEquals(t2, schedCon.getCurrentTime());
	}

	@Test
	public void testUpdateNextDayLessOverTime() {
		Mockito.when(t1.getDays()).thenReturn(0);
		Mockito.when(t1.getHours()).thenReturn(22);
		Mockito.when(t1.getMinutes()).thenReturn(0);

		Mockito.when(t2.getDays()).thenReturn(1);
		Mockito.when(t2.getHours()).thenReturn(6);
		Mockito.when(t2.getMinutes()).thenReturn(0);

		schedCon.update(t2);
		
		assertEquals(0, schedCon.getOverTime());
		assertEquals(t2, schedCon.getCurrentTime());
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void testOvertimeNegative() {
		schedCon.setOverTime(-20);
		assertEquals(0, schedCon.getOverTime());
	}
	
	@Test
	public void testOvertimePositive() {
		schedCon.setOverTime(50);
		assertEquals(50, schedCon.getOverTime());
	}
	
	//--------------------------------------------------------------------------
	@Test
	public void testSetTimeNull() {
		exception.expect(IllegalArgumentException.class);
		schedCon.setCurrentTime(null);
	}
	
	@Test
	public void testSetTimeValid() {
		schedCon.setCurrentTime(t2);
		assertEquals(t2, schedCon.getCurrentTime());
	}
}
