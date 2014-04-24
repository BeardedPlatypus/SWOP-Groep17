package domain.order;

import domain.DateTime;
import domain.car.Model;
import domain.car.Specification;

/** 
 * The SingleTaskOrder provides the interface for a custom order consisting of
 * just a single Task in the specification. 
 * It extends the abstract Order class. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class SingleTaskOrder extends Order {
	/**
	 * Construct a new SingleTaskOrder with the specified model and specification, 
	 * initTime and estimatedCompletion time and deadline.  
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
	 * 		| model == null || specifications == null || initTime == null || deadline == null
	 */
	public SingleTaskOrder(Model model, 
			               Specification specification,
						   int orderNumber, 
						   DateTime submissionTime, 
						   DateTime deadline) throws NullPointerException {
		super(model, specification, orderNumber, submissionTime);
		
		if (deadline == null)
			throw new NullPointerException("Deadline cannot be null.");
		
		this.deadline = deadline;
	}
	
	//--------------------------------------------------------------------------
	// Deadline Property
	//--------------------------------------------------------------------------
	/**
	 * Get the Deadline of this SingleTaskOrder. 
	 * 
	 * @return the Deadline of this SingleTaskOrder.
	 */
	public DateTime getDeadline() {
		return this.deadline;
	}
	
	/** The Deadline of this SingleTaskOrder. */
	private final DateTime deadline;
}
