package domain.restrictions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.assembly_line.TaskType;
import domain.car.Option;

public class OptionProhibitsOtherSetRestrictionTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	Option option1a;
	Option option1b;
	Option option2a;
	Option option2b;
	OptionProhibitsOtherSetRestriction testRestr;
	
	@Before
	public void setUp() throws Exception {
		option1a = new Option(TaskType.BODY, "1a", "1a desc");
		option1b = new Option(TaskType.BODY, "1b", "1b desc");
		option2a = new Option(TaskType.DRIVETRAIN, "2a", "2a desc");
		option2b = new Option(TaskType.DRIVETRAIN, "2b", "2b desc");
	}

	@Test
	public void testConstructor() {
		Set<Option> proh = new HashSet<Option>();
		proh.add(option2a);
		testRestr = new OptionProhibitsOtherSetRestriction(option1a, proh);
	}
	
	@Test
	public void testConstructorNullOpt() {
		exception.expect(IllegalArgumentException.class);
		Set<Option> proh = new HashSet<Option>();
		proh.add(option2a);
		testRestr = new OptionProhibitsOtherSetRestriction(null, proh);
	}
	
	@Test
	public void testConstructorNullList() {
		exception.expect(IllegalArgumentException.class);
		Set<Option> proh = null;
		testRestr = new OptionProhibitsOtherSetRestriction(option1a, proh);
	}	
	
	@Test
	public void testConstructorNullInList() {
		exception.expect(IllegalArgumentException.class);
		Set<Option> proh = new HashSet<Option>();
		proh.add(null);
		testRestr = new OptionProhibitsOtherSetRestriction(option1a, proh);
	}
	
	@Test
	public void testIsLegalTrue(){
		testConstructor(); // set up the restriction in the test of the constructor.
		ArrayList<Option> options = new ArrayList<>();
		options.add(option1a);
		options.add(option2b);
		assertTrue(testRestr.isLegalOptionList(options));
	}
	
	@Test
	public void testIsLegalFalse(){
		testConstructor(); // set up the restriction in the test of the constructor.
		ArrayList<Option> options = new ArrayList<>();
		options.add(option1a);
		options.add(option2a);
		assertFalse(testRestr.isLegalOptionList(options));
	}
	
	@Test
	public void testIsLegalNotPresent(){
		testConstructor(); // set up the restriction in the test of the constructor.
		ArrayList<Option> options = new ArrayList<>();
		options.add(option1b);
		options.add(option2a);
		assertTrue(testRestr.isLegalOptionList(options));
	}
	

}
