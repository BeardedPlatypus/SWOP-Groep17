package domain.assemblyLine;

/** 
 * The OrderObserver interface makes it possible to observe if there exists 
 * Orders in the OrderSubject. 
 * It provides an updateOrder method that the OrderSubject can call on attached
 * observers. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public interface OrderObserver {
	/** 
	 * Update this OrderSubject, called by the orderSubject that passes itself. 
	 * 
	 * @param orderSubject
	 * 		The orderSubject that calls this OrderObserver.
	 * 
	 * @throws IllegalArgumentException
	 * 		| orderSubject == null 
	 */
	public void updateOrder(OrderSubject orderSubject) throws IllegalArgumentException;
}
