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
	 * Primary method to accept events. A TimeEvent is created
	 * and stored in an event queue.
	 * 
	 * @param timeToElapse
	 * 		The amount of time to update with
	 * @param actor
	 * 		Actor to activate when the event fires
	 * @throws IllegalArgumentException
	 * 		timeToElapse is null
	 * @throws IllegalArgumentException
	 * 		actor is null
	 * @throws IllegalArgumentException
	 * 		actor was not previously registered
	 * @throws IllegalArgumentException
	 * 		There is already an event in the queue for actor
	 */
	public void constructEvent(DateTime timeToElapse, EventActor actor) throws IllegalArgumentException;
	
	/**
	 * Removes the event that has been scheduled for the specified actor from
	 * the event queue.
	 * 
	 * @param actor
	 * 		The EventActor for which to remove the event
	 * @throws IllegalArgumentException
	 * 		hasEventForActor(actor) == false
	 */
	public void removeEventForActor(EventActor actor) throws IllegalArgumentException;
	
	/**
	 * Indicate whether this EventConsumer has an event for the specified actor.
	 * 
	 * @param actor
	 * 		The EventActor to check for
	 * @return There is an event for actor
	 */
	public boolean hasEventForActor(EventActor actor);
	
	/**
	 * Register the specified actor as source of new events
	 * 
	 * @param actor
	 * 		The actor to register
	 * @throws IllegalArgumentException
	 * 		actor is null
	 */
	public void register(EventActor actor) throws IllegalArgumentException;
	
	/**
	 * Unregister the specified actor as source of new events
	 * 
	 * @param actor
	 * 		The actor to unregister
	 */
	public void unregister(EventActor actor);
	
}
