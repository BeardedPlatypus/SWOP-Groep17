package domain;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {
	
	Option testOption;
	
	@Before
	public void setUp() throws Exception {
		 testOption = new Option("OptionName","choice1","choice2","choice3");
	}
	
	@Test
	public void getChoiceNameTest() {
		assertTrue(testOption.getChoiceName(0).equals("choice1"));
		assertTrue(testOption.getChoiceName(1).equals("choice2"));
		assertTrue(testOption.getChoiceName(2).equals("choice3"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getChoiceNameIllegalArgumentNegativeTest() {
		testOption.getChoiceName(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getChoiceNameIllegalArgumentTooBigTest() {
		testOption.getChoiceName(5);
	}
	
	@Test
	public void constructorTest() {
		assertTrue(testOption.getOptionName().equals("OptionName"));
		assertTrue(testOption.getAmountOfChoices() == 3);
		testOption = new Option("OptionName2","choice1","choice2","choice3","choice4");
		assertTrue(testOption.getOptionName().equals("OptionName2"));
		assertTrue(testOption.getAmountOfChoices() == 4);
	}
	
	@Test
	public void getChoicesIteratorTest() {
		Iterator<String> choices = testOption.getChoicesIterator();
		assertTrue(choices.hasNext());
		assertTrue(choices.next().equals("choice1"));
		choices.next();
		choices.next();
		assertFalse(choices.hasNext());
	}


}
