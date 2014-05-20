/**
 * 
 */
package domain.productionSchedule;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.DateTime;

/**
 * @author Martinus Wilhelmus Tegelaers
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
	
	@Mock TimeObserver to1;
	@Mock TimeObserver to2;
	@Mock TimeObserver to3;
	
	ClockManager clock;
	//--------------------------------------------------------------------------
	// Setup Methods
	//--------------------------------------------------------------------------
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
		assertTrue(cm.getTimeObservers().isEmpty());
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
		assertTrue(cm.getTimeObservers().isEmpty());
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
		assertTrue(clock.getTimeObservers().isEmpty());
		
		clock.attachTimeObserver(to1);
		assertEquals(1, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));

		clock.attachTimeObserver(to2);
		assertEquals(2, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));

		clock.attachTimeObserver(to3);
		assertEquals(3, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));
		assertTrue(clock.getTimeObservers().contains(to3));
	}
	
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#attachTimeObserver(domain.productionSchedule.TimeObserver)}.
	 */
	@Test
	public void testAttachTimeObserverSame() {
		assertTrue(clock.getTimeObservers().isEmpty());
		
		clock.attachTimeObserver(to1);
		assertEquals(1, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));

		clock.attachTimeObserver(to1);
		assertEquals(1, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));

		clock.attachTimeObserver(to1);
		assertEquals(1, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
	}
	
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#attachTimeObserver(domain.productionSchedule.TimeObserver)}.
	 */
	@Test
	public void testAttachTimeObserverNull() {
		exception.expect(IllegalArgumentException.class);
		assertTrue(clock.getTimeObservers().isEmpty());
		
		clock.attachTimeObserver(null);
	}	

	//--------------------------------------------------------------------------
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#detachTimeObserver(domain.productionSchedule.TimeObserver)}.
	 */
	@Test
	public void testDetachTimeObserverNullEmpty() {
		exception.expect(IllegalArgumentException.class);		
		
		clock.attachTimeObserver(null);		
	}

	@Test
	public void testDetachTimeObserverNullNonEmpty() {
		exception.expect(IllegalArgumentException.class);
		
		clock.attachTimeObserver(to1);
		clock.attachTimeObserver(to2);
		clock.attachTimeObserver(to3);
		
		clock.attachTimeObserver(null);		
	}
	
	@Test
	public void testDetachTimeObserverNotExisting() {
		clock.attachTimeObserver(to1);
		clock.attachTimeObserver(to2);
		
		assertEquals(2, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));
		assertFalse(clock.getTimeObservers().contains(to3));
		
		clock.detachTimeObserver(to3);
		
		assertEquals(2, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));
		assertFalse(clock.getTimeObservers().contains(to3));		
	}

	@Test
	public void testDetachTimeObserverNormal() {
		//SETUP
		//----------------------------------------------------------------------
		assertTrue(clock.getTimeObservers().isEmpty());
		
		clock.attachTimeObserver(to1);
		assertEquals(1, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));

		clock.attachTimeObserver(to2);
		assertEquals(2, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));

		clock.attachTimeObserver(to3);
		assertEquals(3, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));
		assertTrue(clock.getTimeObservers().contains(to3));
		
		//----------------------------------------------------------------------
		// Actual tests
		
		clock.detachTimeObserver(to3);
		assertEquals(2, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertTrue(clock.getTimeObservers().contains(to2));
		assertFalse(clock.getTimeObservers().contains(to3));

		clock.detachTimeObserver(to2);
		assertEquals(1, clock.getTimeObservers().size());
		assertTrue(clock.getTimeObservers().contains(to1));
		assertFalse(clock.getTimeObservers().contains(to2));
		assertFalse(clock.getTimeObservers().contains(to3));

		clock.detachTimeObserver(to1);
		assertEquals(0, clock.getTimeObservers().size());
		assertFalse(clock.getTimeObservers().contains(to1));
		assertFalse(clock.getTimeObservers().contains(to2));
		assertFalse(clock.getTimeObservers().contains(to3));
	}
	
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#notifyTime()}.
	 */
	@Test
	public void testNotifyTime() {
		// Spy on clock
		ClockManager spiedNotifyClock = Mockito.spy(clock);
		
		// Get current and future time
		DateTime time = spiedNotifyClock.getCurrentTime();
		DateTime newTime = time.addTime(0, 0, 5);
		
		// Attach observers
		spiedNotifyClock.attachTimeObserver(to1);
		spiedNotifyClock.attachTimeObserver(to2);
		spiedNotifyClock.attachTimeObserver(to3);
		
		// Check initial update
		Mockito.verify(to1).update(time);
		Mockito.verify(to2).update(time);
		Mockito.verify(to3).update(time);
		
		// Change time and check if notifyTime has been called.
		spiedNotifyClock.update(new DateTime(0, 0, 5));
		Mockito.verify(spiedNotifyClock).notifyTime();
		
		// Verify notifyTime has updated observers
		Mockito.verify(to1).update(newTime);
		Mockito.verify(to2).update(newTime);
		Mockito.verify(to3).update(newTime);
	}

	//--------------------------------------------------------------------------
	// IncrementTimeObserver methods.
	//--------------------------------------------------------------------------
	//FIXME
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#update(domain.DateTime)}.
	 */
	@Test
	public void testUpdate() {
		ClockManager spiedClock = Mockito.spy(clock);
		
		spiedClock.update(t1);
		Mockito.verify(spiedClock).incrementTime(t1);
	}
}
