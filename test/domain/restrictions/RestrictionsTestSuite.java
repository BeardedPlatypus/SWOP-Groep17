package domain.restrictions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OptionProhibitsOtherSetRestrictionTest.class,
				OptionRequiresOtherSetRestrictionTest.class,
				OptionRestrictionManagerTest.class,
				RequiredOptionSetRestrictionTest.class,
				RestrictionTest.class })
public class RestrictionsTestSuite {

}
