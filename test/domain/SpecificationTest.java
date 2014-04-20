package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SpecificationTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	Specification testSpec;
	Option option1;
	Option option2;
	Option option3;
	
	@Before
	public void setUp() throws Exception {
		option1 = new Option(TaskType.BODY, "body", "mount body");
		option2 = new Option(TaskType.DRIVETRAIN, "drivetrain", "mount drivetrain");
		option3 = new Option(TaskType.ACCESSORIES, "accessories", "mount accessories");
		ArrayList<Option> options = new ArrayList<>();
		options.add(option1);
		options.add(option2);
		options.add(option3);
		testSpec = new Specification(options);
	}

	@Test
	public void constructorTest() {
		assertTrue(testSpec.getAmountOfOptions() == 3);
	}

	@Test
	public void constructorTestNullList() {
		exception.expect(IllegalArgumentException.class);
		testSpec = new Specification(null);
	}

	@Test
	public void constructorTestNullInList() {
		exception.expect(IllegalArgumentException.class);
		List<Option> opts = new ArrayList<>();
		opts.add(option1);
		opts.add(null);
		testSpec = new Specification(opts);
	}
	
	@Test
	public void getSpecTest() {
		assertTrue(testSpec.getOption(0).equals(option1));
		assertTrue(testSpec.getOption(1).equals(option2));
		assertTrue(testSpec.getOption(2).equals(option3));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSpecTestTooBigException() {
		testSpec.getOption(4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSpecTestNegativeException() {
		testSpec.getOption(-1);
	}

	@Test
	public void testEqualsObject() {
		assertFalse(testSpec.equals(null));
		assertFalse(testSpec.equals(option1));
		List<Option> sameOpts = new ArrayList<>();
		sameOpts.add(option1);
		sameOpts.add(option2);
		sameOpts.add(option3);
		List<Option> diffOpts = new ArrayList<>();
		diffOpts.add(option1);
		Specification testSpecSame = new Specification(sameOpts);
		Specification testSpecDiff = new Specification(diffOpts);
		assertTrue(testSpec.equals(testSpecSame));
		assertFalse(testSpec.equals(testSpecDiff));
		assertTrue(testSpec.equals(testSpec));
	}

}
