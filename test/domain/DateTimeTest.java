package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DateTimeTest {

	DateTime sample111DateTime;
	
	@Before
	public void setUp() throws Exception {
		sample111DateTime = new DateTime(1, 1, 1);
	}

	@Test
	public void progressTimeTest() {
		DateTime currentDateTime = sample111DateTime;
		assertEquals(currentDateTime.getDays(), 1);
		assertEquals(currentDateTime.getHours(), 1);
		assertEquals(currentDateTime.getMinutes(), 1);
		currentDateTime = currentDateTime.progressTimeBy(3, 2, 4);
		assertEquals(currentDateTime.getDays(), 4);
		assertEquals(currentDateTime.getHours(), 3);
		assertEquals(currentDateTime.getMinutes(), 5);
		currentDateTime = currentDateTime.progressTimeBy(0, 0, 59);
		assertEquals(currentDateTime.getDays(), 4);
		assertEquals(currentDateTime.getHours(), 4);
		assertEquals(currentDateTime.getMinutes(), 4);
		currentDateTime = currentDateTime.progressTimeBy(0, 22, 58);
		assertEquals(currentDateTime.getDays(), 5);
		assertEquals(currentDateTime.getHours(), 3);
		assertEquals(currentDateTime.getMinutes(), 2);
		currentDateTime = currentDateTime.progressTimeBy(16, 23, 59);
		assertEquals(currentDateTime.getDays(), 22);
		assertEquals(currentDateTime.getHours(), 3);
		assertEquals(currentDateTime.getMinutes(), 1);
		currentDateTime = currentDateTime.progressTimeBy(-1, -1, -1);
		assertEquals(currentDateTime.getDays(), 21);
		assertEquals(currentDateTime.getHours(), 2);
		assertEquals(currentDateTime.getMinutes(), 0);
		currentDateTime = currentDateTime.progressTimeBy(0, 0, -1);
		assertEquals(currentDateTime.getDays(), 21);
		assertEquals(currentDateTime.getHours(), 1);
		assertEquals(currentDateTime.getMinutes(), 59);
		
		
	}

}
