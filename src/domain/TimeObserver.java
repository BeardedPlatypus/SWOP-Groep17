package domain;

/**
 * Observer interface for classes that need to be aware of time.
 * @author Thomas Vochten
 *
 */
public interface TimeObserver {
	
	/**
	 * Notify this TimeObserver of the passage of time.
	 * @param time
	 * 		The new value for the time
	 */
	public void update(DateTime time);

}
