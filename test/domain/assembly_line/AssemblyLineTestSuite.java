package domain.assembly_line;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActiveStateTest.class,
				AssemblyLineBuilderTest.class,
				AssemblyLineControllerTest.class,
				AssemblyLineTest.class,
				AssemblyProcedureTest.class,
				AssemblyTaskTest.class,
				BrokenStateTest.class,
				LayoutFactoryTest.class,
				LayoutManipulatorTest.class,
				OperationalStateTest.class,
				StateCatalogTest.class,
				WorkPostTest.class })
public class AssemblyLineTestSuite {

}
