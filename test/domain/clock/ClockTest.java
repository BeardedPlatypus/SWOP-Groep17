/**
 * 
 */
package domain.clock;

import static org.junit.Assert.*;

import java.util.PriorityQueue;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import domain.DateTime;
import domain.clock.TimeObserver;

/**
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class ClockTest {
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
	
	@Mock EventActor actor1;
	@Mock EventActor actor2;
	@Mock EventActor actor3;
	
	Clock clock;
	//--------------------------------------------------------------------------
	// Setup Methods
	//--------------------------------------------------------------------------
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		clock = new Clock(new DateTime(0, 6, 0));
		
		clock.register(actor1);
		clock.register(actor2);
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
		new Clock(null);
	}
	
	/**
	 * Test with non-default DateTime init.
	 */
	@Test
	public void testClockManagerDateTimeStartTime() {
		Clock cm = new Clock(t1);
		
		assertEquals(t1, cm.getCurrentTime());
		assertTrue(cm.getTimeObservers().isEmpty());
	}
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// TimeSubject methods.
	//--------------------------------------------------------------------------
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#attachTimeObserver(domain.clock.TimeObserver)}.
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
	 * Test method for {@link domain.productionSchedule.ClockManager#attachTimeObserver(domain.clock.TimeObserver)}.
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
	 * Test method for {@link domain.productionSchedule.ClockManager#attachTimeObserver(domain.clock.TimeObserver)}.
	 */
	@Test
	public void testAttachTimeObserverNull() {
		exception.expect(IllegalArgumentException.class);
		assertTrue(clock.getTimeObservers().isEmpty());
		
		clock.attachTimeObserver(null);
	}	

	//--------------------------------------------------------------------------
	/**
	 * Test method for {@link domain.productionSchedule.ClockManager#detachTimeObserver(domain.clock.TimeObserver)}.
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
	
	@Test
	public void setTimeNotifyTest() {
		try {
			Whitebox.invokeMethod(clock, "setCurrentTime", new DateTime(0, 9, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (TimeObserver obs : clock.getTimeObservers()) {
			Mockito.verify(obs).update(new DateTime(0, 9, 0));
		}
	}
	
	//--------------------------------------------------------------------------
	// Event queue methods
	//--------------------------------------------------------------------------
	@Test
	public void register_null() {
		exception.expect(IllegalArgumentException.class);
		clock.register(null);
	}
	
	@Test
	public void register_valid() {
		clock.register(actor3);
		Set<EventActor> registeredActors = Whitebox.getInternalState(clock, "registeredActors");
		assertTrue(registeredActors.contains(actor3));
	}
	
	@Test
	public void constructEvent_nullDateTime() {
		exception.expect(IllegalArgumentException.class);
		clock.constructEvent(null, actor1);
	}
	
	@Test
	public void constructEvent_nullActor() {
		exception.expect(IllegalArgumentException.class);
		clock.constructEvent(new DateTime(0, 9, 0), null);
	}
	
	@Test
	public void constructEvent_notRegisteredActor() {
		exception.expect(IllegalArgumentException.class);
		clock.constructEvent(new DateTime(0, 9, 0), actor3);
	}
	
	@Test
	public void constructEvent_valid() {
		clock.constructEvent(new DateTime(0, 9, 0), actor1);
		PriorityQueue<TimeEvent> queue = Whitebox.getInternalState(clock, "eventQueue");
		TimeEvent peeked = queue.peek();
		assertTrue(peeked.compareTo(new TimeEvent(new DateTime(0, 15, 0), actor1)) == 0);
	}
	
	@Test
	public void constructEvent_allRegisteredActors() {
		clock.constructEvent(new DateTime(0, 9, 0), actor1);
		clock.constructEvent(new DateTime(0, 10, 0), actor2);
		Mockito.verify(actor1).activate();
		Mockito.verify(actor2, Mockito.never()).activate();
		assertEquals(new DateTime(0, 15, 0), clock.getCurrentTime());
	}
	
	@Test
	public void constructEvent_allRegisteredActorsFireAll() {
		clock.constructEvent(new DateTime(0, 9, 0), actor1);
		clock.constructEvent(new DateTime(0, 9, 0), actor2);
		Mockito.verify(actor1).activate();
		Mockito.verify(actor2).activate();
		assertEquals(new DateTime(0, 15, 0), clock.getCurrentTime());
	}
	
	@Test
	public void constructEvent_checkHeadQueue() {
		clock.register(actor3);
		clock.constructEvent(new DateTime(0, 9, 0), actor1);
		clock.constructEvent(new DateTime(0, 10, 0), actor2);
		PriorityQueue<TimeEvent> queue = Whitebox.getInternalState(clock, "eventQueue");
		assertEquals(new DateTime(0, 15, 0), queue.peek().getGlobalTime());
	}
	
	@Test
	public void constructEvent_multipleFromSameActor() {
		clock.constructEvent(new DateTime(0, 9, 0), actor1);
		exception.expect(IllegalArgumentException.class);
		clock.constructEvent(new DateTime(0, 8, 0), actor1);
	}
	
	@Test
	public void unregister_nullActor() {
		exception.expect(IllegalArgumentException.class);
		clock.unregister(null);
	}
	
	@Test
	public void constructEvent_unregister() {
		clock.register(actor3);
		clock.constructEvent(new DateTime(0, 10, 0), actor1);
		clock.constructEvent(new DateTime(0, 9, 0), actor2);
		
		clock.unregister(actor2);
		Set<EventActor> registeredActors = Whitebox.getInternalState(clock, "registeredActors");
		assertFalse(registeredActors.contains(actor2));
		PriorityQueue<TimeEvent> eventQueue = Whitebox.getInternalState(clock, "eventQueue");
		assertTrue(eventQueue.size() < 2);
		TimeEvent peeked = eventQueue.peek();
		EventActor peekedActor = Whitebox.getInternalState(peeked, EventActor.class);
		assertFalse(peekedActor == actor2);
	}
	
	@Test
	public void removeEventForActor_noEvent() {
		exception.expect(IllegalArgumentException.class);
		clock.removeEventForActor(actor1);
	}
	
	@Test
	public void removeEventForActor_legit() {
		clock.constructEvent(new DateTime(0, 9, 0), actor1);
		clock.removeEventForActor(actor1);
		PriorityQueue<TimeEvent> eventQueue = Whitebox.getInternalState(clock, "eventQueue");
		assertTrue(eventQueue.isEmpty());
	}
}
