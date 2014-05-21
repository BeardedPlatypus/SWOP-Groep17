package domain.clock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import com.google.common.base.Optional;

import domain.DateTime;

public class TimeEventTest {

	@Rule public ExpectedException expected = ExpectedException.none();
	
	@Mock EventActor actor;
	@Mock EventActor otherActor;
	
	DateTime oneTime;
	DateTime otherTime;
	DateTime earlierTime;
	
	TimeEvent oneEvent;
	TimeEvent otherEvent;
	TimeEvent laterEvent;
	TimeEvent earlierEvent;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		oneTime = new DateTime(0, 9, 0);
		otherTime = new DateTime(0, 12, 0);
		earlierTime = new DateTime(0, 6, 0);
		
		oneEvent = new TimeEvent(oneTime, actor);
		otherEvent = new TimeEvent(otherTime, otherActor);
		laterEvent = new TimeEvent(otherTime, actor);
		earlierEvent = new TimeEvent(earlierTime, actor);
	}

	@Test
	public void constructor_nullDateTime() {
		expected.expect(IllegalArgumentException.class);
		new TimeEvent(null, actor);
	}
	
	@Test
	public void constructor_nullActor() {
		expected.expect(IllegalArgumentException.class);
		new TimeEvent(oneTime, null);
	}
	
	@Test
	public void constructor_valid() {
		TimeEvent event = new TimeEvent(oneTime, actor);
		assertEquals(oneTime, event.getGlobalTime());
		EventActor fromEventActor = Whitebox.getInternalState(event, "actor");
		assertEquals(actor, fromEventActor);
	}
	
	@Test
	public void activate_test() {
//		TimeEvent spiedEvent = Mockito.spy(oneEvent);
//		spiedEvent.activate();
		oneEvent.activate();
		Mockito.verify(actor).activate();
	}
	
	@Test
	public void overrides_True() {
		assertTrue(oneEvent.overridesEvent(laterEvent));
		assertTrue(earlierEvent.overridesEvent(oneEvent));
	}
	
	@Test
	public void overrides_False() {
		assertFalse(oneEvent.overridesEvent(otherEvent));
		assertFalse(otherEvent.overridesEvent(oneEvent));
		assertFalse(oneEvent.overridesEvent(oneEvent));
		assertFalse(oneEvent.overridesEvent(earlierEvent));
	}
	
	@Test
	public void hasActor_true() {
		assertTrue(oneEvent.hasActor(actor));
	}
	
	@Test
	public void hasActor_false() {
		assertFalse(oneEvent.hasActor(otherActor));
	}
	
	@Test
	public void compareTo_smaller() {
		assertTrue(oneEvent.compareTo(otherEvent) < 0);
	}
	
	@Test
	public void compareTo_equal() {
		TimeEvent sameTime = new TimeEvent(oneTime, otherActor);
		assertEquals(0, oneEvent.compareTo(sameTime));
	}
	
	@Test
	public void compareTo_greater() {
		assertTrue(oneEvent.compareTo(earlierEvent) > 0);
	}

}
