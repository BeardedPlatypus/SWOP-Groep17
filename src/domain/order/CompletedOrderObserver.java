package domain.order;

/**
 * Interface for objects that are interested in completed Orders
 * 
 * @author Thomas Vochten
 *
 */
public interface CompletedOrderObserver {
	
	/**
	 * Notify this observer of the specified event
	 * 
	 * @param event
	 * 		The event to be notified of
	 * @throws IllegalArgumentException
	 * 		event is null
	 */
	public void updateCompletedOrder(CompletedOrderEvent event) throws IllegalArgumentException;

}
