package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AssemblyTaskInfoTest.class,
				AssemblyTaskTest.class,
				DateTimeTest.class, 
				ManufacturerTest.class,
				ModelCatalogTest.class,
				NewOrderSessionHandlerTest.class,				
	            OrderTest.class,
	            WorkPostTest.class })
public class DomainTestSuite {

}
