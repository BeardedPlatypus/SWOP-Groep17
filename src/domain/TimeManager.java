package domain;

import java.util.ArrayList;
import java.util.List;

//TODO new in iteration 2
/**
 * Class responsible for manipulating the system's clock.
 * 
 * @author Thomas Vochten
 *
 */
public class TimeManager {
	
	public TimeManager() {
		this.currentTime = new DateTime(0, 6, 0);
		this.observers = new ArrayList<TimeObserver>();
	}
	
	//--------------------------------------------------------------------------
	// State
	//--------------------------------------------------------------------------
	/** The current time */
	private DateTime currentTime;
	
	/**
	 * Get the current time.
	 * 
	 * @return The current time
	 */
	public DateTime getCurrentTime() {
		return this.currentTime;
	}
	
	/**
	 * Add the specified time to this TimeManager's current time.
	 * 
	 * @param time
	 * 		The time to be added to this TimeManager's current time
	 */
	public void addTime(DateTime time) {
		this.setCurrentTime(this.getCurrentTime().addTime(time));
	}
	
	/**
	 * Set the current time to the specified value.
	 * 
	 * @param currentTime
	 * 		The value the current time will be set to
	 */
	private void setCurrentTime(DateTime currentTime) {
		this.currentTime = currentTime;
		this.updateObservers();
	}
	
	//--------------------------------------------------------------------------
	// Observer variables and methods
	//--------------------------------------------------------------------------
	/** Those objects that are interested in the passage of time */
	private List<TimeObserver> observers;
	
	/**
	 * Register the specified observer with this TimeManager
	 * 
	 * @param observer
	 * 		The observer to be registered
	 */
	public void register(TimeObserver observer) {
		this.getObservers().add(observer);
	}
	
	/**
	 * Get the observers that are observing this TimeManager
	 * 
	 * @return The observers
	 */
	private List<TimeObserver> getObservers() {
		return this.observers;
	}
	
	/**
	 * Notify all observers of the passage of time.
	 */
	private void updateObservers() {
		for (TimeObserver observer : this.getObservers()) {
			observer.update(this.getCurrentTime());
		}
	}
	
}