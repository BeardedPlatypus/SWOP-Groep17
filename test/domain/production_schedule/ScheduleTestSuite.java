package domain.production_schedule;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SchedulerContextTest.class,
				OrderRequestTest.class})
public class ScheduleTestSuite {

}
