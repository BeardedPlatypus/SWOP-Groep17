package domain.statistics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CarsProducedRegistrarTest.class,
				DelayRegistrarTest.class,
				MedianSelectorTest.class,
				ProcedureStatisticsTest.class,
				StatisticsLoggerTest.class,
				WorkingDayTest.class })
public class StatisticsTestSuite {

}
