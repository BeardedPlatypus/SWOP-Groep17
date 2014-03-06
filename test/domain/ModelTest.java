package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ModelTest {
	
	Option testBodyOption;
	Option testPaintOption;
	Model testModel;
	
	@Before
	public void setUp() throws Exception {
		testBodyOption = new Option("Body", "sedan", "break");
		testPaintOption = new Option("Paint", "pink", "blue");
		ArrayList<Option> options = new ArrayList<Option>();
		options.add(testBodyOption);
		options.add(testPaintOption);
		testModel = new Model("TestModel", options);
	}

	@Test
	public void constructorTest() {
		ArrayList<Option> options = new ArrayList<Option>();
		options.add(testBodyOption);
		options.add(testPaintOption);
		Model testModel2 = new Model("Model2", options);
		assertTrue(testModel2.getModelName().equals("Model2"));
		assertTrue(testModel2.getAmountOfOptions() == 2);
	}
	
	@Test
	public void getModelOptionTest() {
		assertTrue(testModel.getModelOption(0).equals(testBodyOption));
		assertTrue(testModel.getModelOption(1).equals(testPaintOption));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getModelOptionTooBigTest() {
		testModel.getModelOption(2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getModelOptionNegativeTest() {
		testModel.getModelOption(-1);
	}
	
	@Test
	public void makeSpecificationsTest() {
		int[] choices = {1,2};
		Specification testSpec = testModel.makeSpecification(choices);
		assertTrue(testSpec.getSpec(1) == 2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void makeSpecificationsInvalidSpecsTest() {
		int[] choices = {1,3};
		Specification testSpec = testModel.makeSpecification(choices);
		testSpec.getSpec(1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void makeSpecificationsWrongNumberTest() {
		int[] choices = {1,1,1};
		Specification testSpec = testModel.makeSpecification(choices);
		testSpec.getSpec(1);
	}
	
	@Test
	public void validSpecificationsTest() {
		int[] choices = {1,2};
		Specification testValidSpec = new Specification(choices);
		choices[1] = 3;
		Specification testInvalidSpec = new Specification(choices);
		int[] choices2 = {1,1,1};
		Specification testMoreInvalidSpec = new Specification(choices2);
		assertTrue(testModel.isValidSpecification(testValidSpec));
		assertFalse(testModel.isValidSpecification(testInvalidSpec));
		assertFalse(testModel.isValidSpecification(testMoreInvalidSpec));
	}
	
	
}
