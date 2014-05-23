package domain.clock;

import java.util.List;

/**
 * The TimeSubject provides the subject interface of the Observer Pattern for 
 * the internal time in this System. 
 * It provides methods to attach and detach TimeObservers from this Subject, as 
 * well as a notify method to update all TimeObservers that observe this 
 * TimeSubject.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public interface TimeSubject {
	/**
	 * Add the specified TimeObserver to the objects observing this TimeSubject.
	 * TimeObserver t will be updated whenever the time of this TimeSubject
	 * is updated. 
	 * 
	 * @param t
	 * 		The TimeObserver that observes this TimeSubject. 
	 * 
	 * @postcondition t observes this TimeSubject
	 * 
	 * @throws IllegalArgumentException
	 * 		| t == null
	 */
	public void attachTimeObserver(TimeObserver t) throws IllegalArgumentException;
	
	/**
	 * Remove the specified TimeObserver from the objects observing this TimeSubject.
	 * TimeObserver t will not be updated anymore. 
	 * 
	 * @param t
	 * 		The TimeObserver that will stop observing this TimeSubject
	 * 
	 * @precondition t observes this TimeSubject
	 * @postcondition t no longer observes this TimeSubject
	 * @throws IllegalArgumentException
	 * 		t is null
	 * 
	 */
	public void detachTimeObserver(TimeObserver t) throws IllegalArgumentException;
	
	/**
	 * Notify all TimeObservers that observe this TimeSubject with the new time 
	 * value of this TimeSubject. 
	 * 
	 * @precondition internal time has changed. 
	 * @postcondition | FORALL t: t == TimeObserver AND t observes this -> t.update()
	 */
	public void notifyTime();
	
	/**
	 * Get the TimeObservers of this TimeSubject. 
	 * 
	 * @return The TimeObservers of this TimeSubject.
	 */
	public List<TimeObserver> getTimeObservers();
}
