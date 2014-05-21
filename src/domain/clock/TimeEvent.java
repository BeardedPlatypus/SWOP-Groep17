package domain.clock;

import domain.DateTime;

/**
 * Class which represents an occuring event with the time of occurence.
 * 
 * @author Thomas Vochten, Frederik Goovaerts
 *
 */
public class TimeEvent {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new event with given globalTime 
	 * 
	 * @param globalTime
	 * @param actor
	 */
	public TimeEvent(DateTime newTime, EventActor actor){
		this.newTime = newTime;
		this.actor = actor;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	public DateTime getGlobalTime() {
		return newTime;
	}

	private EventActor getActor() {
		return actor;
	}

	private final DateTime newTime;
	
	private final EventActor actor;
	
	//--------------------------------------------------------------------------
	// Class Methods
	//--------------------------------------------------------------------------
	
	public void goGoGadgetActivate(){
		//TODO
		this.getActor();
	}
	
	//--------- Event Overriding ---------//

	public boolean overridesEvent(TimeEvent otherEvent){
		return false;
		//TODO
	}

	//----- end of Event Overriding -----//

	
}
