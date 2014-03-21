package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdvanceAssemblyLineHandlerTest.class,
				AssemblyLineTest.class,
				AssemblyProcedureTest.class,
				AssemblyTaskTest.class,
				DateTimeTest.class, 
				ManufacturerTest.class,
				ModelCatalogTest.class,
				ModelTest.class,
				NewOrderSessionHandlerTest.class,
				OptionTest.class,
	            OrderTest.class,
	            PerformAssemblyTaskHandlerTest.class,
	            ProductionScheduleTest.class,
	            SpecificationTest.class,
	            WorkPostTest.class })
public class DomainTestSuite {

}
