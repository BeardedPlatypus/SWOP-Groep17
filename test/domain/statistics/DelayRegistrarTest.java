package domain.statistics;

import static org.junit.Assert.*;

import java.util.List;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

import domain.statistics.DelayRegistrar;
import domain.statistics.ProcedureStatistics;
import domain.statistics.WorkingDay;

public class DelayRegistrarTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	DelayRegistrar registrar;
	double epsilon = 1E-14;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		registrar = new DelayRegistrar();
		
		ProcedureStatistics stats = new ProcedureStatistics(10);
		registrar.addStatistics(stats);
		
		stats = new ProcedureStatistics(20);
		registrar.addStatistics(stats);
		
		stats = new ProcedureStatistics(50);
		registrar.addStatistics(stats);
		
		stats = new ProcedureStatistics(30);
		registrar.addStatistics(stats);
		
		stats = new ProcedureStatistics(40);
		registrar.addStatistics(stats);
		
		stats = new ProcedureStatistics(10);
		registrar.addStatistics(stats);
	}
	
	public void addStatisticsTest() {
		ProcedureStatistics stats = new ProcedureStatistics(1000);
		int dayNumber = registrar.getActiveDay().getDayNumber();
		registrar.addStatistics(stats);
		
		// Using whitebox, because these methods are protected.
		List<Pair<Integer, WorkingDay>> delays = null;
		try {
			delays = Whitebox.invokeMethod(registrar, "getDelays");
		} catch (Exception e) {
			// Whitebox reserves the right to re-throw an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
		Pair<Integer, WorkingDay> lastDelay = delays.get(delays.size() - 1);
		assertTrue(1000 == lastDelay.getValue0());
		assertEquals(dayNumber, lastDelay.getValue1().getDayNumber());
		assertEquals(dayNumber, lastDelay.getValue1().getDayNumber());
	}

	@Test
	public void switchDayTest() {
		registrar.switchDay(1);
		assertEquals(1, registrar.getActiveDay().getDayNumber());
	}
	
	@Test
	public void switchDayTest_equalDayNumber() {
		registrar.switchDay(0);
		assertEquals(0, registrar.getActiveDay().getDayNumber());
	}
	
	@Test
	public void switchDayTest_smallerDayNumber() {
		registrar.switchDay(1);
		exception.expect(IllegalArgumentException.class);
		registrar.switchDay(0);
	}
	
	@Test
	public void getAverageTest() {
		// Using whitebox, because these methods are protected.
		try {
			double average = Whitebox.<Double> invokeMethod(registrar,"getAverage");
			assertEquals(80d/3d, average, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to rethrow an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAverageTest_noStats() {
		DelayRegistrar registrar = new DelayRegistrar();
		// Using whitebox, because these methods are protected.
		try {
			double average = Whitebox.<Double> invokeMethod(registrar,"getAverage");
			assertEquals(0, average, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to rethrow an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void getMedianTest() {
		// Using whitebox, because these methods are protected.
		try {
			double median = Whitebox.<Double> invokeMethod(registrar,"getMedian");
			assertEquals(25, median, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to rethrow an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void getMedianTest_noStats() {
		DelayRegistrar registrar = new DelayRegistrar();
		// Using whitebox, because these methods are protected.
		try {
			double median = Whitebox.<Double> invokeMethod(registrar,"getMedian");
			assertEquals(0, median, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to rethrow an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void getMedianTest_oneStat() {
		DelayRegistrar registrar = new DelayRegistrar();
		registrar.addStatistics(new ProcedureStatistics(100));
		// Using whitebox, because these methods are protected.
		try {
			double median = Whitebox.<Double> invokeMethod(registrar,"getMedian");
			assertEquals(100, median, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to rethrow an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void getStatsTest() {
		System.out.println(registrar.getStatistics());
	}

}
