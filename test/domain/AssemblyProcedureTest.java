package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AssemblyProcedureTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock Order order;
	@Mock AssemblyProcedure procedure;
	
	AssemblyTask color = new AssemblyTask("blue", "Paint the car blue", TaskType.BODY);
	AssemblyTask engine = new AssemblyTask("4 cilinders", "Install standard 2l 4 cilinders", TaskType.DRIVETRAIN);
	AssemblyTask body = new AssemblyTask("sedan", "Assemble a sedan body", TaskType.BODY);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyTaskInfo> bodyTasks = new ArrayList<AssemblyTaskInfo>();
		bodyTasks.add(color.getTaskInfo(1));
		bodyTasks.add(body.getTaskInfo(2));
		
		List<AssemblyTaskInfo> drivetrainTasks = new ArrayList<AssemblyTaskInfo>();
		bodyTasks.add(engine.getTaskInfo(1));
		
		Mockito.when(procedure.getAssemblyTasks(TaskType.BODY)).thenReturn(bodyTasks);
		Mockito.when(procedure.getAssemblyTasks(TaskType.DRIVETRAIN)).thenReturn(drivetrainTasks);
		
		Mockito.
	}
	
	@Test
	public void constructor_NullOrderTest() {
		exception.expect(IllegalArgumentException.class);
		new AssemblyProcedure(null);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
