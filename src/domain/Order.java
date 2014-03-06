package domain;

/**
 * Order class of the Assembly Line software system. 
 * It provides a mostly immutable set of properties of an order, the model,
 * specification, and initialisation are immutable and set at creation. 
 * An Order is created as !isCompleted, and can only be set to completed once. 
 * The estimatedCompletionTime can be updated as time progresses.
 * 
 * Order objects are created by the ProductionScheduler, which keeps track of 
 * them until they are placed on the AssemblyLine. 
 * Once completed, they are placed in the completedOrderSet of the Manufacturer.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 * @invariant order.getModel() != null
 * @invariant order.getSpecifications() != null
 * @invariant order.getInitialisationTime() != null
 * @invariant order.getEstimatedCompletionTime() != null
 * @invariant order.getModel().isValidSpecification(order.getSpecifications())
 */
public class Order implements OrderContainer{
	/**
	 * Construct a new Order with the specified model and specification, 
	 * initTime and estimatedCompletion time. 
	 * The isCompleted field is initialised as False. 
	 * 
	 * @param model 
	 * 		The model of this new Order.
	 * @param specifications
	 * 		The specifications of this new Order.
	 * @param orderNumber
	 * 		The orderNumber of this new Order.  
	 * @param initTime
	 * 		The time at which this order was created.
	 * @param estimatedCompletionDateTime
	 * 		The estimated time at which this order should be completed. 
	 * 
	 * @postcondition | (new this).order.getModel() == model
	 * 				  | (new this).order.getSpecifications() == specifications
	 * 			      | (new this).order.getInitTime() == initTime
	 * 			      | (new this).order.getEstimatedCompletionDateTime() == estimatedCompletionDateTime
	 * 				  | (new this).order.isCompleted() == False
	 * 				  | (new this).order.getOrderNumber() == orderNumber
	 * 
	 * @throws NullPointerException
	 * 		| model == null || specifications == null || initTime == null || estimatedCompletionDateTime == null 
	 * @throws IllegalArgumentException
	 * 		| !model.isValidSpecifications(specifications)
	 */
	public Order(Model model, Specifications specifications, int orderNumber,
			     DateTime initTime,
				 DateTime estimatedCompletionDateTime) throws NullPointerException,
				 											  IllegalArgumentException{
		if (model == null )
			throw new NullPointerException("Model is null.");
		if (specifications == null)
			throw new NullPointerException("Specifications is null.");
		if (initTime == null)
			throw new NullPointerException("initTime is null.");
		if (estimatedCompletionDateTime == null)
			throw new NullPointerException("estimatedCompletionDateTime is null.");
		
		if (!model.isValidSpecification(specifications))
			throw new IllegalArgumentException();
		
		this.model = model; 
		this.initTime = initTime;		
		this.specifications = specifications;
		this.orderNumber = orderNumber;
		
		this.setIsComplete(false);
		this.setEstimatedCompletionTime(estimatedCompletionDateTime);
	}

	//------------------------------------------------------------------------
	// Properties
	//------------------------------------------------------------------------
	/**
	 * Set this order to completed at the given completionTime. 
	 * 
	 * @param completionTime : 
	 * 		The time this order has been completed. 
	 * 
	 * @postcondition | !this.isCompleted() && 
	 * 				  | completionTime != null -> (new this).isCompleted() == True
	 * 				  |                           (new this).getCompletionTime() == completionTime
	 */
	public void setAsCompleted(DateTime completionTime) {
		if (!this.isCompleted() && completionTime != null) {
			this.setEstimatedCompletionTime(completionTime);
			this.setIsComplete(true);
		}
	}

	/**
	 * Get if this order has been completed. 
	 * 
	 * @return if this Order is completed. 
	 */
	@Override
	public boolean isCompleted() {
		return this.completed;
	}
	
	/** 
	 * Set the completion state of this order to isCompleted. 
	 *  
	 * @param isCompleted : the new status of this.Order
	 * 
	 * @postcondition (new this).isCompleted() == isCompleted
	 */
	//@Basic
	private void setIsComplete(boolean isCompleted) {
		this.completed = isCompleted;
	}

	/** If this order has been completed. */
	private boolean completed;

	//--------------------------------------------------------------------------
	/**
	 * Get the estimated completion time of this Order. 
	 * If completed, this is the actual completion time. 
	 * 
	 * @return the (estimated) completion time of this Order.
	 */
	//@Basic
	public DateTime getEstimatedCompletionTime() {
		return this.estimatedCompletionTime;
	}

	//@Basic
	/** 
	 * Set the estimated completion time of this Order to completionTime
	 * 
	 * @param completionTime
	 * 		The new estimated completion time of this Order. 
	 * 
	 * @postconditions | !this.isCompleted -> (new this).getEstimatedCompletionTime() == completionTime
	 */
	public void setEstimatedCompletionTime(DateTime completionTime) {
		if (!this.isCompleted())
			this.estimatedCompletionTime = completionTime;
	}
	
	/** The estimated completion time of this Order. */
	private DateTime estimatedCompletionTime;
	
	//--------------------------------------------------------------------------
	/**
	 * Get the initialisation time of this Order.
	 * 
	 * @return The initialisation time of this Order.
	 */
	@Override
	public DateTime getInitialisationTime() {
		return this.initTime;
	}
	
	/** The initialisation time of this Order. */
	public final DateTime initTime;

	//--------------------------------------------------------------------------
	/**
	 * Get the model of this Order.
	 * 
	 * @return The Model of this Order. 
	 */
	@Override
	public Model getModel() {
		return this.model;
	}

	/** The model of this Order. */
	public final Model model;

	//--------------------------------------------------------------------------
	/**
	 * Get the Specifications of this Order.
	 * 
	 * @return The Specifications of this Order. 
	 */
	@Override
	public Specifications getSpecifications() {
		return this.specifications;
	}
	
	/** The specifications of this Order. */
	public final Specifications specifications;

	@Override
	public int getOrderNumber() {
		return this.orderNumber;
	}

	public final int orderNumber;
	
	//--------------------------------------------------------------------------
	//class methods
	/**
	 * Print an overview of all relevant fields of this Order. 
	 * 
	 * @return An overview of all relevant fields of this Order. 
	 */
	@Override
	public String toString() {
		return "Order\n" +
			   "-----------------------\n" +
			   "Model:" + this.getModel().getModelName() + "\n" +
			   "Specifications: " + this.getSpecifications().toString() + "\n" +
			   "Initialisation time: " + this.getInitialisationTime().toString() + "\n" +
			   "Estimated Completion time: " + this.getEstimatedCompletionTime().toString() + "\n";
	}
}