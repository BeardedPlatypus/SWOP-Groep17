package domain.statistics;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.statistics.WorkingDay;


public class WorkingDayTest {

	@Rule public ExpectedException exception = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void constructor_negativeDay() {
		exception.expect(IllegalArgumentException.class);
		new WorkingDay(-100);
	}
	
	@Test
	public void constructor_valid() {
		WorkingDay day = new WorkingDay(10);
		assertEquals(day.getDayNumber(), 10);
	}

}
