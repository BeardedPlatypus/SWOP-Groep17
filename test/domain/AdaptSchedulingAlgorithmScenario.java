package domain;

import junit.framework.TestCase;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.productionSchedule.strategy.AlgorithmStrategyFactory;
import domain.productionSchedule.strategy.AlgorithmView;
import domain.handlers.AdaptSchedulingAlgorithmHandler;

public class AdaptSchedulingAlgorithmScenario extends TestCase {
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	AdaptSchedulingAlgorithmHandler handler;
	AlgorithmStrategyFactory algFac;
	
	//--------------------------------------------------------------------------
	// Setup
	//--------------------------------------------------------------------------
	@Before
	public void setUp() throws Exception {
		
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
		List<AlgorithmView> algorithms = handler.getAlgorithms();
		
		// test the current algorithm.
		AlgorithmView curAlg = handler.getCurrentAlgorithm();
		
		//3. The user selects the new scheduling algorithm to be used.
		//4. The system applies the new scheduling algorithm5 and updates its
		//   state accordingly
		handler.setFifoAlgorithm();
		
		// Check if fifo algorithm is indeed used. 
		AlgorithmView setAlg = handler.getCurrentAlgorithm();
		assertEquals(null, setAlg); //FIXME change this fifo algorithm view.
		
		// Check if orders are indeed ordered by the fifo algorithm

	}
	
	@Test
	public void testBatchFlow() {
		//1. The user wants to select an alternative scheduling algorithm.
		//2. The system shows the available algorithms, as well as the currently
		//   selected algorithm.
		
		// test the getting of the algorithms. 
		List<AlgorithmView> algorithms = handler.getAlgorithms();
		
		// test the current algorithm.
		AlgorithmView curAlg = handler.getCurrentAlgorithm();
		
		//3. (a) The user indicates he wants to use the Specifcation Batch al-
		//       gorithm.
		//4. The system shows a list of the sets of car options for which more
		//   than 3 orders are pending in the production queue
		List<Specification> batches = handler.getCurrentBatches();
		
		// Check if all batches that should be in there are there
		assertEquals(null, batches); //FIXME
		
		
		//5. The user selects one of these sets for batch processing
		Specification batch = null; //FIXME 
		
		//4. The system applies the new scheduling algorithm5 and updates its
		//   state accordingly
		handler.setBatchAlgorithm(batch);
		
		// Check if batch algorithm is indeed used. 
		AlgorithmView setAlg = handler.getCurrentAlgorithm();
		assertEquals(null, setAlg); //FIXME change this batch algorithm view.
		
		// Check if orders are indeed ordered by the batch algorithm
	}
}
