package scenario;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.google.common.base.Optional;

import domain.assembly_line.AssemblyLine;
import domain.assembly_line.AssemblyLineStateView;
import domain.assembly_line.AssemblyLineView;
import domain.assembly_line.AssemblyProcedure;
import domain.assembly_line.WorkPost;
import domain.handlers.ChangeOperationalStatusHandler;
import domain.handlers.DomainFacade;
import domain.handlers.InitialisationHandler;

public class ChangeOperationalStatusScenario {

	ChangeOperationalStatusHandler handler;
	DomainFacade facade;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
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
		states = handler.getAssemblyLineStates();
		assertEquals("Broken", states.get(0));
		}
	
	@Test
	public void changeState_maintenance() {
		List<AssemblyLineView> lines = facade.getAssemblyLineStatusHandler().getLineViews();
		AssemblyLine line = (AssemblyLine) lines.get(0);
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
			List<AssemblyLineStateView> states = handler.getAssemblyLineStates();
			assertEquals("In maintenance", states.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
