package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.assemblyLine.AssemblyLineTestSuite;
import domain.handlers.HandlersTestSuite;
import domain.order.OrderTestSuite;
import domain.productionSchedule.SchedulerContextTest;
import domain.productionSchedule.strategy.AlgorithmStrategyFactoryTest;
import domain.productionSchedule.strategy.BatchComparatorTest;
import domain.productionSchedule.strategy.FifoComparatorTest;
import domain.productionSchedule.strategy.FifoStrategyTest;
import domain.restrictions.OptionProhibitsOtherSetRestrictionTest;
import domain.restrictions.OptionRequiresOtherSetRestrictionTest;
import domain.restrictions.*;
import scenario.ScenarioTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ DomainTestSuite.class,
				AssemblyLineTestSuite.class,
				HandlersTestSuite.class,
				ScenarioTestSuite.class,
				OrderTestSuite.class,
				
				ClockManagerTest.class,
				SchedulerContextTest.class,
				AlgorithmStrategyFactoryTest.class,
				BatchComparatorTest.class,
				FifoComparatorTest.class,
				FifoStrategyTest.class,
				
				OptionProhibitsOtherSetRestrictionTest.class,
				OptionRequiresOtherSetRestrictionTest.class,
				OptionRestrictionManagerTest.class,
				RequiredOptionSetRestrictionTest.class,
				RestrictionTest.class})
public class GlobalTestSuite {


}
