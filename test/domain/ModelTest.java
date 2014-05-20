package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.assemblyLine.TaskType;
import domain.car.CarModel;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Specification;
import exceptions.NoOptionCategoriesRemainingException;

/**
 * @author Frederik Goovaerts
 */
public class ModelTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	Option option1a;
	Option option1b;
	Option option2a;
	Option option2b;
	Option nonModelOption;
	OptionCategory optionCat1;
	OptionCategory optionCat2;
	Model testModel;
	
	@Before
	public void setUp() throws Exception {
		option1a = new Option(TaskType.BODY, "1a", "1a desc");
		option1b = new Option(TaskType.BODY, "1b", "1b desc");
		ArrayList<Option> options1 = new ArrayList<>();
		options1.add(option1a);
		options1.add(option1b);
		optionCat1 = new OptionCategory(options1, "Options1");
		option2a = new Option(TaskType.DRIVETRAIN, "2a", "2a desc");
		option2b = new Option(TaskType.DRIVETRAIN, "2b", "2b desc");
		ArrayList<Option> options2 = new ArrayList<>();
		options2.add(option2a);
		options2.add(option2b);
		optionCat2 = new OptionCategory(options2, "Options2");
		ArrayList<OptionCategory> cats = new ArrayList<>();
		cats.add(optionCat1);
		cats.add(optionCat2);
		testModel = new CarModel("TestModel", cats, 30);
		nonModelOption = new Option(TaskType.ACCESSORIES, "BadOption", "BadOptionInfo");
	}

	@Test
	public void constructorTest() {
		assertTrue(testModel.getAmountOfOptionCategories() == 2);
		assertTrue(testModel.getName().equals("TestModel"));
		assertTrue(testModel.getMinsOnWorkPostOfType(TaskType.BODY) == 30);
	}

	@Test
	public void testGetModelOptionCategory() {
		assertTrue(testModel.getModelOptionCategory(0).equals(optionCat1));
		assertTrue(testModel.getModelOptionCategory(1).equals(optionCat2));
	}
	
	@Test
	public void testGetModelOptionCategoryTooSmallIndex() {
		exception.expect(IllegalArgumentException.class);
		testModel.getModelOptionCategory(-1);
	}
	
	@Test
	public void testGetModelOptionCategoryTooLargeIndex() {
		exception.expect(IllegalArgumentException.class);
		testModel.getModelOptionCategory(5);
	}
	@Test
	public void testGetOptionCategories() {
		assertTrue(testModel.getOptionCategories().contains(optionCat1));
		assertTrue(testModel.getOptionCategories().contains(optionCat2));
		assertTrue(testModel.getOptionCategories().size()==2);
	}

	@Test
	public void testMakeSpecification() {
		ArrayList<Option> optionsForSpec = new ArrayList<>();
		optionsForSpec.add(option1a);
		optionsForSpec.add(option2a);
		Specification testSpec = testModel.makeSpecification(optionsForSpec);
		assertTrue(testSpec.getAmountOfOptions() == 2);
		assertTrue(testSpec.getOptions().contains(option1a));
		assertTrue(testSpec.getOptions().contains(option2a));
	}
	
	@Test
	public void testMakeSpecificationNullList() {
		exception.expect(IllegalArgumentException.class);
		testModel.makeSpecification(null);
	}
	
	@Test
	public void testMakeSpecificationNullInList() {
		exception.expect(IllegalArgumentException.class);
		ArrayList<Option> optionsForSpec = new ArrayList<>();
		optionsForSpec.add(option1a);
		optionsForSpec.add(null);
		testModel.makeSpecification(optionsForSpec);
	}
	
	@Test
	public void testMakeSpecificationOptionNotOfModel() {
		exception.expect(IllegalArgumentException.class);
		ArrayList<Option> optionsForSpec = new ArrayList<>();
		optionsForSpec.add(option1a);
		optionsForSpec.add(nonModelOption);
		testModel.makeSpecification(optionsForSpec);
	}

	@Test
	public void testCheckOptionsValidity() {
		ArrayList<Option> optionsForValid = new ArrayList<>();
		optionsForValid.add(option1a);
		optionsForValid.add(option2a);
		assertTrue(testModel.checkOptionsValidity(optionsForValid));
	}
	

	@Test
	public void testCheckOptionsValidityNullList() {
		exception.expect(IllegalArgumentException.class);
		assertTrue(testModel.checkOptionsValidity(null));
	}

	@Test
	public void testCheckOptionsValidityNullInList() {
		exception.expect(IllegalArgumentException.class);
		ArrayList<Option> optionsForValid = new ArrayList<>();
		optionsForValid.add(option1a);
		optionsForValid.add(null);
		assertTrue(testModel.checkOptionsValidity(optionsForValid));
	}
	
	@Test
	public void testCheckOptionsValidityNotContains() {
		ArrayList<Option> optionsForValid = new ArrayList<>();
		optionsForValid.add(option1a);
		optionsForValid.add(nonModelOption);
		assertFalse(testModel.checkOptionsValidity(optionsForValid));
	}
	
	@Test
	public void testCheckOptionsValidityDuplicate() {
		ArrayList<Option> optionsForValid = new ArrayList<>();
		optionsForValid.add(option1a);
		optionsForValid.add(option1a);
		assertFalse(testModel.checkOptionsValidity(optionsForValid));
	}

	@Test
	public void testGetNextOptionCategory() throws NoOptionCategoriesRemainingException {
		ArrayList<Option> optionList = new ArrayList<>();
		assertTrue((testModel.getNextOptionCategory(optionList).equals(optionCat1))||
				testModel.getNextOptionCategory(optionList).equals(optionCat2));
		optionList.add(option1a);
		assertTrue(testModel.getNextOptionCategory(optionList).equals(optionCat2));
		optionList.remove(option1a);
		optionList.add(option2a);
		assertTrue(testModel.getNextOptionCategory(optionList).equals(optionCat1));
	}
	
	//Throws declaration, don't even know why the system wants this.
	@Test
	public void testGetNextOptionCategoryNoneRemaining() throws NoOptionCategoriesRemainingException {
		exception.expect(NoOptionCategoriesRemainingException.class);
		ArrayList<Option> optionList = new ArrayList<>();
		optionList.add(option1a);
		optionList.add(option2a);
		testModel.getNextOptionCategory(optionList);
	}

	@Test
	public void testHasUnfilledOptions() {
		ArrayList<Option> optionList = new ArrayList<>();
		assertTrue(testModel.hasUnfilledOptions(optionList));
		optionList.add(option1a);
		assertTrue(testModel.hasUnfilledOptions(optionList));
		optionList.add(option2a);
		assertFalse(testModel.hasUnfilledOptions(optionList));
	}
	
	
}
