package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import scenario.ScenarioTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ DomainTestSuite.class,
				ScenarioTestSuite.class })
public class GlobalTestSuite {


}
