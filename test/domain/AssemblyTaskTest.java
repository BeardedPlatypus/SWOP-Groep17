package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AssemblyTaskTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void constructor_NullNameTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask(null, "dummy", TaskType.BODY, 0);
	}
	
	@Test
	public void constructor_NullActionInfoTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask("dummy", null, TaskType.BODY, 0);
	}
	
	@Test
	public void constructor_NullTaskTypeTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask("dummy", "dummy", null, 0);
	}
	
	@Test
	public void constructor_ValidArgumentsTest() {
		AssemblyTask task = new AssemblyTask("john", "doe", TaskType.BODY, 1);
		assertEquals(task.getName(), "john");
		assertEquals(task.getActionInfo(), "doe");
		assertEquals(task.getTaskType(), TaskType.BODY);
		assertTrue(task.getTaskNumber() == 1);
	}

	@Test
	public void setCompletedTest() {
		AssemblyTask task = new AssemblyTask("baz", "qux", TaskType.BODY, 1);
		assertFalse(task.isCompleted());
		task.setCompleted(true);
		assertTrue(task.isCompleted());
		task.setCompleted(false);
		assertFalse(task.isCompleted());
	}
}
