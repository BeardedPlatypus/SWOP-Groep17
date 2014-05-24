package scenario;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.google.common.base.Optional;

import domain.assembly_line.AssemblyLine;
import domain.assembly_line.AssemblyLineFacade;
import domain.assembly_line.AssemblyLineStateView;
import domain.assembly_line.AssemblyLineView;
import domain.assembly_line.AssemblyProcedure;
import domain.assembly_line.WorkPost;
import domain.handlers.ChangeOperationalStatusHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;
import domain.initialdata.InitialDataLoader;

public class ChangeOperationalStatusScenario {

	ChangeOperationalStatusHandler handler;
	DomainFacade facade;
	InitialDataLoader loader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		loader = init.getInitialDataLoader();
		facade = init.getDomainFacade();
		handler = facade.getChangeOperationalStatusHandler();
	}

	@Test
	public void changeState_broken() {
		// Step 1: the user wants to change the operational status of an assembly line
		// Step 2: The system shows the available statuses (operational, maintenance, broken), as well as the current operational status.
		List<AssemblyLineStateView> states = handler.getAssemblyLineStates();
		assertEquals(3, states.size());
		List<AssemblyLineStateView> available = handler.getAvailableStates();
		assertEquals(3, available.size());
		// Step 3: The user selects the new operational status to be used.
		// Step 4: The system applies the new operational status of the assembly line
		int index = 0;
		for (int i = 0; i < available.size(); i++) {
			if (available.get(i).getName().equals("Broken")) {
				index = i;
				break;
			}
		}
		handler.setAssemblyLineState(0, index);
		loader.placeIdenticalStandardOrder(3);
		loader.completeAllTasksOnAssemblyLine(0, 20, 1);
		loader.completeAllTasksOnAssemblyLine(1, 20, 1);
		loader.completeAllTasksOnAssemblyLine(2, 20, 1);
		loader.placeIdenticalStandardOrder(3);
		states = handler.getAssemblyLineStates();
		assertEquals("Broken", states.get(0).getName());
		}
	
	@Test
	public void changeState_maintenance() {
		List<AssemblyLineView> lines = facade.getAssemblyLineStatusHandler().getLineViews();
		AssemblyLineFacade line = (AssemblyLineFacade) lines.get(0);
		try {
			WorkPost workPost = Whitebox.invokeMethod(line, "getWorkPost", 0);
			Optional<AssemblyProcedure> proc = Optional.absent();
			Whitebox.invokeMethod(workPost, "setAssemblyProcedure", proc);
			List<AssemblyLineStateView> available = handler.getAvailableStates();
			int index = 0;
			for (int i = 0; i < available.size(); i++) {
				if (available.get(i).getName().equals("In maintenance")) {
					index = i;
					break;
				}
			}
			handler.setAssemblyLineState(0, index);
			loader.placeIdenticalStandardOrder(3);
			loader.completeAllTasksOnAssemblyLine(0, 20, 1);
			loader.completeAllTasksOnAssemblyLine(1, 20, 1);
			loader.completeAllTasksOnAssemblyLine(2, 20, 1);
			List<AssemblyLineStateView> states = handler.getAssemblyLineStates();
			assertEquals("In maintenance", states.get(0).getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
