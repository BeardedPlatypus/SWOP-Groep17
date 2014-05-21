package domain.clock;

import com.google.common.base.Optional;

import domain.DateTime;

/**
 * An interface which offers methods to accept and implicitly handle TimeEvents.
 * 
 * @author Frederik Goovaerts, Thomas Vochten
 *
 */
public interface EventConsumer {

	/**
	 * Primary method to accept events.
	 * 
	 * @param timeToElapse
	 * 		The amount of time to update with
	 * @param actor
	 * 		Actor to activate when the event fires
	 * @throws IllegalArgumentException
	 * 		timeToElapse is null
	 * @throws IllegalArgumentException
	 * 		actor is null
	 */
	public void constructEvent(DateTime timeToElapse, Optional<EventActor> actor) throws IllegalArgumentException;
	
}
