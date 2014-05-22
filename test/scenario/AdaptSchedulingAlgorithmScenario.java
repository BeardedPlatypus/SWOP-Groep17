package scenario;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domain.Manufacturer;
import domain.order.StandardOrder;
import domain.production_schedule.strategy.AlgorithmStrategyFactory;
import domain.production_schedule.strategy.BatchStrategy;
import domain.production_schedule.strategy.SchedulingStrategyView;
import domain.car.Option;
import domain.car.Specification;
import domain.handlers.AdaptSchedulingAlgorithmHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

public class AdaptSchedulingAlgorithmScenario extends TestCase {
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	AdaptSchedulingAlgorithmHandler handler;
	DomainFacade facade;
	AlgorithmStrategyFactory algFac;
	Manufacturer manufacturer;
	
	//--------------------------------------------------------------------------
	// Setup
	//--------------------------------------------------------------------------
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		facade = init.getDomainFacade();
		
	}
	
	//--------------------------------------------------------------------------
	// Test cases
	//--------------------------------------------------------------------------
	@Test
	public void test_FifoFlow() {
		//1. The user wants to select an alternative scheduling algorithm.
		//2. The system shows the available algorithms, as well as the currently
		//   selected algorithm.
		
		// test the getting of the algorithms. 
		List<SchedulingStrategyView> algorithms = facade.getAlgorithms();
		boolean hasFifo = false;
		boolean hasBatch = false;
		for(SchedulingStrategyView algorithm : algorithms){
			assertNotNull(algorithm);
			if(algorithm.getName().equals("Batch strategy")) hasBatch = true;
			if(algorithm.getName().equals("First-in first-out strategy")) hasFifo = true;
		}
		assertTrue(hasFifo && hasBatch);
		
		// test the current algorithm.
		SchedulingStrategyView curAlg = facade.getCurrentAlgorithm();
		assertNotNull(curAlg);
		
		//3. The user selects the new scheduling algorithm to be used.
		//4. The system applies the new scheduling algorithm5 and updates its
		//   state accordingly
		facade.setFifoAlgorithm();
		
		// Check if fifo algorithm is indeed used. 
		SchedulingStrategyView setAlg = facade.getCurrentAlgorithm();
		assertEquals(setAlg.getName(), "First-in first-out strategy");

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBatchFlow() {
		//1. The user wants to select an alternative scheduling algorithm.
		//2. The system shows the available algorithms, as well as the currently
		//   selected algorithm.
		
		// test the getting of the algorithms. 
		List<SchedulingStrategyView> algorithms = facade.getAlgorithms();
		boolean hasFifo = false;
		boolean hasBatch = false;
		for(SchedulingStrategyView algorithm : algorithms){
			assertNotNull(algorithm);
			if(algorithm.getName().equals("Batch strategy")) hasBatch = true;
			if(algorithm.getName().equals("First-in first-out strategy")) hasFifo = true;
		}
		assertTrue(hasFifo && hasBatch);
		
		// test the current algorithm.
		SchedulingStrategyView curAlg = facade.getCurrentAlgorithm();
		assertNotNull(curAlg);
		
		//3. The user selects the new scheduling algorithm to be used.
		//4. The system applies the new scheduling algorithm5 and updates its
		//   state accordingly
		List<Option> emptyOptions = new ArrayList<Option>();
		Specification emptySpec = new Specification(emptyOptions);
		facade.setBatchAlgorithm(emptySpec);;
		
		// Check if batch algorithm is indeed used. 
		SchedulingStrategyView setAlg = facade.getCurrentAlgorithm();
		assertEquals(setAlg.getName(), "Batch strategy");
		BatchStrategy<StandardOrder> usedAlg = (BatchStrategy<StandardOrder>) setAlg;
		
		// Check if orders are indeed ordered by the batch algorithm
		List<StandardOrder> cont = manufacturer.getProductionSchedule().getStandardOrderQueue();
		for (int i = 1; i < cont.size(); i++) {
			assertTrue(usedAlg.compare(cont.get(i - 1), cont.get(i)) <= 0);
		}
	}
}
