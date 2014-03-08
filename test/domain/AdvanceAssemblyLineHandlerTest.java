package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AdvanceAssemblyLineHandlerTest {
	
	@Rule public ExpectedException exception = ExpectedException.none();
	

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAdvanceAssemblyLineHandler() {
		List<AssemblyProcedureContainer> posts = new ArrayList<>();
		AssemblyLine line = Mockito.mock(AssemblyLine.class);
		Mockito.when(line.getCurrentActiveAssemblies()).thenReturn(posts);
		AdvanceAssemblyLineHandler testHandler = new AdvanceAssemblyLineHandler(line);
		assertTrue(testHandler.getCurrentActiveAssemblies().equals(posts));
	}

	@Test
	public void constructorNullExceptionTest() {
		exception.expect(IllegalArgumentException.class);
		new AdvanceAssemblyLineHandler(null);
	}


}
