package domain;

import domain.Option;

/**
 * Superclass of all Restriction classes.
 * This series of classes has its own inherent restriction made up of options and
 * some way they interact. A restriction class can then accept a set of options
 * and check whether or not its restriction is enforced, or the set is deemed as
 * illegal by the restriction.
 * 
 * @author Frederik Goovaerts
 */
public abstract class Restriction {
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Instatiate the abstract part of a Restriction class with an option as the
	 * main restriction actor/participant.
	 * 
	 * @param part
	 * 		The main participant for this restriction
	 */
	public Restriction(Option part){
		mainParticipant = part;
	}
	
	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------
	
	/**
	 * Get the main participant of the restriction.
	 * 
	 * @return the main participant of the restriction
	 */
	public Option getMainParticipant(){
		return mainParticipant;
	}
	
	/** the main participant of the restriction */
	private final Option mainParticipant;
}