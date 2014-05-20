package domain.productionSchedule.strategy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AlgorithmStrategyFactoryTest.class,
				BatchComparatorTest.class,
				BatchStrategyTest.class,
				FifoComparatorTest.class,
				FifoStrategyTest.class })
public class StrategyTestSuite {

}
