package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DateTimeTest {

	DateTime sample111DateTime;

	DateTime zero;
	DateTime pos;
	@Before
	public void setUp() throws Exception {
		sample111DateTime = new DateTime(1, 1, 1);
		
		this.zero = new DateTime(0, 0, 0);
		this.pos = new DateTime(10, 10, 10);
	}

	@Test
	public void test_constructorDateTimeValid() throws Exception {
		DateTime test1 = new DateTime(4, 3, 2);
		
		assertEquals(4, test1.days);
		assertEquals(3, test1.hours);
		assertEquals(2, test1.minutes);
		
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
	public void test_constructorDateTimeZero() throws Exception {
		DateTime test = new DateTime(0, 0, 0);
		
		assertEquals(0, test.getDays());
		assertEquals(0, test.getHours());
		assertEquals(0, test.getMinutes());
	}
	
	@Test
	public void test_constructorDateTimeNegativeDays() throws Exception {
		DateTime test1 = new DateTime(-1, 0, 0);
		
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());

		DateTime test2 = new DateTime(-1, 10, 0);
		
		assertEquals(0, test2.getDays());
		assertEquals(0, test2.getHours());
		assertEquals(0, test2.getMinutes());

		DateTime test3 = new DateTime(-1, 30, 0);
		
		assertEquals(0, test3.getDays());
		assertEquals(6, test3.getHours());
		assertEquals(0, test3.getMinutes());
	}
	
	@Test
	public void test_constructorDateTimeNegativeHours() throws Exception {
		DateTime test1 = new DateTime(0, -5, 0);
		
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());

		DateTime test2 = new DateTime(1, -5, 0);
		
		assertEquals(0, test2.getDays());
		assertEquals(19, test2.getHours());
		assertEquals(0, test2.getMinutes());
	}

	@Test
	public void test_constructorDateTimeNegativeMinutes() throws Exception {
		DateTime test1 = new DateTime(0, 0, -10);
		
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());

		DateTime test2 = new DateTime(1, 1, -50);
		
		assertEquals(1, test2.getDays());
		assertEquals(0, test2.getHours());
		assertEquals(10, test2.getMinutes());
	}
	
	@Test
	public void test_constructorDateTimeTooManyHours() throws Exception {
		DateTime test1 = new DateTime(0, 50, 0);
		
		assertEquals(2, test1.getDays());
		assertEquals(2, test1.getHours());
		assertEquals(0, test1.getMinutes());

		DateTime test2 = new DateTime(10, 26, 0);
		
		assertEquals(11, test2.getDays());
		assertEquals(2, test2.getHours());
		assertEquals(0, test2.getMinutes());
	}

	@Test
	public void test_constructorDateTimeTooManyMinutes() throws Exception {
		DateTime test1 = new DateTime(0, 0, 200);
		
		assertEquals(0, test1.getDays());
		assertEquals(3, test1.getHours());
		assertEquals(20, test1.getMinutes());

		DateTime test2 = new DateTime(1, 1, 360);
		
		assertEquals(1, test2.getDays());
		assertEquals(7, test2.getHours());
		assertEquals(0, test2.getMinutes());
	}

	
	
	@Test
	public void test_addTime() {
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
	
	@Test
	public void test_addTimeZero() {
		DateTime test1 = zero.addTime(0,0,0);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.addTime(0,0,0);
		assertEquals(10, test2.getDays());
		assertEquals(10, test2.getHours());
		assertEquals(10, test2.getMinutes());	
	}

	@Test
	public void test_addTimePosSmall() {
		DateTime test1 = zero.addTime(10,10,10);
		assertEquals(10, test1.getDays());
		assertEquals(10, test1.getHours());
		assertEquals(10, test1.getMinutes());
	
		DateTime test2 = pos.addTime(10,10,10);
		assertEquals(20, test2.getDays());
		assertEquals(20, test2.getHours());
		assertEquals(20, test2.getMinutes());	
	}
	
	@Test
	public void test_addTimePosBig() {
		DateTime test1 = zero.addTime(60,60,120);
		assertEquals(62, test1.getDays());
		assertEquals(14, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.addTime(70,70,80);
		assertEquals(83, test2.getDays());
		assertEquals(9, test2.getHours());
		assertEquals(30, test2.getMinutes());	
	}

	@Test
	public void test_addTimeZeroDT() {
		DateTime test1 = zero.addTime(zero);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.addTime(zero);
		assertEquals(10, test2.getDays());
		assertEquals(10, test2.getHours());
		assertEquals(10, test2.getMinutes());	
	}

	@Test
	public void test_addTimePosSmallDT() {
		DateTime test1 = zero.addTime(pos);
		assertEquals(10, test1.getDays());
		assertEquals(10, test1.getHours());
		assertEquals(10, test1.getMinutes());
	
		DateTime test2 = pos.addTime(pos);
		assertEquals(20, test2.getDays());
		assertEquals(20, test2.getHours());
		assertEquals(20, test2.getMinutes());	
	}
	
	@Test
	public void test_addTimePosBigDT() {
		DateTime big = new DateTime(60,60,120);
		DateTime bigg = new DateTime(70,70,80);
		DateTime test1 = zero.addTime(big);
		assertEquals(62, test1.getDays());
		assertEquals(14, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.addTime(bigg);
		assertEquals(83, test2.getDays());
		assertEquals(9, test2.getHours());
		assertEquals(30, test2.getMinutes());	
	}

	@Test
	public void test_subtractTimeZero() {
		DateTime test1 = zero.subtractTime(0,0,0);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.subtractTime(0,0,0);
		assertEquals(10, test2.getDays());
		assertEquals(10, test2.getHours());
		assertEquals(10, test2.getMinutes());	
	}

	@Test
	public void test_subtractTimePosSmall() {
		DateTime test1 = zero.subtractTime(10,10,10);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.subtractTime(10,10,10);
		assertEquals(0, test2.getDays());
		assertEquals(0, test2.getHours());
		assertEquals(0, test2.getMinutes());	
	}
	
	@Test
	public void test_subtractTimePosBig() {
		DateTime test1 = zero.subtractTime(60,60,120);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.subtractTime(70,70,80);
		assertEquals(0, test2.getDays());
		assertEquals(0, test2.getHours());
		assertEquals(0, test2.getMinutes());	
	}

	@Test
	public void test_subtractTimeZeroDT() {
		DateTime test1 = zero.subtractTime(zero);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.subtractTime(zero);
		assertEquals(10, test2.getDays());
		assertEquals(10, test2.getHours());
		assertEquals(10, test2.getMinutes());	
	}

	@Test
	public void test_subtractTimePosSmallDT() {
		DateTime test1 = zero.subtractTime(pos);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.subtractTime(pos);
		assertEquals(0, test2.getDays());
		assertEquals(0, test2.getHours());
		assertEquals(0, test2.getMinutes());	
	}
	
	@Test
	public void test_subtractTimePosBigDT() {
		DateTime big = new DateTime(60,60,120);
		DateTime bigg = new DateTime(70,70,80);
		DateTime test1 = zero.subtractTime(big);
		assertEquals(0, test1.getDays());
		assertEquals(0, test1.getHours());
		assertEquals(0, test1.getMinutes());
	
		DateTime test2 = pos.subtractTime(bigg);
		assertEquals(0, test2.getDays());
		assertEquals(0, test2.getHours());
		assertEquals(0, test2.getMinutes());	
	}
	
	//--------------------------------------------------------------------------
	// Get in minutes.
	//--------------------------------------------------------------------------
	@Test
	public void test_getInMinutesZero() {
		DateTime zero = new DateTime(0, 0, 0);
		
		assertEquals(0, zero.getInMinutes());
	}
	
	@Test
	public void test_getInMinutesMinutesSmall() {
		DateTime zero = new DateTime(0, 0, 50);
		
		assertEquals(50, zero.getInMinutes());
	}

	@Test
	public void test_getInMinutesMinutesBig() {
		DateTime zero = new DateTime(0, 0, 5000);
		
		assertEquals(5000, zero.getInMinutes());
	}
	
	@Test
	public void test_getInMinutesHoursSmall() {
		DateTime zero = new DateTime(0, 10, 0);
		
		assertEquals(600, zero.getInMinutes());
	}

	@Test
	public void test_getInMinutesHoursBig() {
		DateTime zero = new DateTime(0, 50, 0);
		
		assertEquals(3000, zero.getInMinutes());
	}

	@Test
	public void test_getInMinutesDays() {
		DateTime zero = new DateTime(1, 0, 0);
		
		assertEquals(24 * 60, zero.getInMinutes());
	}
	
	@Test 
	public void test_getInMinutesCombined() {
		DateTime zero = new DateTime(1, 2, 3);
		
		assertEquals(24 * 60 + 2 * 60 + 3, zero.getInMinutes());
	}
	
	//--------------------------------------------------------------------------
	// toString
	//--------------------------------------------------------------------------
	@Test
	public void test_toString(){
		DateTime t1 = new DateTime(1,0,0);
		assertNotNull(t1.toString());
	}
	
	//--------------------------------------------------------------------------
	// hashCode
	//--------------------------------------------------------------------------
	@Test
	public void test_hashCode(){
		DateTime t1 = new DateTime(1,0,0);
		DateTime t2 = new DateTime(0,1,0);
		DateTime t3 = new DateTime(0,0,1);
		
		assertNotEquals(t1.hashCode(), t2.hashCode());
		assertNotEquals(t1.hashCode(), t3.hashCode());
		assertNotEquals(t2.hashCode(), t3.hashCode());
	}

	//--------------------------------------------------------------------------
	// Compare to 
	//--------------------------------------------------------------------------
	@Test
	public void test_compareToSmaller() {
		DateTime t1 = new DateTime(1, 0, 0);
		DateTime t2 = new DateTime(0, 1, 0);
		DateTime t3 = new DateTime(0, 0, 1);
		
		assertEquals(-1, zero.compareTo(t1));
		assertEquals(-1, zero.compareTo(t2));
		assertEquals(-1, zero.compareTo(t3));
		
		assertEquals(-1, t3.compareTo(t2));
		assertEquals(-1, t3.compareTo(t1));
		assertEquals(-1, t2.compareTo(t1));
	}
	
	@Test
	public void test_compareToEqualsItself() {
		DateTime t1 = new DateTime(1, 0, 0);
		DateTime t2 = new DateTime(0, 1, 0);
		DateTime t3 = new DateTime(0, 0, 1);
		
		assertEquals(0, t1.compareTo(t1));
		assertEquals(0, t2.compareTo(t2));
		assertEquals(0, t3.compareTo(t3));
	}
	
	@Test
	public void test_compareToEqualsSomethingElse() {
		DateTime t11 = new DateTime(1, 0, 0);
		DateTime t21 = new DateTime(0, 1, 0);
		DateTime t31 = new DateTime(0, 0, 1);

		DateTime t12 = new DateTime(1, 0, 0);
		DateTime t22 = new DateTime(0, 1, 0);
		DateTime t32 = new DateTime(0, 0, 1);

		
		assertEquals(0, t11.compareTo(t12));
		assertEquals(0, t21.compareTo(t22));
		assertEquals(0, t31.compareTo(t32));		
	}
	
	@Test
	public void test_compareToEqualsGreater() {
		DateTime t1 = new DateTime(1, 0, 0);
		DateTime t2 = new DateTime(0, 1, 0);
		DateTime t3 = new DateTime(0, 0, 1);
		
		assertEquals(1, t1.compareTo(zero));
		assertEquals(1, t2.compareTo(zero));
		assertEquals(1, t3.compareTo(zero));
		
		assertEquals(1, t2.compareTo(t3));
		assertEquals(1, t1.compareTo(t3));
		assertEquals(1, t1.compareTo(t2));		
	}

	//--------------------------------------------------------------------------
	// Equals
	//--------------------------------------------------------------------------
	@Test
	public void test_equalsItself() {
		DateTime t1 = new DateTime(1, 0, 0);
		DateTime t2 = new DateTime(0, 1, 0);
		DateTime t3 = new DateTime(0, 0, 1);
		
		assertEquals(true, t1.equals(t1));
		assertEquals(true, t2.equals(t2));
		assertEquals(true, t3.equals(t3));
	} 
	
	@Test
	public void test_equalsSomethingElse() {
		DateTime t11 = new DateTime(1, 0, 0);
		DateTime t21 = new DateTime(0, 1, 0);
		DateTime t31 = new DateTime(0, 0, 1);

		DateTime t12 = new DateTime(1, 0, 0);
		DateTime t22 = new DateTime(0, 1, 0);
		DateTime t32 = new DateTime(0, 0, 1);

		
		assertEquals(true, t11.equals(t12));
		assertEquals(true, t21.equals(t22));
		assertEquals(true, t31.equals(t32));		
	}
	
	@Test
	public void test_equalsFalse() {
		DateTime t1 = new DateTime(1, 0, 0);
		DateTime t2 = new DateTime(0, 1, 0);
		DateTime t3 = new DateTime(0, 0, 1);
		
		assertEquals(false, t1.equals(t2));
		assertEquals(false, t1.equals(t3));
		assertEquals(false, t2.equals(t1));
		assertEquals(false, t2.equals(t3));
		assertEquals(false, t3.equals(t1));
		assertEquals(false, t3.equals(t2));
	}
	
	@Test
	public void test_equalsNull() {
		assertEquals(false, this.zero.equals(null));
	}
	
	@Test
	public void test_equalsDifferentClass() {
		assertEquals(false, this.zero.equals("Ik heb weer is honger."));
	}
}
