package scenario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import domain.assembly_line.AssemblyLineView;
import domain.handlers.AssemblyLineStatusHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

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
		// including pending and finished tasks at each work post.
		// --- Only getting the information is tested, formatting is the responsibility 
		// of the UI ---
		AssemblyLineView view = handler.getLineViews().get(0);
		assertEquals(3, view.getWorkPostViews().size());
		assertFalse(view.getWorkPostViews().get(0).isEmpty());
		assertTrue(view.getWorkPostViews().get(1).isEmpty());
		assertTrue(view.getWorkPostViews().get(2).isEmpty());
		assertFalse(view.getWorkPostViews().get(0).isFinished());
	}

}
