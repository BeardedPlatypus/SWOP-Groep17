package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class IntermediateStuff {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	

	//--------------------------------------------------------------------------
	// TimeObserver related methods.
	//--------------------------------------------------------------------------

	
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
