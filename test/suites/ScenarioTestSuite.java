package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import scenario.AdaptSchedulingAlgorithmScenario;
import scenario.CheckAssemblyLineStatusScenario;
import scenario.CheckAssemblyLineStatusScenario_Alternate;
import scenario.CheckProductionStatisticsScenario;
import scenario.CheckProductionStatisticsScenario_Alternate;
import scenario.NewOrderSessionScenario;
import scenario.OrderDetailsScenario;
import scenario.OrderDetailsScenario_Alternate;
import scenario.OrderSingleTaskScenario;
import scenario.PerformAssemblyTaskScenario;

@RunWith(Suite.class)
@SuiteClasses({ AdaptSchedulingAlgorithmScenario.class,
				CheckAssemblyLineStatusScenario.class,
				CheckAssemblyLineStatusScenario_Alternate.class,
				CheckProductionStatisticsScenario.class,
				CheckProductionStatisticsScenario_Alternate.class,
				NewOrderSessionScenario.class,
				OrderDetailsScenario.class,
				OrderDetailsScenario_Alternate.class,
				OrderSingleTaskScenario.class,
				PerformAssemblyTaskScenario.class})
public class ScenarioTestSuite {

}
