package domain;

import static org.junit.Assert.*;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
		Pair<Integer, WorkingDay> lastDelay = registrar.getDelays().get(registrar.getDelays().size());
		assertTrue(1000 == lastDelay.getValue0());
		assertEquals(dayNumber, registrar.getDelays().get(registrar.getDelays().size() - 1).getValue1().getDayNumber());
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
		double average = registrar.getAverage();
		assertEquals(80d/3d, average, epsilon);
	}
	
	@Test
	public void getAverageTest_noStats() {
		DelayRegistrar registrar = new DelayRegistrar();
		assertEquals(0, registrar.getAverage(), epsilon);
	}
	
	@Test
	public void getMedianTest() {
		double median = registrar.getMedian();
		assertEquals(25, median, epsilon);
	}
	
	@Test
	public void getMedianTest_noStats() {
		DelayRegistrar registrar = new DelayRegistrar();
		assertEquals(0, registrar.getMedian(), epsilon);
	}
	
	@Test
	public void getMedianTest_oneStat() {
		DelayRegistrar registrar = new DelayRegistrar();
		registrar.addStatistics(new ProcedureStatistics(100));
		assertEquals(100, registrar.getMedian(), epsilon);
	}
	
	@Test
	public void getStatsTest() {
		System.out.println(registrar.getStatistics());
	}

}
