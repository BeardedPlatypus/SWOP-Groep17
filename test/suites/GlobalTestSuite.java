package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.DomainTestSuite;
import domain.assembly_line.AssemblyLineTestSuite;
import domain.car.CarTestSuite;
import domain.clock.ClockTestSuite;
import domain.handlers.HandlersTestSuite;
import domain.order.OrderTestSuite;
import domain.production_schedule.ScheduleTestSuite;
import domain.production_schedule.strategy.StrategyTestSuite;
import domain.restrictions.RestrictionsTestSuite;
import domain.statistics.StatisticsTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ DomainTestSuite.class,				// domain
				AssemblyLineTestSuite.class,		// domain.assemblyLine
				CarTestSuite.class,					// domain.car
				ClockTestSuite.class,				// domain.clock
				HandlersTestSuite.class,			// domain.handlers
				OrderTestSuite.class,				// domain.order
				ScheduleTestSuite.class,			// domain.productionSchedule
				StrategyTestSuite.class,			// domain.productionSchedule.strategy
				StatisticsTestSuite.class,			// domain.statistics
				RestrictionsTestSuite.class })		// domain.restrictions
public class GlobalTestSuite {


}
