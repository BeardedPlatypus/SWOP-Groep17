package domain.order;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.assemblyLine.TaskType;
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
	 * submissionTime and deadline.  
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
	 * 		The time at which this order has to be finished. 
	 * 
	 * @postcondition | (new this).order.getModel() == model
	 * 				  | (new this).order.getSpecifications() == specifications
	 * 			      | (new this).order.getInitTime() == initTime
	 * 				  | (new this).order.getDeadline() == Optional.of(deadline)
	 * 				  | (new this).order.isCompleted() == False
	 * 				  | (new this).order.getOrderNumber() == orderNumber
	 * 
	 * @throws IllegalArgumentException
	 * 		| model == null || specifications == null || submissionTime == null || deadline == null
	 */
	public SingleTaskOrder(Model model, 
			               Specification specification,
						   int orderNumber, 
						   DateTime submissionTime, 
						   DateTime deadline) throws IllegalArgumentException {
		super(model, specification, orderNumber, submissionTime, Optional.fromNullable(deadline));
		
		// Requirement that each SingleTaskOrderAlways has a deadline.
		if (!this.getDeadline().isPresent()) {
			throw new IllegalArgumentException("SingleTaskOrder has to have a deadline");
		}
	}
	
	/**
	 * Get the tasktype of this singleTaskOrder
	 * 
	 * @return the tasktype of this order
	 */
	public TaskType getSingleTaskOrderType(){
		if(this.getSpecifications().getAmountOfOptions()<1)
			throw new IllegalStateException("This order has no option!");
		return this.getSpecifications().getOption(0).getType();
	}
}
