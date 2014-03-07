package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class AssemblyProcedureTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock Order order;
	
	AssemblyProcedure procedure;
	
	AssemblyTask color;
	AssemblyTask body;
	AssemblyTask engine;
	
	List<AssemblyTask> tasks;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		color = new AssemblyTask("blue", "Paint the car blue", TaskType.BODY, 0);
		body = new AssemblyTask("sedan", "Assemble a sedan body", TaskType.BODY, 1);
		engine = new AssemblyTask("4 cilinders", "Install standard 2l 4 cilinders", TaskType.DRIVETRAIN, 2);
		
		tasks = new ArrayList<AssemblyTask>();
		tasks.add(color);
		tasks.add(body);
		tasks.add(engine);
		
		procedure = Mockito.spy(new AssemblyProcedure(order, tasks));
	}
	
	@Test
	public void constructor_NullOrderTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(null, tasks);
	}
	
	@Test
	public void completeTask_negativeIntTask() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(-1, TaskType.BODY);
		
	}
	
	@Test
	public void completeTask_TooBigIntTask() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(3, TaskType.BODY);
	}
	
	@Test
	public void completeTask_WrongTaskType() {
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(2, TaskType.BODY);
	}
	
	@Test
	public void completeTask_valid() {
		assertFalse(body.isCompleted());
		procedure.completeTask(1, TaskType.BODY);
		assertTrue(body.isCompleted());
	}
	
	@Test
	public void getTasksOfType_test() {
		List<AssemblyTaskContainer> containers = procedure.getAssemblyTasks(TaskType.BODY);
		assertEquals(containers.size(), 2);
		assertSame(containers.get(0), color);
		assertSame(containers.get(1), body);
		assertFalse(containers.contains(engine));
	}

}
