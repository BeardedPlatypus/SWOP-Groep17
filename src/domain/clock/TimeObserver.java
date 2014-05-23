package domain.clock;

import domain.DateTime;

/**
 * The TimeObserver class implements the Observer interface of the Observer Pattern.
 * It provides the update method the observed TimeSubject will call once its 
 * internal value changes.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public interface TimeObserver {
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
