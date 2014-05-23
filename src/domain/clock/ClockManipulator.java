package domain.clock;

import domain.DateTime;

public class ClockManipulator {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new clockManipulator with given Clock
	 * 
	 * @param clock
	 * 		The clock for the new manipulator
	 */
	public ClockManipulator(Clock clock){
		this.clock = clock;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get the clock of this manipulator for internal use
	 * 
	 * @return the clock
	 */
	private Clock getClock(){
		return this.clock;
	}
	
	private final Clock clock;
	
	//--------------------------------------------------------------------------
	// Methods
	//--------------------------------------------------------------------------
	
	/**
	 * Advance the day by one in the system, without checking any of the
	 * preconditions. All observers of the clock are notified.
	 * 
	 * This is only used to set initial time of the system.
	 */
	public void advanceDay(){
		DateTime currentTime = clock.getCurrentTime();
		int thisDay = currentTime.subtractTime(0, 6, 0).getDays();
		DateTime newDay = new DateTime(thisDay+1, 6, 0);
		this.getClock().setCurrentTime(newDay);
		this.getClock().notifyTime();
	}
	
}
