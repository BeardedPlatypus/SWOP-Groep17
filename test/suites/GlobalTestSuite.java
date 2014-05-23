package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.assembly_line.AssemblyLineTestSuite;
import domain.handlers.HandlersTestSuite;
import domain.order.OrderTestSuite;
import domain.production_schedule.ProductionScheduleTestSuite;
import domain.production_schedule.strategy.StrategyTestSuite;
import domain.restrictions.RestrictionsTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ DomainTestSuite.class,				// domain
				AssemblyLineTestSuite.class,		// domain.assemblyLine
				HandlersTestSuite.class,			// domain.handlers
				OrderTestSuite.class,				// domain.order
				ProductionScheduleTestSuite.class,	// domain.productionSchedule
				StrategyTestSuite.class,			// domain.productionSchedule.strategy
				RestrictionsTestSuite.class })		// domain.restrictions
public class GlobalTestSuite {


}
