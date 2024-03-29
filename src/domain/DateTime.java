package domain;

import util.annotations.Immutable;

/**
 * The DateTime class is an immutable data object that contains the number of
 * days, hours, and minutes.
 * Created by the ProductionSchedule class.
 * 
 * @author Martinus Wilhelmus Tegelaers
 * 
 * @invariant 0 <= this.getDays()
 * @invariant 0 <= this.getHours() <= 23
 * @invariant 0 <= this.getMinutes() <= 59
 */
@Immutable
public class DateTime implements Comparable<DateTime>{
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new DateTime with the given days, hours, and minutes.
	 * 
	 * @param days
	 * 		The number of days of this new DateTime.
	 * @param hours
	 * 		The number of hours of this new DateTime.
	 * @param minutes
	 * 		The number of minutes of this new DateTime. 
	 * 
	 * @postcondition | (new this).dateTime.getDays() == DateTime.sanitize(days, hours, minutes)[0]
	 * @postcondition | (new this).dateTime.getHours() == DateTime.sanitize(days, hours, minutes)[1]
	 * @postcondition | (new this).dateTime.getMinutes() == DateTime.sanitize(days, hours, minutes)[2]
	 */
	public DateTime(int days, int hours, int minutes) {
		int[] new_values = DateTime.sanitise(days, hours, minutes);
		
		this.days = new_values[0];
		this.hours = new_values[1];
		this.minutes = new_values[2];
	}
	
	/**
	 * Sanitise the days, hours and minutes so that they confirm to the DateTime
	 * invariants.
	 * 
	 * @param days
	 * 		The input days.
	 * @param hours
	 * 		The input hours.
	 * @param minutes
	 * 		The input minutes.
	 * 
	 * @return {<sanitised days>, <sanitised hours>, <sanitised minutes>}
	 */
	public static int[] sanitise(int days, int hours, int minutes) {
		return sanitise(days * 1440 + hours * 60 + minutes);
		
	}
	
	public static int[] sanitise(long minutes) {
		if (minutes <= 0)
			return new int[]{0, 0, 0};
		
		int[] result = new int[3];
		result[0] = (int) (minutes / 1440);
		result[1] = (int) ((minutes % 1440) / 60);
		result[2] = (int) (minutes % 60);
		return result;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	/** 
	 * Get the days of this DayTime.
	 * 
	 * @return The days of this DayTime
	 */
	public int getDays() {
		return this.days;
	}

	/** The days of this DateTime. */
	public final int days;

	/**
	 * Get the hours of this DayTime
	 * 
	 * @return The hours of this DayTime
	 */
	public int getHours() {
		return this.hours;
	}

	/** The hours of this DateTime. */
	public final int hours;
	
	/**
	 * Get the minutes of this DateTime.
	 * 
	 * @return The minutes of this DateTime.
	 */
	public int getMinutes() {
		return this.minutes;
	}
	
	/** The minutes of this DateTime. */
	public final int minutes;

	/**
	 * Get the time of this DateTime in minutes. 
	 * 
	 * @return the time of this DateTime in minutes.
	 */
	public long getInMinutes() {
		return this.getDays() * 1440 + this.getHours() * 60 + this.getMinutes();
	}
	
	//--------------------------------------------------------------------------
	// Creation Methods. 
	//--------------------------------------------------------------------------
	/**
	 * Create a new DateTime with the current DateTime plus the specified days,
	 * hours, and minutes. 
	 * 
	 * @param days
	 * 		The days to be added to this days. 
	 * @param hours
	 * 		The hours to be added to this hours. 
	 * @param minutes
	 * 		The minutes to be added this minutes.
	 * 
	 * @return new DateTime(days + this.getDays(), hours + this.getHours(), minutes + this.getMinutes())
	 */
	public DateTime addTime(int days, int hours, int minutes) {
		return new DateTime(days + this.getDays(), hours + this.getHours(), minutes + this.getMinutes());
	}
	
	/**
	 * Create new DateTime with the current DateTime + the specified DateTime values
	 * 
	 * @param dt
	 * 		The DateTime values to be added to this DateTime. 
	 * 
	 * @effect addTime(dt.days, dt.hours, dt.minutes)
	 */
	public DateTime addTime(DateTime dt) {
		return this.addTime(dt.getDays(), dt.getHours(), dt.getMinutes());
	}

	//--------------------------------------------------------------------------
	/**
	 * Create a new DateTime with the current DateTime subtract the specified days,
	 * hours, and minutes. 
	 * 
	 * @param days
	 * 		The days to be added to this days. 
	 * @param hours
	 * 		The hours to be added to this hours. 
	 * @param minutes
	 * 		The minutes to be added this minutes.
	 * 
	 * @return new DateTime(days + this.getDays(), hours + this.getHours(), minutes + this.getMinutes())
	 */
	public DateTime subtractTime(int days, int hours, int minutes) {
		return new DateTime(this.getDays() - days, this.getHours() - hours, this.getMinutes() - minutes);
	}

	/**
	 * Create new DateTime with the current DateTime - the specified DateTime values
	 * 
	 * @param dt
	 * 		The DateTime values to be added to this DateTime. 
	 * 
	 * @effect subtractTime(dt.days, dt.hours, dt.minutes)
	 */
	public DateTime subtractTime(DateTime dt) {
		return this.subtractTime(dt.getDays(), dt.getHours(), dt.getMinutes());
	}
	
	/**
	 * Potato quality multiplies.
	 * 
	 * @param s
	 * 		this potato.
	 * @return potato.
	 */
	public DateTime multiplyWithScalar(double s) {
		int[] val = sanitise(Math.round(this.getInMinutes() * s));
		return new DateTime(val[0], val[1], val[2]);
	}

	//--------------------------------------------------------------------------
	// Comparable interface. 
	//--------------------------------------------------------------------------
	@Override
	public int compareTo(DateTime that) {
		return Long.compare(this.getInMinutes(), that.getInMinutes());
	}
	 
	//--------------------------------------------------------------------------
	// Generic Object methods. 
	//--------------------------------------------------------------------------
	/**
	 * Returns a simple string representation of the DateTime object, formatted as:
	 * "Day DD, HHhMMm", based on the DateTime's data.
	 */
	@Override
	public String toString(){
		return ("Day " + this.getDays() + ", " + this.getHours() + "h" + this.getMinutes()+ "m");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + days;
		result = prime * result + hours;
		result = prime * result + minutes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateTime other = (DateTime) obj;
		if (days != other.days)
			return false;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}
