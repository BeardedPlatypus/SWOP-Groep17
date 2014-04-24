package domain;

import static org.junit.Assert.*;

import java.util.List;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import domain.statistics.CarsProducedRegistrar;
import domain.statistics.ProcedureStatistics;
import domain.statistics.WorkingDay;

public class CarsProducedRegistrarTest {
	
	CarsProducedRegistrar registrar;
	double epsilon = 1E-14;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		registrar = new CarsProducedRegistrar();
		
		this.initialiseStatistics();
	}

	@Test
	public void addStatisticsTest() {
		ProcedureStatistics stats = new ProcedureStatistics(100);
		registrar.addStatistics(stats);
		// Using whitebox, because these methods are protected.
		try {
			int currentDayAmount = Whitebox.<Integer> invokeMethod(registrar,"getCurrentDayAmount");
			assertEquals(1, currentDayAmount);
		} catch (Exception e) {
			// Whitebox reserves the right to rethrow an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getAverageTest() {
		// Using whitebox, because these methods are protected.
		try {
			double average = Whitebox.<Double> invokeMethod(registrar,"getAverage");
			assertEquals(42, average, epsilon);
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
			assertEquals(40, median, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to re-throw an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
		
		registrar.addStatistics(new ProcedureStatistics(100));
		registrar.switchDay(registrar.getActiveDay().getDayNumber() + 1);
		
		// Using whitebox, because these methods are protected.
		try {
			double median = Whitebox.<Double> invokeMethod(registrar,"getMedian");
			assertEquals(35, median, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to re-throw an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void isValidDayTest_invalid() {
		assertFalse(registrar.isValidDay(4));
	}
	
	@Test
	public void isValidDayTest_valid() {
		assertTrue(registrar.isValidDay(5));
		assertTrue(registrar.isValidDay(6));
	}
	
	@Test
	public void switchDayTest_multipleDays() {
		registrar.addStatistics(new ProcedureStatistics(100));
		int newDayNumber = registrar.getActiveDay().getDayNumber() + 3;
		registrar.switchDay(newDayNumber);
		
		// Using whitebox, because these methods are protected.
		try {
			double average = Whitebox.<Double> invokeMethod(registrar,"getAverage");
			assertEquals(211d/8d, average, epsilon);
			
			double median = Whitebox.<Double> invokeMethod(registrar,"getMedian");
			assertEquals(20, median, epsilon);
			
			List<Pair<Integer, WorkingDay>> carsProducedNumbers = Whitebox.invokeMethod(registrar, "getCarsProducedNumbers");
			Pair<Integer, WorkingDay> secondToLast = carsProducedNumbers.get(carsProducedNumbers.size() - 2);
			Pair<Integer, WorkingDay> last = carsProducedNumbers.get(carsProducedNumbers.size() - 1);
			
			assertTrue(0 == secondToLast.getValue0());
			assertTrue(0 == last.getValue0());
		} catch (Exception e) {
			// Whitebox reserves the right to re-throw an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void finishUpActiveDayTest() {
		registrar.addStatistics(new ProcedureStatistics(8008135));
		
		// Using whitebox, because these methods are protected.
		try {
			Whitebox.invokeMethod(registrar, "finishUpActiveDay", 6);
		
			double average = Whitebox.<Double> invokeMethod(registrar,"getAverage");
			assertEquals(211d/6d, average, epsilon);
			
			double median = Whitebox.<Double> invokeMethod(registrar,"getMedian");
			assertEquals(35, median, epsilon);
		} catch (Exception e) {
			// Whitebox reserves the right to re-throw an exception. This method doesn't have the ability to throw
			// an exception, so this catch should never be reached.
			e.printStackTrace();
		}
	}
	
	@Test
	public void getStatisticsTest() {
		System.out.println(registrar.getStatistics());
	}
	
	public void initialiseStatistics() {
		ProcedureStatistics stats = new ProcedureStatistics(1337);
		
		for (int i = 0; i < 50; i++) {
			registrar.addStatistics(stats);
		}
		
		registrar.switchDay(1);
		
		for (int i = 0; i < 80; i++) {
			registrar.addStatistics(stats);
		}
		
		registrar.switchDay(2);
		
		for (int i = 0; i < 10; i++) {
			registrar.addStatistics(stats);
		}
		
		registrar.switchDay(3);
		
		for (int i = 0; i < 30; i++) {
			registrar.addStatistics(stats);
		}
		
		registrar.switchDay(4);
		
		for (int i = 0; i < 40; i++) {
			registrar.addStatistics(stats);
		}
		
		registrar.switchDay(5);
	}

}
