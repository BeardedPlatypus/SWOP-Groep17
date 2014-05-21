package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.clock.TimeObserver;
import domain.productionSchedule.ClockManager;
import domain.statistics.StatisticsLogger;

public class ClockManagerTest {
	
	ClockManager clockManager;
	
	@Mock StatisticsLogger statisticsLogger;
	@Mock TimeObserver timeObserver;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		clockManager = new ClockManager();
		clockManager.attachTimeObserver(timeObserver);
	}

	@Test
	public void addTimeTest() {
		DateTime current = clockManager.getCurrentTime();
		DateTime later = current.addTime(1, 1, 1);
		
		clockManager.update(new DateTime(1,1,1));
		assertEquals(later, clockManager.getCurrentTime());
		
		Mockito.verify(timeObserver).update(later);
	}

}
