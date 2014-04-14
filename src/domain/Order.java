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
public class Order implements OrderContainer {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
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
	 * 				  | (new this).order.isCompleted() == False
	 * 				  | (new this).order.getOrderNumber() == orderNumber
	 * 
	 * @throws NullPointerException
	 * 		| model == null || specifications == null || initTime == null 
	 * @throws IllegalArgumentException
	 * 		| !model.isValidSpecifications(specifications)
	 */
	public Order(Model model, Specification specification, int orderNumber, DateTime submissionTime) 
													   throws NullPointerException,
				 											  IllegalArgumentException{
		if (model == null )
			throw new NullPointerException("Model is null.");
		if (specification == null)
			throw new NullPointerException("Specification is null.");		
		if (submissionTime == null)
			throw new NullPointerException("The submission time is null.");
		if (!model.isValidSpecification(specification))
			throw new IllegalArgumentException();

		this.model = model; 
		this.specifications = specification;
		this.orderNumber = orderNumber;
		this.submissionTime = submissionTime;
		
		this.setIsComplete(false);
	}	
	
	//------------------------------------------------------------------------
	// Properties
	//------------------------------------------------------------------------
	@Override
	public boolean isCompleted() {
		return this.completed;
	}
	
	/**
	 * Set this order to completed at the given completionTime. 
	 * 
	 * 
 	 * @postcondition | this.isCompleted()
	 */
	public void setAsCompleted() {
		this.setIsComplete(true);
	}
	
	/** 
	 * Set the completion state of this order to isCompleted. 
	 *  
	 * @param isCompleted : the new status of this.Order
	 * 
	 * @postcondition (new this).isCompleted() == isCompleted
	 */
	//@Basic
	protected void setIsComplete(boolean isCompleted) {
		this.completed = isCompleted;
	}

	/** If this order has been completed. */
	private boolean completed;

	//--------------------------------------------------------------------------
	@Override
	public Model getModel() {
		return this.model;
	}

	/** The model of this Order. */
	public final Model model;

	//--------------------------------------------------------------------------
	@Override
	public Specification getSpecifications() {
		return this.specifications;
	}
	
	/** The specifications of this Order. */
	public final Specification specifications;

	//--------------------------------------------------------------------------

	@Override
	public DateTime getSubmissionTime() {
		return this.submissionTime;
	}
	
	/** The DateTime at which this order was submitted. */
	private final DateTime submissionTime;
	
	//--------------------------------------------------------------------------
	@Override
	public DateTime getCompletionTime() throws IllegalStateException {
		if (!this.isCompleted()) {
			throw new IllegalStateException();
		}
		return this.completionTime;
	}
	
	/**
	 * Set the completion time of this Order to the specified DateTime.
	 * 
	 * @param dt
	 * 		The new completion DateTime of this Order.
	 * 
	 * @throws NullPointerException
	 * 		| dt == null
	 * @throws IllegalStateException
	 * 		dt has been set already.
	 * @throws IllegalArgumentException
	 * 		| dt < this.getSubmissionTime()
	 */
	void setCompletionTime(DateTime dt) throws NullPointerException {
		if (dt == null)
			throw new NullPointerException("Completion time cannot be null.");
		if (this.completionTime != null)
			throw new IllegalStateException("An order cannot be completed twice");
		if (dt.compareTo(this.getSubmissionTime()) < 0)
			throw new IllegalArgumentException("An order cannot be completed before it is submitted");
		
		this.completionTime = dt;
	}
	
	/** The time at which this Order was completed. */
	private DateTime completionTime = null;	
	
	//--------------------------------------------------------------------------
	@Override
	public int getOrderNumber() {
		return this.orderNumber;
	}

	/** The order number of this Order. */
	public final int orderNumber;
	
	//--------------------------------------------------------------------------
	//class methods
	//--------------------------------------------------------------------------
	/**
	 * Print an overview of all relevant fields of this Order. 
	 * 
	 * @return An overview of all relevant fields of this Order. 
	 */
	@Override
	public String toString() {
		return "Order\n" +
			   "-----------------------\n" +
			   "Model:" + this.getModel().getName() + "\n" +
			   "Specifications: " + this.getSpecifications().toString() + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderNumber != other.orderNumber)
			return false;
		return true;
	}
}