package domain;

/**
 * Container Interface that provides get methods of the container, for inspection
 * purposes. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public interface OrderContainer {
	/** 
	 * Get the completion state of this container. 
	 * 
	 * @return The current completion state of this container. 
	 */
	public boolean isCompleted();
	
	/** 
	 * Get the model of this OrderContainer.  
	 *  
	 * @return The model of this OrderContainer. 
	 */
	public Model getModel();
	
	/**
	 * Get the specification of this OrderContainer. 
	 * 
	 * @return The specification of this OrderContainer. 
	 */
	public Specification getSpecifications();
		
	/**
	 * Get the (unique) order number of this OrderContainer.
	 * 
	 * @return The order number of this OrderContainer.
	 */
	public int getOrderNumber();
}
