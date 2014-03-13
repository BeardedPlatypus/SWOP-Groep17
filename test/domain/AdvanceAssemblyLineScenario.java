package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AdvanceAssemblyLineScenario {

	@Rule public ExpectedException exception = ExpectedException.none();
	
	AdvanceAssemblyLineHandler advanceHandler;
	
	@Before
	public void setUp() throws Exception {
		InitialisationHandler init = new InitialisationHandler();
		advanceHandler = init.getAdvanceHandler();
	}
	
	@Test
	public void normalFlowInitial_test() {
		//1. The user indicates he wants to advance the assembly line.
		
		//2. The system presents an overview of the current assembly line status,
		//as well as a view of the future assembly line status (as it would be after completing this use case),
		//including pending and finished tasks at each work post.
		advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		advanceHandler.getFutureWorkpostsAndActiveAssemblies();
		//3. The user confirms the decision to move the assembly line forward, 
		//and enters the time that was spent during the current phase (e.g. 45 minutes instead of the scheduled hour).
		advanceHandler.tryAdvance(45);
		//4. The system moves the assembly line forward one work post according to the scheduling rules.
		//5. The system presents an overview of the new assembly line status.
		advanceHandler.getCurrentWorkpostsAndActiveAssemblies();
		//6. The user indicates he is done viewing the status.
		
	}

}
