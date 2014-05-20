package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CarModelTest.class,
				CarsProducedRegistrarTest.class,
				ClockManagerTest.class,
				CompletedOrderCatalogTest.class,
				DateTimeTest.class,
				DelayRegistrarTest.class,
				MedianSelectorTest.class,
				ModelCatalogTest.class,
				ModelTest.class,
				OptionCategoryTest.class,
				OptionTest.class,
				OrderSessionTest.class,
				ProcedureStatisticsTest.class,
				SingleOrderSessionTest.class,
				SingleTaskCatalogTest.class,
				SpecificationTest.class,
				StatisticsLoggerTest.class,
				TruckModelTest.class,
				WorkingDayTest.class })
public class DomainTestSuite {

}
