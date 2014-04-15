package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SpecificationTest {

	Specification testSpec;
	
	@Before
	public void setUp() throws Exception {
		Option option1 = new Option(TaskType.BODY, "body", "mount body");
		Option option2 = new Option(TaskType.DRIVETRAIN, "drivetrain", "mount drivetrain");
		Option option3 = new Option(TaskType.ACCESSORIES, "accessories", "mount accessories");
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
	public void getSpecTest() {
		assertTrue(testSpec.getSpec(0) == 1);
		assertTrue(testSpec.getSpec(1) == 4);
		assertTrue(testSpec.getSpec(2) == 3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSpecTestTooBigException() {
		testSpec.getSpec(4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSpecTestNegativeException() {
		testSpec.getSpec(-1);
	}

}
