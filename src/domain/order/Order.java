package domain.order;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.assemblyLine.TaskType;
import domain.car.Specification;
import domain.car.Model;


/** 
 * The Order provides an interface for orders within the Domain. It implements 
 * the OrderContainer interface for passing to the outside system. 
 * 
 * It provides a mostly immutable set of properties of an order, the model,
 * specification, and submission time are immutable and set at creation.
 * An Order is created as not isCompleted, and can only be set to completed once.
 * The estimatedCompletionTime can be updated as time progresses.
 * 
 * Order objects are created by the ProductionScheduler, which keeps track of
 * them. They are converted to AssemblyProcedures before being placed on an
 * AssemblyLine.
 * 
 * @author Martinus Wilhelmus Tegelaers
 * 
 * @invariant order.getModel() != null
 * @invariant order.getSpecifications() != null
 * @invariant order.getSubmissionTime() != null
 * @invariant order.getModel().isValidSpecification(order.getSpecifications())
 */
public abstract class Order implements OrderContainer {
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
	 * @param submissionTime
	 * 		The time at which this order was created.
	 * @param deadline
	 * 		The deadline of this Order if one is specified. 
	 * 
	 * @postcondition | (new this).order.getModel() == model
	 * 				  | (new this).order.getSpecifications() == specifications
	 * 			      | (new this).order.getSubmissionTime() == submissionTime
	 * 				  | (new this).order.isCompleted() == False
	 * 				  | (new this).order.getOrderNumber() == orderNumber
	 * 
	 * @throws IllegalArgumentExceptionException
	 * 		| model == null || specifications == null || 
	 *      | submissionTime == null || deadline == null  
	 */
	protected Order(Model model, Specification specification, int orderNumber, 
			        DateTime submissionTime, Optional<DateTime> deadline) 
													   throws IllegalArgumentException{
		if (model == null)
			throw new IllegalArgumentException("Model is null.");
		if (specification == null)
			throw new IllegalArgumentException("Specification is null.");		
		if (submissionTime == null)
			throw new IllegalArgumentException("The submission time is null.");
		if (deadline == null)
			throw new IllegalArgumentException("The deadline cannot be null.");
		
		this.model = model; 
		this.specifications = specification;
		this.orderNumber = orderNumber;
		this.submissionTime = submissionTime;
		this.deadline = deadline;
		
		this.setIsComplete(false);
	}
	
	//--------------------------------------------------------------------------
	// completion of the Order. 
	//--------------------------------------------------------------------------
	@Override
	public boolean isCompleted() {
		return this.completed;
	}
	
	/**
	 * Set this order to completed at the given completionTime. 
	 * 
	 * @param dt
	 * 		The time at which this order was completed.
	 * 
 	 * @postcondition | this.isCompleted()
 	 * @postcondition | this.getCompletionTimeh() == dt
 	 * 
	 * @throws IllegalArgumentException
	 * 		| dt == null
	 * @throws IllegalStateException
	 * 		dt has been set already.
	 * @throws IllegalArgumentException
	 * 		| dt < this.getSubmissionTime()
	 */
	public void setAsCompleted(DateTime dt) {
		this.setIsComplete(true);
		this.setCompletionTime(dt);
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
	 * @throws IllegalArgumentException
	 * 		| dt == null
	 * @throws IllegalStateException
	 * 		dt has been set already.
	 * @throws IllegalArgumentException
	 * 		| dt < this.getSubmissionTime()
	 */
	protected void setCompletionTime(DateTime dt) throws  IllegalStateException,
														 IllegalArgumentException {
		if (dt == null)
			throw new IllegalArgumentException("Completion time cannot be null.");
		if (this.completionTime != null)
			throw new IllegalStateException("An order cannot be completed twice");		
		if (dt.compareTo(this.getSubmissionTime()) < 0)
			throw new IllegalArgumentException("An order cannot be completed before it is submitted");
		
		this.completionTime = dt;
	}
	
	/** The time at which this Order was completed. */
	private DateTime completionTime = null;	

	//--------------------------------------------------------------------------
	// SubmissionTime
	//--------------------------------------------------------------------------
	@Override
	public DateTime getSubmissionTime() {
		return this.submissionTime;
	}
	
	/** The DateTime at which this order was submitted. */
	private final DateTime submissionTime;

	//--------------------------------------------------------------------------
	// Deadline
	//--------------------------------------------------------------------------
	@Override
	public Optional<DateTime> getDeadline() {
		return this.deadline;
	}
	
	/** An Optional of the deadline of this Order. */
	private final Optional<DateTime> deadline;
	
	//--------------------------------------------------------------------------
	// Specifications
	//--------------------------------------------------------------------------
	@Override
	public Specification getSpecifications() {
		return this.specifications;
	}
	
	/** The specifications of this Order. */
	public final Specification specifications;
	
	//--------------------------------------------------------------------------
	// OrderNumber
	//--------------------------------------------------------------------------
	@Override
	public int getOrderNumber() {
		return this.orderNumber;
	}

	/** The order number of this Order. */
	public final int orderNumber;

	//--------------------------------------------------------------------------
	// Model
	//--------------------------------------------------------------------------
	@Override
	public Model getModel() {
		return this.model;
	}

	/** The model of this Order. */
	public final Model model;
	
// DEPRECATED
//	/**
//	 * Get the amount of minutes that the ordered car is expected to spend
//	 * at each work post.
//	 * 
//	 * @return The amount of minutes
//	 */
//	public int getMinutesPerPost() {
//		return this.getModel().getMinsPerWorkPost();
//	}

	/**
	 * Get the amount of minutes that the ordered car is expected to spend
	 * at the workpost of the specified workPostType
	 * 
	 * @param workPostType
	 * 		The type of workpost of which the number of minutes are requestsed.
	 * 
	 * @return The amount of minutes that the ordered car is expected to spend at 
	 * 		   the workpost of the specified workPostType.
	 */
	public int getMinutesOnPostOfType(TaskType workPostType) {
		return this.getModel().getMinsOnWorkPostOfType(workPostType);
	}
	
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
		if (!(obj instanceof Order))
			return false;
		Order other = (Order) obj;
		if (orderNumber != other.orderNumber)
			return false;
		return true;
	}

}
