package domain.assemblyLine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StateCatalogTest {

	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock AssemblyLine assemblyLine;
	StateCatalog catalog;
	
	OperationalState opState;
	MaintenanceState mainState;
	BrokenState brokenState;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		catalog = new StateCatalog();
		
		opState = new OperationalState();
		mainState = new MaintenanceState();
		brokenState = new BrokenState();
		
		catalog.addToAvailableStates(opState);
		catalog.addToAvailableStates(mainState);
		catalog.addToAvailableStates(brokenState);
	}

	@Test
	public void addToAvailableStates_test() {
		catalog.addToAvailableStates(new ActiveState());
		assertTrue(catalog.getAvailableStates().contains(new ActiveState()));
	}
	
	@Test
	public void addToAvailableStates_duplicate() {
		expected.expect(IllegalArgumentException.class);
		catalog.addToAvailableStates(new OperationalState());
	}
	
	@Test
	public void getState_negativeIndex() {
		expected.expect(IllegalArgumentException.class);
		catalog.getStateInstance(-1);
	}
	
	@Test
	public void getState_tooBigIndex() {
		expected.expect(IllegalArgumentException.class);
		catalog.getStateInstance(Integer.MAX_VALUE);
	}
	
	@Test
	public void getState_valid() {
		AssemblyLineState state = catalog.getStateInstance(0);
		assertEquals(new OperationalState(), state);
	}
}
