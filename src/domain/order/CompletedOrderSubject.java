package domain.order;

/**
 * Interface for objects that complete Orders and need to notify other
 * parts of the system.
 * 
 * @author Thomas Vochten
 *
 */
public interface CompletedOrderSubject {

	/**
	 * Attach the specified CompletedOrderObserver to this CompletedOrderSubject
	 * 
	 * @param observer
	 * 		The observer to attach
	 * @throws IllegalArgumentException
	 * 		observer is null
	 */
	public void attachObserver(CompletedOrderObserver observer) throws IllegalArgumentException;
	
	/**
	 * Detach the specified CompletedOrderObserver from this CompletedOrderSubject
	 * 
	 * @param observer
	 * 		The observer to detach
	 */
	public void detachObserver(CompletedOrderObserver observer);
	
	/**
	 * Notify all observers of a CompletedOrderEvent
	 * 
	 * @param event
	 * 		The event to notify of
	 * @throws IllegalArgumentException
	 * 		event is null
	 */
	public void notifyOrderComplete(CompletedOrderEvent event) throws IllegalArgumentException;
}
