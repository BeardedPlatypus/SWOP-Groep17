package domain.production_schedule;

/**
 * The OrderSubject provides the subject interface of the Observer Pattern for 
 * the  addition of orders to the system.
 * It provides methods to attach and detach OrderObservers from this Subject, as 
 * well as a notify method to update all OrderObservers that observe this 
 * OrderSubject.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public interface OrderSubject {
	/**
	 * Add the specified OrderObserver to the objects observing this OrderSubject.
	 * OrderObserver o will be updated whenever a new order is added to this 
	 * OrderSubject updated. 
	 * 
	 * @param o
	 * 		The OrderObserver that observes this OrderSubject. 
	 * 
	 * @postcondition o observes this OrderSubject
	 * 
	 * @throws IllegalArgumentException
	 * 		| o == null
	 */
	public void attachOrderObserver(OrderObserver t) throws IllegalArgumentException;
	
	/**
	 * Remove the specified OrderObserver from the objects observing this TimeSubject.
	 * OrderObserver o will not be updated anymore. 
	 * 
	 * @param o
	 * 		The OrderObserver that will stop observing this OrderSubject
	 * 
	 * @precondition o observes this OrderSubject
	 * @postcondition o no longer observes this OrderSubject
	 * @throws IllegalArgumentException
	 * 		| o == null
	 * 
	 */
	public void detachOrderObserver(OrderObserver o) throws IllegalArgumentException;
	
	/**
	 * Notify all OrderObservers that observe this OrderSubject when a new order
	 * is added to this OrderSubject.
	 * 
	 * @precondition a new order is added 
	 * @postcondition | FORALL o: o == OrderObserver AND o observes this -> o.notifyOrder()
	 */
	public void notifyNewOrder();
}
