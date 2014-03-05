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

	public Specifications getSpecifications();
	
	/**
	 * Get the initialisation time of this OrderContainer
	 * 
	 * @return The initialisation time of this OrderContainer
	 */
	public DateTime getInitialisationTime();

	/**
	 * Get the initialisation time of this OrderContainer
	 * 
	 * @return The initialisation time of this OrderContainer
	 */
	public DateTime getEstimatedCompletionTime();
}
