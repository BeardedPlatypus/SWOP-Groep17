package domain.production_schedule;

public interface OrderObserver {
	/**
	 * Notify this OrderObserver of a change in orders in the OrderObservable.
	 *  
	 * @param o
	 * 		The OrderObservable that this OrderObserver is subscribed to.
	 * 
	 * @throws IllegalArgumentException
	 * 		| time == null
	 */
	public void notifyOrder(OrderSubject o) throws IllegalArgumentException;
}
