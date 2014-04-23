package domain.productionSchedule.strategy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.junit4.*;

import domain.Option;
import domain.Specification;
import domain.TaskType;

@RunWith(PowerMockRunner.class)
public class AlgorithmStrategyFactoryTest {

	AlgorithmStrategyFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		factory = new AlgorithmStrategyFactory();
	}

	@Test
	public void getAlgorithmViewsTest() {
		List<SchedulingStrategyView> strats = factory.getAlgorithmViews();
		assertEquals(FifoStrategy.class, strats.get(0).getClass());
		assertEquals(BatchStrategy.class, strats.get(1).getClass());
	}
	
	@Test
	public void getFifoStrategyTest() {
		SchedulingStrategy strat = factory.getFifoStrategy();
		assertEquals(FifoStrategy.class, strat.getClass());
	}
	
	@Test
	public void getBatchStrategyTest() {
		Option option = new Option(TaskType.BODY, "john", "doe");
		Specification spec = new Specification(new ArrayList<Option>(Arrays.asList(option)));
		BatchStrategy strat = new BatchStrategy(spec);
		BatchComparator comp = Whitebox.getInternalState(strat, BatchComparator.class);
		assertEquals(spec, Whitebox.getInternalState(comp, Specification.class));
	}

}
