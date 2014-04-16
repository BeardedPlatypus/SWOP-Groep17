package domain;

import static org.junit.Assert.*;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		assertEquals(1, registrar.getCurrentDayAmount());
	}
	
	@Test
	public void getAverageTest() {
		double average = registrar.getAverage();
		assertEquals(42, average, epsilon);
	}
	
	@Test
	public void getMedianTest() {
		double median = registrar.getMedian();
		assertEquals(40, median, epsilon);
		
		registrar.addStatistics(new ProcedureStatistics(100));
		registrar.switchDay(registrar.getActiveDay().getDayNumber() + 1);
		median = registrar.getMedian();
		assertEquals(35, median, epsilon);
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
		assertEquals(211d/8d, registrar.getAverage(), epsilon);
		assertEquals(20, registrar.getMedian(), epsilon);
		Pair<Integer, WorkingDay> secondToLast = registrar.getCarsProducedNumbers()
				.get(registrar.getCarsProducedNumbers().size() - 2);
		Pair<Integer, WorkingDay> last = registrar.getCarsProducedNumbers()
				.get(registrar.getCarsProducedNumbers().size() - 1);
		assertTrue(0 == secondToLast.getValue0());
		assertTrue(0 == last.getValue0());
	}
	
	@Test
	public void finishUpActiveDayTest() {
		registrar.addStatistics(new ProcedureStatistics(8008135));
		registrar.finishUpActiveDay(6);
		
		double average = registrar.getAverage();
		assertEquals(211d/6d, average, epsilon);
		
		double median = registrar.getMedian();
		assertEquals(35, median, epsilon);
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
