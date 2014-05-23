package domain.order;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CompletedOrderCatalogTest.class,
				OrderFactoryTest.class,
				SingleTaskOrderTest.class,
				StandardOrderTest.class })
public class OrderTestSuite {

}
