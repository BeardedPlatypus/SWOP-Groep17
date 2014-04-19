package domain;
/**
 * Class to help statistics registrar keep track of the passage of days.
 * 
 * @author Thomas Vochten
 *
 */

public class WorkingDay {
	
	/**
	 * Initialises a new WorkingDay with the specified day number.
	 * 
	 * @param day
	 * 		Which day it is.
	 * 
	 * @throws IllegalArgumentException
	 * 		day is a negative number.
	 */
	public WorkingDay(int day) throws IllegalArgumentException {
		if (day < 0) {
			throw new IllegalArgumentException("day cannot be negative.");
		}
		this.day = day;
	}
	
	/** The day number this WorkingDay represents. */
	private int day;
	
	/**
	 * 
	 * @return
	 */
	public int getDayNumber() {
		return this.day;
	}

}
