package domain.productionSchedule;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClockManagerTest.class,
				SchedulerContextTest.class })
public class ProductionScheduleTestSuite {

}
