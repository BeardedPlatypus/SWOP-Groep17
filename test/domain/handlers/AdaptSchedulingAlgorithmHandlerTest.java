package domain.handlers;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.*;

import domain.Manufacturer;
import domain.car.Specification;

@RunWith(PowerMockRunner.class)
public class AdaptSchedulingAlgorithmHandlerTest {

	AdaptSchedulingAlgorithmHandler handler;
	
	@Mock Manufacturer manufacturer;
	@Mock Specification specification;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		handler = new AdaptSchedulingAlgorithmHandler(manufacturer);
	}

	@Test
	public void getAlgorithmsTest() {
		handler.getAlgorithms();
		Mockito.verify(manufacturer).getAlgorithms();
	}
	
	@Test
	public void getCurrentAlgorithmTest() {
		handler.getCurrentAlgorithm();
		Mockito.verify(manufacturer).getCurrentAlgorithm();
	}
	
	@Test
	public void getCurrentBatchesTest() {
		handler.getCurrentBatches();
		Mockito.verify(manufacturer).getCurrentBatches();
	}
	
	@Test
	public void setFifoAlgorithmTest() {
		handler.setFifoAlgorithm();
		Mockito.verify(manufacturer).setFifoAlgorithm();
	}
	
	@Test
	public void setBatchAlgorithmTest() {
		handler.setBatchAlgorithm(specification);
		Mockito.verify(manufacturer).setBatchAlgorithm(specification);
	}

}
