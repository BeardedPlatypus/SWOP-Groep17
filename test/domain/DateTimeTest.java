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
	public void test_constructorDateTime() throws Exception {
		DateTime test1 = new DateTime(0, 0, 0);
		
		assertEquals(0, test1.days);
		assertEquals(0, test1.hours);
		assertEquals(0, test1.minutes);
		
		DateTime test2 = new DateTime(20, 5, 6);
		
		assertEquals(20, test2.days);
		assertEquals(5, test2.hours);
		assertEquals(6, test2.minutes);

		DateTime test3 = new DateTime(51, 2, 0);
		
		assertEquals(51, test3.days);
		assertEquals(2, test3.hours);
		assertEquals(0, test3.minutes);

	}
	
	@Test
	public void test_constructorDateTimeNegativeInputIllegalArgument() {
		
	}
	
	@Test
	public void progressTimeTest() {
		DateTime currentDateTime = sample111DateTime;
		assertEquals(currentDateTime.getDays(), 1);
		assertEquals(currentDateTime.getHours(), 1);
		assertEquals(currentDateTime.getMinutes(), 1);
		currentDateTime = currentDateTime.addTime(3, 2, 4);
		assertEquals(currentDateTime.getDays(), 4);
		assertEquals(currentDateTime.getHours(), 3);
		assertEquals(currentDateTime.getMinutes(), 5);
		currentDateTime = currentDateTime.addTime(0, 0, 59);
		assertEquals(currentDateTime.getDays(), 4);
		assertEquals(currentDateTime.getHours(), 4);
		assertEquals(currentDateTime.getMinutes(), 4);
		currentDateTime = currentDateTime.addTime(0, 22, 58);
		assertEquals(currentDateTime.getDays(), 5);
		assertEquals(currentDateTime.getHours(), 3);
		assertEquals(currentDateTime.getMinutes(), 2);
		currentDateTime = currentDateTime.addTime(16, 23, 59);
		assertEquals(currentDateTime.getDays(), 22);
		assertEquals(currentDateTime.getHours(), 3);
		assertEquals(currentDateTime.getMinutes(), 1);
	}

}
