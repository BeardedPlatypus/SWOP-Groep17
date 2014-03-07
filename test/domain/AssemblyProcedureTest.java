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
	@Mock Model model;
	@Mock Specification specs;
	@Mock Option option;
	
	AssemblyProcedure procedure;
	
	AssemblyTask color = new AssemblyTask("blue", "Paint the car blue", TaskType.BODY, 0);
	AssemblyTask engine = new AssemblyTask("4 cilinders", "Install standard 2l 4 cilinders", TaskType.DRIVETRAIN, 0);
	AssemblyTask body = new AssemblyTask("sedan", "Assemble a sedan body", TaskType.BODY, 0);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		procedure = Mockito.spy(new AssemblyProcedure(order));
		
		List<AssemblyTaskContainer> bodyTasks = new ArrayList<AssemblyTaskContainer>();
		bodyTasks.add(color);
		bodyTasks.add(body);
		
		List<AssemblyTaskContainer> drivetrainTasks = new ArrayList<AssemblyTaskContainer>();
		bodyTasks.add(engine);
		
		Mockito.when(procedure.getAssemblyTasks(TaskType.BODY)).thenReturn(bodyTasks);
		Mockito.when(procedure.getAssemblyTasks(TaskType.DRIVETRAIN)).thenReturn(drivetrainTasks);
		
	}
	
	@Test
	public void generateTasks_invalidSpecs() {
		Mockito.when(model.isValidSpecification(specs)).thenReturn(false);
		
	}
	
//	@Test
//	public void constructor_NullOrderTest() {
//		exception.expect(IllegalArgumentException.class);
//		new AssemblyProcedure(null);
//	}
	
	@Test
	public void completeTask_negativeIntTask() {
		
		exception.expect(IllegalArgumentException.class);
		procedure.completeTask(-1, TaskType.BODY);
		
	}
	
//	@Test
//	public void completeTask_TooBigIntTask() {
//		
//	}

}
