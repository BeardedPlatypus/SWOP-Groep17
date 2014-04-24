package scenario;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.InteractionSimulator;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

/**
 * Use case scenario test: Check Production Statistics
 * 
 * @author Simon Slangen
 *
 */
public class CheckProductionStatisticsScenario_Alternate {
	
	DomainFacade facade;
	InteractionSimulator sim;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
		
		sim = new InteractionSimulator(facade);
		
	}
	
	@Test
	public void emptyAssemblyLine_test() {
		String report = facade.getStatisticsReport();
		assertTrue(report.length() > 0);
	}

	@Test
	public void normalFlowInitial_test() {
		//Place 5 orders.
		sim.simulatePlaceOrder(5);
		//Complete 2 assembly line iterations, while spending 50 minutes per task.
		sim.simulateCompleteAllTasksOnAssemblyLine(2, 50);
		//Complete 2 assembly line iterations, while spending 60 minutes per task.
		sim.simulateCompleteAllTasksOnAssemblyLine(2, 60);
		//Place 2 more orders.
		sim.simulatePlaceOrder(2);
		//Complete 3 assembly line iterations, while spending 60 minutes per task.
		sim.simulateCompleteAllTasksOnAssemblyLine(2, 70);
		
		
		//1. The user wants to check statistics about the production. ==HAPPENS IN UI==
		//2. The system shows a set of available statistics.
		//3. The user indicates he is done viewing statistics.
		String report = facade.getStatisticsReport();
		assertTrue(report.length() > 0);
	}

}
