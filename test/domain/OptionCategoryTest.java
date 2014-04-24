package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.assemblyLine.TaskType;
import domain.car.Option;
import domain.car.OptionCategory;

/**
 * @author Frederik Goovaerts
 */
public class OptionCategoryTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	Option option1a;
	Option option1b;
	Option nonCatOption;
	OptionCategory optionCat;
	
	@Before
	public void setUp() throws Exception {
		option1a = new Option(TaskType.BODY, "1a", "1a desc");
		option1b = new Option(TaskType.BODY, "1b", "1b desc");
		ArrayList<Option> options1 = new ArrayList<>();
		options1.add(option1a);
		options1.add(option1b);
		optionCat = new OptionCategory(options1);
		nonCatOption = new Option(TaskType.ACCESSORIES, "BadOption", "BadOptionInfo");
	}

	@Test
	public void testConstructor() {
		assertTrue(optionCat.getAmountOfOptions() == 2);
		assertTrue(optionCat.getOption(0).equals(option1a)||
				optionCat.getOption(0).equals(option1b));
		assertTrue(optionCat.getOption(1).equals(option1a)||
				optionCat.getOption(1).equals(option1b));
	}
	
	@Test
	public void testConstructorNullList() {
		exception.expect(IllegalArgumentException.class);
		optionCat = new OptionCategory(null);
	}
	
	@Test
	public void testGetOptionTooSmallArgument() {
		exception.expect(IllegalArgumentException.class);
		optionCat.getOption(-1);
	}
	
	@Test
	public void testGetOptionTooLargeArgument() {
		exception.expect(IllegalArgumentException.class);
		optionCat.getOption(5);
	}
	@Test
	public void testConstructorNullInList() {
		exception.expect(IllegalArgumentException.class);
		option1a = new Option(TaskType.BODY, "1a", "1a desc");
		ArrayList<Option> options1 = new ArrayList<>();
		options1.add(option1a);
		options1.add(null);
		optionCat = new OptionCategory(options1);
	}
	
	@Test
	public void testContainsOption() {
		assertTrue(optionCat.containsOption(option1a));
		assertFalse(optionCat.containsOption(nonCatOption));
	}
}
