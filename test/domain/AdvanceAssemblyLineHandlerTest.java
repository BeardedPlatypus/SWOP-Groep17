package domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AdvanceAssemblyLineHandlerTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	

	@Before
	public void setUp() throws Exception {
	}

//	@Test
//	public void testAdvanceAssemblyLineHandler() {
//		List<AssemblyProcedureContainer> posts = new ArrayList<>();
//		AssemblyLine line = Mockito.mock(AssemblyLine.class);
//		Mockito.when(line.getCurrentWorkPostsAndActiveAssemblies()).thenReturn(posts);
//		AdvanceAssemblyLineHandler testHandler = new AdvanceAssemblyLineHandler(line);
//		assertTrue(testHandler.getCurrentWorkPostsAndActiveAssemblies().equals(posts));
//	}

	@Test
	public void constructorNullExceptionTest() {
		exception.expect(IllegalArgumentException.class);
		new AdvanceAssemblyLineHandler(null);
	}


}
