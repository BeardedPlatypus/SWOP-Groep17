package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.statistics.StatisticsLogger;

public class TimeManagerTest {

	TimeManager timeManager;
	
	@Mock StatisticsLogger statisticsLogger;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		timeManager = new TimeManager();
		timeManager.register(statisticsLogger);
	}

	@Test
	public void addTimeTest() {
		DateTime current = timeManager.getCurrentTime();
		DateTime later = current.addTime(1, 1, 1);
		
		timeManager.addTime(new DateTime(1, 1, 1));
		assertEquals(later, timeManager.getCurrentTime());
		
		Mockito.verify(statisticsLogger).update(later);
	}

}
