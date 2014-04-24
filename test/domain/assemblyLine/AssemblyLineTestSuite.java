package domain.assemblyLine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AssemblyLineTest.class,
				AssemblyProcedureTest.class,
				AssemblyTaskTest.class,
				WorkPostTest.class })
public class AssemblyLineTestSuite {

}
