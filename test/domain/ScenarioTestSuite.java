package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import deprecated.AdvanceAssemblyLineScenario;

@RunWith(Suite.class)
@SuiteClasses({ PerformAssemblyTaskScenario.class,
				NewOrderSessionScenario.class,
				AdvanceAssemblyLineScenario.class })
public class ScenarioTestSuite {

}
