package domain.car;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CarModelTest.class,
				ModelCatalogTest.class,
				ModelTest.class,
				OptionCategoryTest.class,
				OptionTest.class,
				TruckModelTest.class })
public class CarTestSuite {

}
