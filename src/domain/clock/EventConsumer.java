package domain.clock;

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
	 * @param event
	 * 		The accepted event
	 */
	public void constructEvent(DateTime elapseTime, EventActor actor);
	
}
