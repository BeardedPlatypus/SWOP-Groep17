package domain.clock;

import com.google.common.base.Optional;

import domain.DateTime;

/**
 * Class which represents an occuring event with the time of occurence.
 * 
 * @author Thomas Vochten, Frederik Goovaerts
 *
 */
public class TimeEvent implements Comparable<TimeEvent> {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new event with given globalTime 
	 * 
	 * @param globalTime
	 * 		The new time of the system when the event is executed
	 * @param actor
	 * 		Object that takes an action when the event is executed
	 * @throws IllegalArgumentException
	 * 		newTime is null
	 * @throws IllegalArgumentException
	 * 		actor is null
	 */
	public TimeEvent(DateTime newTime, EventActor actor) throws IllegalArgumentException{
		if (newTime == null) {
			throw new IllegalArgumentException("Cannot initialise TimeEvent with"
					+ "null DateTime");
		}
		if (actor == null) {
			throw new IllegalArgumentException("Cannot initialise TimeEvent with"
					+ "null EventActor");
		}
		
		this.newTime = newTime;
		this.actor = actor;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * @return The new time of the system when the event is executed
	 */
	public DateTime getGlobalTime() {
		return newTime;
	}

	/**
	 * @return The object that takes an action when the event is executed
	 */
	private EventActor getActor() {
		return actor;
	}
	
	public boolean hasActor(EventActor actor) {
		return this.getActor().equals(actor);
	}

	/** New time of the system */
	private final DateTime newTime;
	
	/** Delegate for execution */
	private final EventActor actor;
	
	//--------------------------------------------------------------------------
	// Class Methods
	//--------------------------------------------------------------------------
	
	/**
	 * Execute this TimeEvent.
	 */
	public void activate() {
			this.getActor().activate();
	}
	
	//--------- Event Overriding ---------//

	/**
	 * Indicate whether this TimeEvent overrides the other TimeEvent
	 * 
	 * @param otherEvent
	 * 		The TimeEvent that this TimeEvent potentially overrides
	 * @return True if both TimeEvents have the same EventActor
	 * 		and this TimeEvent's DateTime is smaller than the other TimeEvent's
	 * 		DateTime 
	 */
	public boolean overridesEvent(TimeEvent otherEvent){
		return this.getActor().equals(otherEvent.getActor())
				&& this.getGlobalTime().compareTo(otherEvent.getGlobalTime()) < 0;
	}

	//----- end of Event Overriding -----//
	
	public int compareTo(TimeEvent other) {
		return this.getGlobalTime().compareTo(other.getGlobalTime());
	}

	
}
