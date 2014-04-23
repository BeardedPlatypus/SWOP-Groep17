package domain.handlers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.Manufacturer;
import domain.handlers.CheckProductionStatisticsHandler;

public class CheckProductionStatisticsHandlerTest {
	
	@Rule public ExpectedException expected = ExpectedException.none();
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
	public void constructor_nullManufacturer() {
		expected.expect(IllegalArgumentException.class);
		new CheckProductionStatisticsHandler(null);
	}

	@Test
	public void getStatisticsReportTest() {
		handler.getStatisticsReport();
		Mockito.verify(manufacturer).getStatisticsReport();
	}

}
