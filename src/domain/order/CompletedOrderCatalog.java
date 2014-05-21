package domain.order;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.clock.TimeObserver;

/**
 * Catalog which contains completed orders of the system.
 * 
 * @author Thomas Vochten, Frederik Goovaerts
 */
public class CompletedOrderCatalog implements TimeObserver{
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new CompletedOrderCatalog with all defaults set.
	 * 
	 * @post this.completedOrders = new ArrayList<Order>()
	 */
	public CompletedOrderCatalog(){
		this.completedOrders = new ArrayList<Order>();
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Orders
	
	/** List containing the completed Orders of this class */
	private List<Order> completedOrders;

	/**
	 * Get a copy of the list of the completed orders as containers
	 * 
	 * @return a list of the completed Orders as containers
	 */
	public List<OrderView> getCompletedOrderContainers() {
		return new ArrayList<OrderView>(completedOrders);
	}
	
	/**
	 * Get the list of completed orders for internal use
	 * 
	 * @return the list of completed orders
	 */
	private List<Order> getCompletedOrders() {
		return this.completedOrders;
	}
	
	/**
	 * Add the given order to the list of completed orders.
	 * 
	 * @param order
	 * 		the order to complete
	 * @throws IllegalStateException
	 * 		If the order is already completed and/or in the catalog
	 * @throws IllegalArgumentException
	 * 		If given order is null
	 */
	public void addCompletedOrder(Order order) 
			throws IllegalArgumentException,
			IllegalStateException{
		order.setAsCompleted(this.getObservedDateTime());
		this.getCompletedOrders().add(order);
	}

	/**
	 * Check whether this catalog contains given orderContainer/Order
	 * 
	 * @param order
	 * 		The option to check for if it is present
	 * 
	 * @return whether the option is present
	 */
	public boolean contains(OrderView order) {
		return this.getCompletedOrderContainers().contains(order);
	}
	
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Time
	
	/**
	 * Get the completion time of given OrderContainer
	 * 
	 * @param order
	 * 		The order to return the completion time of.
	 * 
	 * @return the completion time of the order
	 * 
	 * @throws IllegalArgumentException
	 * 		when the OrderContainer is null
	 * @throws IllegalStateException
	 * 		when the order is not completed or not in the catalog
	 */
	public DateTime getCompletionTime(OrderView order)
			throws IllegalArgumentException,
			IllegalStateException
	{
		if(order == null)
			throw new IllegalArgumentException("Order should not be null.");
		if(!order.isCompleted())
			throw new IllegalStateException("Order is not completed yet!");
		if(!this.contains(order))
			throw new IllegalStateException("Order is not present in the completed catalog.");
		return order.getCompletionTime();
	}

	/**
	 * Update the dateTime of this class.
	 * 
	 * @throws IllegalArgumentException
	 * 		If the DateTime object is null
	 */
	@Override
	public void update(DateTime time) throws IllegalArgumentException{
		if(time == null)
			throw new IllegalArgumentException("DateTime should not be null.");
		this.observedDateTime = time;
		
	}

	/**
	 * Get the current observed DateTime for internal use.
	 * 
	 * @return the current observed DateTime
	 */
	private DateTime getObservedDateTime() {
		return this.observedDateTime;
	}
	
	private DateTime observedDateTime;
	

	//--------------------------------------------------------------------------

	
}