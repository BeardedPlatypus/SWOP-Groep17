package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.DateTimeTest;
import domain.car.CarModelTest;
import domain.car.ModelCatalogTest;
import domain.car.ModelTest;
import domain.car.OptionCategoryTest;
import domain.car.OptionTest;
import domain.car.TruckModelTest;
import domain.order.OrderSessionTest;
import domain.order.SingleOrderSessionTest;
import domain.order.SingleTaskCatalogTest;
import domain.order.SpecificationTest;
import domain.statistics.CarsProducedRegistrarTest;
import domain.statistics.DelayRegistrarTest;
import domain.statistics.MedianSelectorTest;
import domain.statistics.ProcedureStatisticsTest;
import domain.statistics.StatisticsLoggerTest;
import domain.statistics.WorkingDayTest;

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
