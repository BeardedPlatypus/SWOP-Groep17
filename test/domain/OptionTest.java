package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

public class OptionTest {
	
	Option testOption;
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testOption = new Option(TaskType.ACCESSORIES, "option","description");
	}
	
	@Test
	public void testConstructor() {
		assertTrue(testOption.getType().equals(TaskType.ACCESSORIES));
		assertTrue(testOption.getName().equals("option"));
		assertTrue(testOption.getDescription().equals("description"));
	}
	
	@Test
	public void testConstructorNullName() {
		exception.expect(IllegalArgumentException.class);
		testOption = new Option(TaskType.ACCESSORIES, null,"description");
	}
	
	@Test
	public void testConstructorNullType() {
		exception.expect(IllegalArgumentException.class);
		testOption = new Option(null, "option","description");
	}
	
	@Test
	public void testConstructorNullDesc() {
		exception.expect(IllegalArgumentException.class);
		testOption = new Option(TaskType.ACCESSORIES, "option",null);
	}

	@Test
	public void testEqualsObject() {
		Option testOptionSame = new Option(TaskType.ACCESSORIES, "option", "description");
		assertTrue(testOption.equals(testOptionSame));
	}
	
	@Test
	public void testNotEqualsObject(){
		Option testOptionDifferentName = new Option(TaskType.ACCESSORIES, "another option", "other desc");
		Option testOptionDifferentType = new Option(TaskType.BODY, "option", "other desc");
		assertFalse(testOption.equals(testOptionDifferentName));
		assertFalse(testOption.equals(testOptionDifferentType));
		assertFalse(testOption.equals(new String("someString")));
	}
	
	@Test
	public void testEqualsSameObject(){
		assertTrue(testOption.equals(testOption));
	}
	
	@Test
	public void testNotEqualsNullObject(){
		assertFalse(testOption.equals(null));
	}
}
