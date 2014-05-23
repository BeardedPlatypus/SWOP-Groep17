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
import org.powermock.reflect.Whitebox;

import domain.DateTime;
import domain.Manufacturer;
import domain.assembly_line.AssemblyLine;
import domain.handlers.CheckProductionStatisticsHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
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
	InitialisationHandler init;
	DomainFacade facade;
	Manufacturer manufacturer; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		init = new InitialisationHandler();
		init.setupIteration3();
		facade = init.getDomainFacade();
		handler = facade.getCheckProductionStatisticsHandler();
		manufacturer = Whitebox.getInternalState(handler, Manufacturer.class);
	}

	@Test
	public void normalFlow() {
		// Step 1: The user wants to check statistics about the production
		// --- Responsibility of the UI ---
		// Step 2: The system shows a set of available statistics
		String stats = handler.getStatisticsReport();
		System.out.println(stats);
		assertTrue(stats.contains("Average: 10"));
		assertTrue(stats.contains("Median: 10"));
		assertTrue(stats.contains("10 cars produced on day 0"));
		// Step 3: The user indicates he is done viewing statistics
		// --- Responsibility of the UI ---
	}

}
