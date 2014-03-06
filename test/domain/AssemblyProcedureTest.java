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
	
	AssemblyTask color = new AssemblyTask("blue", "Paint the car blue", TaskType.BODY, 0);
	AssemblyTask engine = new AssemblyTask("4 cilinders", "Install standard 2l 4 cilinders", TaskType.DRIVETRAIN, 0);
	AssemblyTask body = new AssemblyTask("sedan", "Assemble a sedan body", TaskType.BODY, 0);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<AssemblyTask> bodyTasks = new ArrayList<AssemblyTask>();
		bodyTasks.add(color);
		bodyTasks.add(body);
		
		List<AssemblyTask> drivetrainTasks = new ArrayList<AssemblyTask>();
		bodyTasks.add(engine);
		
		Mockito.when(procedure.getAssemblyTasks(TaskType.BODY)).thenReturn(bodyTasks);
		Mockito.when(procedure.getAssemblyTasks(TaskType.DRIVETRAIN)).thenReturn(drivetrainTasks);
		
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
