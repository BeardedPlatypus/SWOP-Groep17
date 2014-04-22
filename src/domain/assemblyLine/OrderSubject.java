package domain.assemblyLine;

import java.util.List;

/**
 * The OrderSubject provides the subject Interface of the Observer relation 
 * with the OrderObserver. 
 * When its internal state changes it updates all attached OrderObservers, by
 * passing itself to the OrderObserver. 
 * It provides the method hasOrders() to check the internal state of this OrderSubject 
 *  which can be used in the OrderObservers update implementation.
 * to deal accordingly with the update.
 */
public interface OrderSubject {
	/**
	 * Add the specified OrderObserver to the objects observing this OrderSubject.
	 * OrderObserver will be updated whenever the internal order changes of this 
	 * OrderSubject
	 * 
	 * @param t
	 * 		The OrderObserver that observes this OrderSubject. 
	 * 
	 * @postcondition t observes this OrderSubject
	 * 
	 * @throws IllegalArgumentException
	 * 		| t == null
	 */
	public void attachOrderObserver(OrderObserver t) throws IllegalArgumentException;
	
	/**
	 * Remove the specified OrderObserver from the objects observing this OrderSubject.
	 * OrderObserver t will not be updated anymore. 
	 * 
	 * @param t
	 * 		The OrderObserver that will stop observing this OrderSubject
	 * 
	 * @precondition t observes this OrderSubject
	 * @postcondition t no longer observes this OrderSubject
	 */
	public void detachOrderObserver(OrderObserver t);

	/**
	 * Notify all OrderObservers that observe this OrderSubject with the new 
	 * Order state of this OrderSubject. 
	 * 
	 * @precondition internal Order state has changed. 
	 * @postcondition | FORALL t: t == OrderObserver AND t observes this -> t.update()
	 */
	public void notifyHasOrders();

	/**
	 * Check if this OrderSubject has Orders. 
	 * 
	 * @return True if still contains orders, false otherwise.
	 */
	public boolean hasOrders();
	
	/**
	 * Get the OrderObservers of this OrderSubject. 
	 * 
	 * @return The OrderObservers of this OrderSubject.
	 */
	public List<OrderObserver> getOrderObservers();
}
