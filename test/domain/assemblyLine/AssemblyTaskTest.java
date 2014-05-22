package domain.assemblyLine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.assembly_line.AssemblyTask;
import domain.assembly_line.TaskType;
import domain.car.Option;

public class AssemblyTaskTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	String optionName = "Blue";
	String optionDescription = "Paint the car blue.";
	TaskType optionType = TaskType.BODY;
	
	@Mock Option option;
	
	AssemblyTask task;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(option.getName()).thenReturn(optionName);
		Mockito.when(option.getDescription()).thenReturn(optionDescription);
		Mockito.when(option.getType()).thenReturn(optionType);
		
		task = new AssemblyTask(option, 1);
	}

	@Test
	public void constructor_NullOptionTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask(null, 0);
	}
	
	@Test
	public void constructor_NegativeTaskNumberTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask(option, -1);
	}
	
	@Test
	public void constructor_ValidArgumentsTest() {
		assertEquals(task.getOption(), option);
		assertTrue(task.getTaskNumber() == 1);
	}
	
	@Test
	public void getOptionNameTest() {
		assertEquals(task.getOptionName(), optionName);
	}
	
	@Test
	public void getOptionDescriptionTest() {
		assertEquals(task.getOptionDescription(), optionDescription);
	}
	
	@Test
	public void getTaskTypeTest() {
		assertEquals(task.getTaskType(), optionType);
	}

	@Test
	public void setCompletedTest() {
		assertFalse(task.isCompleted());
		task.setCompleted(true);
		assertTrue(task.isCompleted());
		task.setCompleted(false);
		assertFalse(task.isCompleted());
	}
}
