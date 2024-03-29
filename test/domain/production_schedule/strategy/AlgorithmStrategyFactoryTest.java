package domain.production_schedule.strategy;

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

import domain.assembly_line.TaskType;
import domain.car.Option;
import domain.car.Specification;
import domain.order.StandardOrder;
import domain.production_schedule.strategy.AlgorithmStrategyFactory;
import domain.production_schedule.strategy.BatchComparator;
import domain.production_schedule.strategy.BatchStrategy;
import domain.production_schedule.strategy.FifoStrategy;
import domain.production_schedule.strategy.SchedulingStrategy;
import domain.production_schedule.strategy.SchedulingStrategyView;

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
		SchedulingStrategy<StandardOrder> strat = factory.getFifoStrategy();
		assertEquals(FifoStrategy.class, strat.getClass());
	}
	
	@Test
	public void getBatchStrategyTest() {
		Option option = new Option(TaskType.BODY, "john", "doe");
		Specification spec = new Specification(new ArrayList<Option>(Arrays.asList(option)));
		BatchStrategy<StandardOrder> strat = (BatchStrategy<StandardOrder>) factory.getBatchStrategy(spec);
		//BatchStrategy strat = new BatchStrategy(spec);
		BatchComparator comp = Whitebox.getInternalState(strat, BatchComparator.class);
		assertEquals(spec, Whitebox.getInternalState(comp, Specification.class));
	}

}
