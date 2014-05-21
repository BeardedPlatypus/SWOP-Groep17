package domain.clock;

/**
 * An actor, able to respond to an event, when called. Knows of itself how it
 * should react to an occurring event.
 * 
 * @author Frederik Goovaerts, Thomas Vochten
 *
 */
public interface EventActor {
	
	public void goGoGadgetActivate();

}
