package scenario;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.*;

import domain.DateTime;
import domain.Manufacturer;
import domain.assemblyLine.AssemblyLine;
import domain.handlers.CheckProductionStatisticsHandler;
import domain.statistics.CarsProducedRegistrar;
import domain.statistics.DelayRegistrar;
import domain.statistics.ProcedureStatistics;
import domain.statistics.StatisticsLogger;

@RunWith(PowerMockRunner.class)
public class CheckProductionStatisticsScenario {
	
	StatisticsLogger logger;
	CarsProducedRegistrar carsRegistrar;
	DelayRegistrar delayRegistrar;
	
	CheckProductionStatisticsHandler handler;
	AssemblyLine assemblyLine;
	
	@Mock Manufacturer manufacturer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		assemblyLine = new AssemblyLine(manufacturer);
		PowerMockito.doReturn(assemblyLine).when(manufacturer, "getAssemblyLine");
		
		logger = new StatisticsLogger();
		carsRegistrar = new CarsProducedRegistrar();
		delayRegistrar = new DelayRegistrar();
		logger.addRegistrar(carsRegistrar);
		logger.addRegistrar(delayRegistrar);
		
		assemblyLine.setStatisticsLogger(logger);
		
		ProcedureStatistics stats;
		for (int i = 0; i < 50; i++) {
			stats = new ProcedureStatistics(20);
			logger.addStatistics(stats);
		}
		logger.update(new DateTime(1, 6, 0));
		for (int i = 0; i < 40; i++) {
			stats = new ProcedureStatistics(30);
			logger.addStatistics(stats);
		}
		logger.update(new DateTime(2, 6, 0));
	}

	@Test
	public void normalFlow() {
		// Step 1: The user wants to check statistics about the production
		// --- Responsibility of the UI ---
		// Step 2: The system shows a set of available statistics
		String stats = handler.getStatisticsReport();
		assertTrue(stats.contains("Average: 45"));
		assertTrue(stats.contains("Median: 45"));
		assertTrue(stats.contains("50 cars produced on day 0"));
		assertTrue(stats.contains("40 cars produced on day 1"));
		
		assertTrue(stats.contains("Average: 24.444444444444443"));
		assertTrue(stats.contains("Median: 20"));
		assertTrue(stats.contains("Delay of 30 minutes on day 1"));
		assertTrue(stats.contains("Delay of 30 minutes on day 1"));
		// Step 3: The user indicates he is done viewing statistics
		// --- Responsibility of the UI ---
	}

}
