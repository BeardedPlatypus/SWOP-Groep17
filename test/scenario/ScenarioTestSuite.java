package scenario;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NewOrderSessionScenario.class,
				AdaptSchedulingAlgorithmScenario.class,
				CheckAssemblyLineStatusScenario.class,
				CheckAssemblyLineStatusScenario_Alternate.class,
				CheckProductionStatisticsScenario.class,
				CheckProductionStatisticsScenario_Alternate.class,
				OrderDetailsScenario.class,
				OrderDetailsScenario_Alternate.class,
				OrderSingleTaskScenario.class,
				PerformAssemblyTaskScenario.class})
public class ScenarioTestSuite {

}
