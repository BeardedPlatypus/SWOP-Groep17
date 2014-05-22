package domain.statistics;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.DateTime;
import domain.statistics.CarsProducedRegistrar;
import domain.statistics.DelayRegistrar;
import domain.statistics.ProcedureStatistics;
import domain.statistics.StatisticsLogger;

public class StatisticsLoggerTest {
	
	StatisticsLogger logger = new StatisticsLogger();
	
	@Mock DelayRegistrar delayRegistrar;
	@Mock CarsProducedRegistrar carsRegistrar;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		logger.addRegistrar(delayRegistrar);
		logger.addRegistrar(carsRegistrar);
		
		Mockito.when(delayRegistrar.isValidDay(1)).thenReturn(true);
		Mockito.when(carsRegistrar.isValidDay(1)).thenReturn(true);
		Mockito.when(delayRegistrar.isValidDay(-1)).thenReturn(false);
		Mockito.when(carsRegistrar.isValidDay(-1)).thenReturn(false);
	}

	@Test
	public void addStatisticsTest() {
		ProcedureStatistics stats = new ProcedureStatistics(100);
		logger.addStatistics(stats);
		Mockito.verify(delayRegistrar).addStatistics(stats);
		Mockito.verify(carsRegistrar).addStatistics(stats);
	}
	
	@Test
	public void getReportTest() {
		logger.getReport();
		Mockito.verify(delayRegistrar).getStatistics();
		Mockito.verify(carsRegistrar).getStatistics();
	}
	
	@Test
	public void updateTimeTest_validDay() {
		logger.update(new DateTime(1, 0, 0));
		Mockito.verify(delayRegistrar).isValidDay(1);
		Mockito.verify(carsRegistrar).isValidDay(1);
		Mockito.verify(delayRegistrar).switchDay(1);
		Mockito.verify(carsRegistrar).switchDay(1);
	}
	
	@Test
	public void updateTimeTest_invalidDay() {
		logger.update(new DateTime(-1, 0, 0));
		Mockito.verify(delayRegistrar, Mockito.never()).switchDay(-1);
		Mockito.verify(carsRegistrar, Mockito.never()).switchDay(-1);
	}

}
