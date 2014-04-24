package scenario;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PerformAssemblyTaskScenario.class,
				NewOrderSessionScenario.class,
				AdaptSchedulingAlgorithmScenario.class,
				CheckAssemblyLineStatusScenario.class,
				CheckProductionStatisticsScenario.class,
				OrderDetailsScenario.class,
				OrderSingleTaskScenario.class})
public class ScenarioTestSuite {

}
