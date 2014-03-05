package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SpecificationTest {

	Specification testSpec;
	
	@Before
	public void setUp() throws Exception {
		int[] choices = {1,4,3};
		testSpec = new Specification(choices);
	}

	@Test
	public void constructorTest() {
		assertTrue(testSpec.getAmountofSpecs() == 3);
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
