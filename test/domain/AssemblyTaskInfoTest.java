package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AssemblyTaskInfoTest {

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
		new AssemblyTaskContainer(false, null, "dummy", 0, TaskType.BODY);
	}
	
	@Test
	public void constructor_NegativeTaskNumberTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTaskContainer(false, "dummy", "dummy", -1, TaskType.BODY);
	}
	
	@Test
	public void constructor_NullActionInfoTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTaskContainer(false, "dummy", null, 0, TaskType.BODY);
	}
	
	@Test
	public void constructor_NullTaskTypeTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTaskContainer(false, "dummy", "dummy", 0, null);
	}
	
	@Test
	public void constructor_ValidArgumentsTest() {
		AssemblyTaskContainer task = new AssemblyTaskContainer(false, "john", "doe", 0, TaskType.BODY);
		assertEquals(task.isCompleted(), false);
		assertEquals(task.getName(), "john");
		assertEquals(task.getActionInfo(), "doe");
		assertEquals(task.getTaskNumber(), 0);
		assertEquals(task.getTaskType(), TaskType.BODY);
	}

}
