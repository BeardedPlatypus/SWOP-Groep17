package domain.productionSchedule;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.order.OrderView;

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
	 * 		t is null
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
	// Completion Time Methods and properties
	//--------------------------------------------------------------------------
	/**
	 * Get the estimated completion of the specified OrderContainer calculated 
	 * by the ClockManager, while given orders are to be scheduled.
	 *  
	 * @param order
	 * 		The order of which the estimated completion time is calculated.
	 * 
	 * @precondition | order != null
	 * 
	 * @return | order.isCompleted() -> order.getEstimatedCompletionTime
	 * @return a DateTime of the estimated completion time of the Order. 
	 * 
	 * @throws IllegalArgumentException
	 *		If either of the arguments is or contains null
	 */
	public DateTime getEstimatedCompletionTime(
			OrderView order, List<OrderView> allPendingOrders) {
		int indexInList;
		if(allPendingOrders.contains(order)){
			indexInList = allPendingOrders.indexOf(order);
		} else{
			indexInList = allPendingOrders.size() + 1;
		}
		return getEstimatedCompletionTime(order, indexInList);
	}
	
	/**
	 * Get the estimated completion based on the current position of the order
	 * in the assembly process. 
	 * The first n positions refer to the n workstations on the assembly line,
	 * The n+m positions, with m > 0, refer to the pending order list. 
	 * 
	 * @param positionOrder
	 * 		The position of the order in the assembly process. 
	 * 
	 * @return a DateTime of the estimated completion time of the Order. 
	 * 
	 * @throws IllegalArgumentException | positionOrder < 0
	 */
	public DateTime getEstimatedCompletionTime(OrderView order, int positionOrder) {
		if (positionOrder < 0) {										            // Invalid Argument
			throw new IllegalArgumentException("position smaller than 0");			//
		}																			//
		                                                                            //
		int amountOfTasksOnAssemblyLine = order.getSpecifications().getAmountOfOptions();      //
		int estHoursForCompletion = N_HOURS_MOVE * (positionOrder + 1);    		    // add one for the actions that are currently being executed to complete.	
		DateTime timeToReturn = this.getCurrentTime();
																					//        
		if (positionOrder < amountOfTasksOnAssemblyLine) {                                     // order is on the assembly line
			return timeToReturn.addTime(0, estHoursForCompletion, 0);
		} else {                                                                    // order is in pending orders
			int timeLeft = this.timeLeftMinutes(); 
					
			if (estHoursForCompletion * 60 <= timeLeft) {                     		// Task can be finished today.
				return timeToReturn.addTime(0, estHoursForCompletion, 0);
				
			} else {								                                // Task can't be finished today.
				estHoursForCompletion -= N_HOURS_MOVE;								// We're moving to the next day, so there is no current action.
				timeToReturn = new DateTime(timeToReturn.getDays() + 1, STARTHOUR, 0);         											// Set to return time to the next day, since we cannot finish it today.

				if (timeLeft < 0) {                                                 // there is left overtime after today, which is specified by a neg timeLeftToday
					 timeLeft = WORKHOURS * 60 + timeLeft;
					
					if(estHoursForCompletion * 60 <= timeLeft) {                    // The order can be finished within the workday of tomorrow.
						return timeToReturn.addTime(0, estHoursForCompletion, 0); 
					} else {                                                        // The order cannot be finished within the workday of tomorrow
						timeToReturn = timeToReturn.addTime(1, 0, 0);			    // We cannot finish the task tomorrow, so we will move to the day after tomorrow. 
					}
				}
				
				int nCarsMoved = Math.max(0, ((timeLeft - 60 * N_HOURS_MOVE * amountOfTasksOnAssemblyLine) / 60 * N_HOURS_MOVE) + 1); // We do not let cars be moved in a negative direction.
				int newPos =  positionOrder - nCarsMoved;                  // The position after tomorrow has ended. 
				
				int carsPerDay = (WORKHOURS - N_HOURS_MOVE * amountOfTasksOnAssemblyLine) / N_HOURS_MOVE + 1; // number of cars that can be produced on a day. + 1 is from the first car.                  
				int extraDays = newPos / carsPerDay; 					   // Number of days that need to pass before order can be executed.
				int extraHours = (newPos % carsPerDay) * N_HOURS_MOVE;     // Number of hours before car can be finished on a day.   
				
				return timeToReturn.addTime(extraDays, extraHours, 0);
			}
		}	
	}
	
	/** 
	 * Calculate the time in minutes left today. 
	 * 
	 * @return the time left today. 
	 */
	private int timeLeftMinutes() {
		return FINISHHOUR * 60 - this.getCurrentTime().getHours() * 60 
				               - this.getCurrentTime().getMinutes();
	}
	
	/** Number of workhours in a shift. */
	private final static int WORKHOURS = 16;
	/** Start of a workday. */
	private final static int STARTHOUR = 6;
	/** End of a workday. */
	private final static int FINISHHOUR = 22;
	/** Estimated number of hours to move to next station. */
	private final static int N_HOURS_MOVE = 1;
	
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
		t.update(this.getCurrentTime());
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
