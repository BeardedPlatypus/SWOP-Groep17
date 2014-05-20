package domain.assemblyLine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AssemblyLineBuilderTest.class,
				AssemblyLineTest.class,
				AssemblyProcedureTest.class,
				AssemblyTaskTest.class,
				LayoutFactoryTest.class,
				OrderAcceptanceChecker.class,
				WorkPostTest.class })
public class AssemblyLineTestSuite {

}
