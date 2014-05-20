package domain.assemblyLine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BrokenStateTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();

	BrokenState state;
	@Mock AssemblyLine line;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		state = new BrokenState();
		state.setAssemblyLine(line);
	}

	@Test
	public void completeTaskTest() {
		expected.expect(IllegalStateException.class);
		state.completeWorkpostTask(0, 0, 20);
	}
	
	@Test
	public void advanceAssemblyLineTest() {
		expected.expect(IllegalStateException.class);
		state.advanceAssemblyLine();
	}
	
	@Test
	public void popNextOrderFromScheduleTest() {
		expected.expect(IllegalStateException.class);
		state.popNextOrderFromSchedule();
	}
	
	@Test
	public void peekNextOrderFromScheduleTest() {
		expected.expect(IllegalStateException.class);
		state.peekNextOrderFromSchedule();
	}

}
