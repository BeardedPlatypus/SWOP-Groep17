/**
 * 
 */
package domain.productionSchedule;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;

import domain.DateTime;

/**
 * @author Month
 *
 */
public class ClockManagerTest {
	//--------------------------------------------------------------------------
	// Variables
	//--------------------------------------------------------------------------
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock DateTime t1;
	@Mock DateTime t2;
	@Mock DateTime t3;
	
	ClockManager clock;
	//--------------------------------------------------------------------------
	// Setup Methods
	//--------------------------------------------------------------------------
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		clock = new ClockManager();
		
	}

	//--------------------------------------------------------------------------
	// basic functionality tests
	//--------------------------------------------------------------------------
	/**
	 * Test if null is not accepted in constructor.
	 */
	@Test
	public void testClockManagerDateTimeNull() {
		exception.expect(IllegalArgumentException.class);
		new ClockManager(null);
	}
	
	/**
	 * Test with non-default DateTime init.
	 */
	@Test
	public void testClockManagerDateTimeStartTime() {
		ClockManager cm = new ClockManager(t1);
		
		assertEquals(t1, cm.getCurrentTime());
	}
	
	/**
	 * Test with default DateTime init.
	 */
	@Test
	public void testClockManagerDateTimeNoArg() {
		ClockManager cm = new ClockManager();
		DateTime t = cm.getCurrentTime();

		assertEquals(0, t.getDays());
		assertEquals(6, t.getHours());
		assertEquals(0, t.getMinutes());
	}
	//--------------------------------------------------------------------------
	
	
	/**
	 * Test increment time with null, should return an IllegalArgumentException.
	 */
	@Test
	public void testIncrementTimeNull() {
		exception.expect(IllegalArgumentException.class);
		clock.incrementTime(null);
	}
	
	/**
	 * Test increment time with valid time value. 
	 */
	@Test
	public void testIncrementTimeValid() {
		ClockManager spiedClock = Mockito.spy(clock);
		spiedClock.incrementTime(new DateTime(0, 4, 10));
		
		DateTime t = spiedClock.getCurrentTime();
		
		assertEquals(0, t.getDays());
		assertEquals(10, t.getHours());
		assertEquals(10, t.getMinutes());
		
		Mockito.verify(spiedClock).notifyTime();
	}

	//--------------------------------------------------------------------------
	// TimeSubject methods.
	//--------------------------------------------------------------------------
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#attachTimeObserver(domain.productionSchedule.TimeObserver)}.
	 */
	@Test
	public void testAttachTimeObserver() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#detachTimeObserver(domain.productionSchedule.TimeObserver)}.
	 */
	@Test
	public void testDetachTimeObserver() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#notifyTime()}.
	 */
	@Test
	public void testNotifyTime() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#update(domain.DateTime)}.
	 */
	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

}
