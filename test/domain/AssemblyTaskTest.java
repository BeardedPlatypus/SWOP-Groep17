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
		new AssemblyTask(null, "dummy", TaskType.BODY);
	}
	
	@Test
	public void constructor_NullActionInfoTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask("dummy", null, TaskType.BODY);
	}
	
	@Test
	public void constructor_NullTaskTypeTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyTask("dummy", "dummy", null);
	}
	
	@Test
	public void constructor_ValidArgumentsTest() {
		AssemblyTask task = new AssemblyTask("john", "doe", TaskType.BODY);
		assertEquals(task.getName(), "john");
		assertEquals(task.getActionInfo(), "doe");
		assertEquals(task.getTaskType(), TaskType.BODY);
	}
	
	@Test
	public void getTaskInfoTest() {
		AssemblyTask task = new AssemblyTask("john", "doe", TaskType.BODY);
		AssemblyTaskInfo taskInfo = task.getTaskInfo(0);
		assertEquals(taskInfo.isCompleted(), false);
		assertEquals(taskInfo.getName(), task.getName());
		assertEquals(taskInfo.getActionInfo(), task.getActionInfo());
		assertEquals(taskInfo.getTaskNumber(), 0);
		assertEquals(taskInfo.getTaskType(), task.getTaskType());
		
		task.setCompleted(true);
		taskInfo = task.getTaskInfo(0);
		assertEquals(taskInfo.isCompleted(), true);
	}

}
