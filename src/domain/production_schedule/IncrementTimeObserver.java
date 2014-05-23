package domain.production_schedule;

import domain.DateTime;

/**
 * The IncrementTimeObserver class implements the Observer interface of the 
 * Observer Pattern.
 * It provides the update method the observed IncrementTimeSubject will call 
 * once its internal value changes.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public interface IncrementTimeObserver {
	/**
	 * Update this TimeObserver's with the new time observed from a TimeSubject.
	 *  
	 * @param time
	 * 		The new time of the observed TimeSubject
	 * 
	 * @throws IllegalArgumentException
	 * 		| time == null
	 */
	public void update(DateTime time) throws IllegalArgumentException;
}
