package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CheckProductionStatisticsHandlerTest {
	
	CheckProductionStatisticsHandler handler;
	
	@Mock Manufacturer manufacturer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		handler = new CheckProductionStatisticsHandler(manufacturer);
	}

	@Test
	public void getStatisticsReportTest() {
		handler.getStatisticsReport();
		Mockito.verify(manufacturer).getStatisticsReport();
	}

}
