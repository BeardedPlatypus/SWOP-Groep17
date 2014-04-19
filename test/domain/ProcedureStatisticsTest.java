package domain;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ProcedureStatisticsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getDelayTest() {
		ProcedureStatistics stats = new ProcedureStatistics(100);
		assertEquals(stats.getDelay(), 100);
	}

}
