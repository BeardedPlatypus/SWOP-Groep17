package domain.productionSchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.TaskType;
import domain.order.Order;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.order.OrderContainer;
import domain.productionSchedule.strategy.SchedulingStrategy;

/**
 * The SchedulerContext handles the scheduling of all orders of this system.
 * It provides methods for getting new AssemblyProcedures, ordering the orders, 
 * checking if orders can be scheduled. 
 * It is also responsible for keeping track of and minimising overtime. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class SchedulerContext {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new SchedulerContext with the given starting DateTime and 
	 * overtime in minutes. 
	 * 
	 * @param initTime
	 * 		The starting time of this new SchedulerContext
	 * @param overTime
	 * 		The overtime of this new SchedulerContext in minutes. 
	 * 
	 * @throws IllegalArgumentException
	 * 		| initTime == null
	 */
	public SchedulerContext(SchedulingStrategy defaultStrategy,
			                List<TaskType> taskCategories) throws IllegalArgumentException {
		//Scheduling variables
		this.defaultStrategy = defaultStrategy;
		
		// Init SingleTaskOrder queues
		for (TaskType t: taskCategories) {
			this.taskOrderQueue.put(t, new ArrayList<SingleTaskOrder>());
		}		
	}
	
	//--------------------------------------------------------------------------
	// Strategy-related methods
	//--------------------------------------------------------------------------
	//TODO
	/**
	 * Get the current scheduling strategy of this SchedulerContext. 
	 * 
	 * @return the current scheduling strategy of this SchedulerContext.
	 */
	public SchedulingStrategy getCurrentSchedulingStrategy() {
		return this.currentStrategy;
	}
	
	/** 
	 * Set the current scheduling strategy of this SchedulerContext to the specified 
	 * SchedulingStrategy.
	 * 
	 * @param newStrategy 
	 * 		The new stratgey of this SchedulerContext;
	 * 
	 * @postcondition | (new this).getSchedulingStrategy() == newStrategy
	 */
	public void setSchedulingStrategy(SchedulingStrategy newStrategy) {
		throw new UnsupportedOperationException();
	}
	
	/** The current strategy of this SchedulerContext. */
	private SchedulingStrategy currentStrategy;
	
	//--------------------------------------------------------------------------
	/**
	 * Get the default ordering strategy (Comparator<Order>) of this 
	 * SchedulerContext.
	 *  
	 * @return the default ordering strategy of this SchedulerContext.
	 */
	protected SchedulingStrategy getDefaultStrategy() {
		return this.defaultStrategy;
	}
	
	/** The default strategy of this SchedulerContext. */
	private final SchedulingStrategy defaultStrategy;
	
	//--------------------------------------------------------------------------
	// Get StandardOrder methods.
	//--------------------------------------------------------------------------
	/**
	 * Check if the specified OrderContainer is currently in this SchedulerContext.
	 * 
	 * @param order
	 * 		The OrderContainer that should be checked. 
	 * 
	 * @return | order in this.
	 */
	public boolean containsOrder(OrderContainer order) {
		throw new UnsupportedOperationException();
	}
	
	/** 
	 * Get the next OrderContainer that will be scheduled by this SchedulerContext.
	 * 
	 * @return The next OrderContainer that will be scheduled by this SchedulerContext.
	 */
	public OrderContainer getNextScheduledOrderContainer() {
		return this.getNextScheduledOrder();
	}
	
	/**
	 * Get the next Order that will be scheduled by this SchedulerContext.
	 * 
	 * @return The next Order that will be scheduled by this SchedulerContext.
	 */
	public Order getNextScheduledOrder() {
		return this.orderQueue.get(0);
	}
	
	/** The order queue of this SchedulerContext */
	private final List<StandardOrder> orderQueue = new ArrayList<>();
	
	//--------------------------------------------------------------------------
	// Get SingleTaskOrder methods
	//--------------------------------------------------------------------------
	/** 
	 * Get the next SingleTaskOrder of type TaskType to be scheduled. 
	 * 
	 * @param t
	 * 		The TaskType of the next SingleTaskOrder to be scheduled.
	 * 
	 * @return The next SingleTaskOrder of the specified TaskType to be scheduled.
	 */
	protected SingleTaskOrder getNextSingleTaskOrderOfType(TaskType t) {
		return this.getSingleTaskOrdersOfType(t).get(0);
	}
	
	/**
	 * Pop the next SingleTaskOrder to be scheduled of the specified TaskType from
	 * its respective queue.
	 * 
	 * @param t
	 * 		The TaskType of the SingleTaskOrder to be popped. 
	 * 
	 * @return the next SingleTaskOrder to be scheduled of the specified type
	 * 
	 * @postcondition | !(new this).contains(result)
	 * @postcondition | !E (o) o.deadline && == o.TaskType == t < result.deadline
	 */
	protected SingleTaskOrder popNextSingleTaskOrderOfType(TaskType t) {
		return this.taskOrderQueue.get(t).remove(0);
	}
	
	/**
	 * Get a list of all pending task orders of the specified TaskType sorted
	 * by Deadline. 
	 * 
	 * @param t
	 * 		The TaskType of which all SingleTaskOrders are requested.
	 *  
	 * @return A list of all pending task orders of the specified TaskType sorted
	 * 		   by deadline.
	 */
	protected List<SingleTaskOrder> getSingleTaskOrdersOfType(TaskType t) {
		return new ArrayList<SingleTaskOrder>(this.taskOrderQueue.get(t));
	}
	
	/** The queues of all SingleTaskOrders sorted by their TaskType. */
	private final HashMap<TaskType, List<SingleTaskOrder>> taskOrderQueue = new HashMap<>();

	//--------------------------------------------------------------------------
	// Adding orders
	//--------------------------------------------------------------------------
	/** 
	 * Add an uncompleted StandardOrder to this SchedulerContext's pending orders. 
	 * 
	 * @param order
	 * 		The StandardOrder to be scheduled. 
	 * 
	 * @postcondition | order in (new this).getPendingStandardOrders() 
	 * 
	 * @throws IllegalArgumentException
	 * 		| !isValidPendingOrder(order)
	 */
	public void addNewStandardOrder(StandardOrder order) throws IllegalArgumentException {
		if (!isValidPendingOrder(order)) {
			throw new IllegalArgumentException("Order is not a valid pending order.");
		}
		//TODO	
	}
	
	/**
	 * Add an uncompleted SingleTaskOrder to this SchedulerContext's pending orders.
	 * 
	 * @param order
	 * 		The SingleTaskOrder to be scheduled.
	 * 
	 * @postcondition | order in (new this).getPendingSingleTaskOrders()
	 * 
	 * @throws IllegalArgumentException
	 * 		| !isValidPendingOrder(order))
	 */
	public void addNewSingleTaskOrder(SingleTaskOrder order) throws IllegalArgumentException {
		if (!isValidPendingOrder(order)) {
			throw new IllegalArgumentException("Order is not a valid pending order.");
		}
		//TODO
	}
	
	/**
	 * Check if the specified Order is a valid pending Order to be scheduled.
	 * 
	 * @return | order != null && !order.isCompleted()
	 */
	public boolean isValidPendingOrder(Order order) {
		return order != null && !order.isCompleted();
	}
}
