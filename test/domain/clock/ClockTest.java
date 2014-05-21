package domain.clock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.DateTime;

public class ClockTest {
	
	@Rule ExpectedException expected = ExpectedException.none();
	
	Clock clock;
	
	@Mock EventActor actor1;
	@Mock EventActor actor2;
	@Mock EventActor actor3;
	@Mock TimeObserver observer;
	
	DateTime time1;
	DateTime time2;
	DateTime time3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		clock = new Clock(new DateTime(0, 0, 0));
	}

	@Test
	public void constructor_nullTime() {
		fail("Not yet implemented");
	}

}
