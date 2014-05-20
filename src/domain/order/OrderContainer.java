package domain.order;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.car.Specification;
import domain.car.Model;

/**
 * Container Interface that provides get methods of the container, for inspection
 * purposes.
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public interface OrderContainer {
	/**
	 * Get the model of this Order.
	 * 
	 * @return The Model of this Order. 
	 */
	public Model getModel();

	/**
	 * Get the Specifications of this Order.
	 * 
	 * @return The Specifications of this Order. 
	 */
	public Specification getSpecifications();

	/**
	 * Get the order number of this Order.
	 * 
	 * @return The order number of this Order.
	 */
	int getOrderNumber();

	/**
	 * Get if this order has been completed. 
	 * 
	 * @return if this Order is completed. 
	 */
	boolean isCompleted();
	
	/**
	 * Get the DateTime at which this Order was submitted. 
	 * 
	 * @return The DateTime at which this Order was submitted. 
	 */
	public DateTime getSubmissionTime();

	/** 
	 * Get the Deadline of this Order, when it should be finished if it is specified
	 * .
	 * @return an optional containing the deadline of this Order if it exists.
	 */
	public Optional<DateTime> getDeadline();
	
	/**
	 * Get the DateTime at which this Order was submitted.
	 * 
	 * @return The DateTime at which this Order was submitted.
	 * 
	 * @throws IllegalStateException
	 * 		| !this.isCompleted()
	 */
	public DateTime getCompletionTime() throws IllegalStateException;

}
