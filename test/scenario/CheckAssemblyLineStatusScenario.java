package scenario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import domain.Manufacturer;
import domain.assembly_line.AssemblyLine;
import domain.assembly_line.AssemblyProcedure;
import domain.assembly_line.AssemblyTask;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.TaskType;
import domain.assembly_line.WorkPost;
import domain.assembly_line.WorkPostView;
import domain.car.Option;
import domain.handlers.AssemblyLineStatusHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
import domain.order.Order;

@RunWith(PowerMockRunner.class)
public class CheckAssemblyLineStatusScenario {
	
	AssemblyLineStatusHandler handler;
	InitialisationHandler init;
	DomainFacade facade;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		init = new InitialisationHandler();
		facade = init.getDomainFacade();
		handler = facade.getAssemblyLineStatusHandler();
	}

	@Test
	public void normal_flow() {
		// Step 1: The user wants to check the current status of the assembly line
		// --- Responsibility of the UI ---
		// Step 2: The system presents an overview of the current assembly line status,
		// including pending and nished tasks at each work post.
		// --- Only getting the information is tested, formatting is the responsibility 
		// of the UI ---
		assertEquals(3, handler.getAmountOfWorkPosts());
		List<AssemblyTaskView> tasks;
		List<WorkPostView> containers = handler.getWorkPosts();
		assertEquals(handler.getWorkPost(0), containers.get(0));
		assertFalse(workPosts.get(0).isFinished());
		assertTrue(workPosts.get(1).isEmpty());
		assertFalse(workPosts.get(2).isFinished());
		assertEquals(2, handler.getAmountOfTasksAtWorkPost(0));
		tasks = handler.getTasksAtWorkPost(0);
		assertEquals(tasks.get(0), task1);
		assertEquals(tasks.get(1), task2);
		assertTrue(tasks.get(0).isCompleted());
		assertFalse(tasks.get(1).isCompleted());
		
		tasks = handler.getTasksAtWorkPost(2);
		assertEquals(tasks.get(0), task3);
		assertEquals(tasks.get(1), task4);
		assertTrue(tasks.get(0).isCompleted());
		assertFalse(tasks.get(1).isCompleted());
	}

}
