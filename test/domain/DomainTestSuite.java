package domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import domain.assemblyLine.AssemblyLineTest;
import domain.assemblyLine.AssemblyProcedureTest;
import domain.assemblyLine.AssemblyTaskTest;
import domain.assemblyLine.WorkPostTest;
import domain.handlers.NewOrderSessionHandlerTest;
import domain.handlers.PerformAssemblyTaskHandlerTest;
import domain.order.StandardOrderTest;

@RunWith(Suite.class)
@SuiteClasses({ AssemblyLineTest.class,
				AssemblyProcedureTest.class,
				AssemblyTaskTest.class,
				DateTimeTest.class, 
				ManufacturerTest.class,
				ModelCatalogTest.class,
				ModelTest.class,
				NewOrderSessionHandlerTest.class,
				OptionTest.class,
	            StandardOrderTest.class,
	            PerformAssemblyTaskHandlerTest.class,
	            ProductionScheduleTest.class,
	            SpecificationTest.class,
	            WorkPostTest.class })
public class DomainTestSuite {

}
