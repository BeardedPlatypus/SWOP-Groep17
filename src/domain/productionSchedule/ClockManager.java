package domain.productionSchedule;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;

/**
 * The ClockManager is the object that manages the internal Clock of the whole
 * system. It gets updated by all TimeIncrementSubjects it observes. 
 * When the time gets updated it notifies all its TimeObservers, through the 
 * TimeSubject interface. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 * 
 * @invariant | this.getCurrentTime != null  
 *
 */
public class ClockManager implements TimeSubject, IncrementTimeObserver {
	//--------------------------------------------------------------------------
	// Constructors
	//--------------------------------------------------------------------------
	/**
	 * Construct a new ClockManager with the specified DateTime as begin time.
	 * 
	 * @param t
	 * 		The begin time of this new ClockManager
	 * 
	 * @postcondition
	 * 		| (new this).getCurrentTime() == t
	 * 
	 * @throws IllegalArgumentException
	 * 		| t == null
	 */
	public ClockManager(DateTime t) {
		if (t == null)
			throw new IllegalArgumentException("time cannot be null.");
		
		this.currentTime = t;
	}
	
	/**
	 * Construct a new ClockManager with a starting day DateTime object 
	 * as begin time.
	 * 
	 * @effect | this(new DateTime(0, 6, 0);
	 */
	public ClockManager() {
		this(new DateTime(0, 6, 0));
	}
	
	//--------------------------------------------------------------------------
	// Time properties. 
	//--------------------------------------------------------------------------
	/**
	 * Get the current DateTime of this ClockManager.
	 * 
	 * @return the current DateTime of this ClockManager.
	 */
	public DateTime getCurrentTime() {
		return this.currentTime;
	}
	
	/**
	 * Increment this ClockManagers internal clock by the given DateTime.
	 * 
	 * @param t
	 * 		The time with which this ClockManager's clock is incremented.
	 * 
	 * @postcondition | (new this).getCurrentTime() = this.getCurrentTime.add(t)
	 * 
	 * @throws IllegalArgumentException
	 */
	protected void incrementTime(DateTime t) throws IllegalArgumentException {
		if (t == null) {
			throw new IllegalArgumentException("Increment time cannot be null.");
		}
		
		DateTime newTime = this.getCurrentTime().addTime(t);
		this.setTime(newTime);
	}
	
	/**
	 * Set the current DateTime of this ClockManager to the specified DateTime
	 * and call the notify method of the TimeSubject interface.
	 * 
	 * @param t
	 * 		The new DateTime of this ClockManager
	 * 
	 * @precondition | t != null
	 * @postcondition | (new this).getCurrentTime == t
	 * 
	 * @effect | this.notify()
	 */
	private void setTime(DateTime t) {
		this.currentTime = t;
		this.notifyTime();
	}
	
	/** The current time of this ClockManager. */
	private DateTime currentTime;
	
	//--------------------------------------------------------------------------
	// TimeSubject Methods.
	//--------------------------------------------------------------------------
	@Override
	public void attachTimeObserver(TimeObserver t) throws IllegalArgumentException {
		if (t == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		
		if (!this.observers.contains(t)) { 
			this.observers.add(t); 
		}
	}

	@Override
	public void detachTimeObserver(TimeObserver t) {
		if (t == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}

		if (this.observers.contains(t)) {
			this.observers.remove(t);
		}
	}

	@Override
	public void notifyTime() {
		DateTime curTime = this.getCurrentTime();
		for(TimeObserver obs : this.getTimeObservers()) {
			obs.update(curTime);
		}
	}

	@Override
	public List<TimeObserver> getTimeObservers() {
		return new ArrayList<TimeObserver>(this.observers);
	}	
	
	/** The observes of this TimeSubject. */ 
	private final List<TimeObserver> observers = new ArrayList<TimeObserver>();
	
	//--------------------------------------------------------------------------
	// IncrementTimeObserver Methods
	//--------------------------------------------------------------------------
	@Override
	public void update(DateTime time) throws IllegalArgumentException {
		this.incrementTime(time);
	}
}