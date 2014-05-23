package domain.statistics;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.order.Order;
import domain.statistics.ProcedureStatistics;


public class ProcedureStatisticsTest {

	@Mock Order order;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getDelayTest() {
		ProcedureStatistics stats = new ProcedureStatistics(100, order);
		assertEquals(stats.getDelay(), 100);
		assertEquals(stats.getCompletedOrder(), order);
	}

}
