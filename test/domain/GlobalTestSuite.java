package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.assemblyLine.AssemblyLineTestSuite;
import domain.handlers.HandlersTestSuite;
import domain.order.OrderTestSuite;
import domain.productionSchedule.ProductionScheduleTestSuite;
import domain.productionSchedule.strategy.StrategyTestSuite;
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
