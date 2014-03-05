package domain;

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
}
