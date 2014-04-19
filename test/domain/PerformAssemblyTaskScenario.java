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

public class PerformAssemblyTaskScenario {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock ProductionSchedule schedule;
	@Mock Order order;
	
	AssemblyLine assemblyLine;
	
	WorkPost workPost1;
	WorkPost workPost2;
	
	AssemblyProcedure procedure1;
	AssemblyProcedure procedure2;
	
	AssemblyTask taskBody1;
	AssemblyTask taskBody2;
	AssemblyTask taskDriveTrain1;
	AssemblyTask taskDriveTrain2;
	
	PerformAssemblyTaskHandler handler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

	assemblyLine = Mockito.spy(new AssemblyLine(schedule));
	
	this.handler = new PerformAssemblyTaskHandler(this.assemblyLine);
	this.taskBody1 = new AssemblyTask("john", "doe", TaskType.BODY, 0);
	this.taskBody2 = new AssemblyTask("alice", "johnson", TaskType.BODY, 1);
	this.taskDriveTrain1 = new AssemblyTask("jane", "doe", TaskType.DRIVETRAIN, 0);
	this.taskDriveTrain2 = new AssemblyTask("bob", "smith", TaskType.DRIVETRAIN, 1);
	
	List<AssemblyTask> tasksBody = new ArrayList<AssemblyTask>();
	List<AssemblyTask> tasksDriveTrain = new ArrayList<AssemblyTask>();
	tasksBody.add(taskBody1);
	tasksBody.add(taskBody2);
	tasksDriveTrain.add(taskDriveTrain1);
	tasksDriveTrain.add(taskDriveTrain2);
	
	workPost1 = assemblyLine.getWorkPost(0);
	workPost2 = assemblyLine.getWorkPost(1);
	
	procedure1 = Mockito.spy(new AssemblyProcedure(order, tasksBody));
	procedure2 = Mockito.spy(new AssemblyProcedure(order, tasksDriveTrain));
	workPost1.setAssemblyProcedure(procedure1);
	workPost2.setAssemblyProcedure(procedure2);
	}

	@Test
	public void scenarioTest_normal() {
		/*
		 * Step 1: The system asks which work post the user is residing at.
		 * Step 2: The user selects the corresponding work post.
		 * 
		 * !! The above are the responsibility of the UI !!
		 * 
		 * Step 3: The system presents an overview of the pending assembly tasks
		 * for the car at the current work post.
		 */
		List<AssemblyTaskContainer> tasks = handler.getAssemblyTasksAtPost(0);
		assertEquals(tasks.get(0), taskBody1);
		assertEquals(tasks.get(1), taskBody2);
		assertFalse(taskBody1.isCompleted());
		assertFalse(taskBody2.isCompleted());
		/*
		 * Step 4: The user selects one of the assembly tasks.
		 * Step 5: The system shows the assembly task information, including
		 * the sequence of actions to perform.
		 * !! The above are the responsibility of the UI !!
		 * 
		 * Step 6: The user performs the assembly tasks and indicates
		 * when the assembly task is finished.
		 */
		handler.completeWorkpostTask(0, 0);
		assertTrue(taskBody1.isCompleted());
		assertFalse(taskBody2.isCompleted());
		assertFalse(taskDriveTrain1.isCompleted());
		assertFalse(taskDriveTrain2.isCompleted());
		/* Step 7: The system stores the changes and presents an updated overview
		 * of pending assembly tasks for the car at the current work post.
		 * 
		 * !! The above is the responsibility of the UI !!
		 */
		handler.completeWorkpostTask(0, 1);
		assertTrue(taskBody1.isCompleted());
		assertTrue(taskBody2.isCompleted());
		assertFalse(taskDriveTrain1.isCompleted());
		assertFalse(taskDriveTrain2.isCompleted());
		// Exiting the overview is the responsibility of the user interface.
		// Use case ends here.
	}

}
