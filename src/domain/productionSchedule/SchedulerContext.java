package domain.productionSchedule;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.DateTime;
import domain.car.Specification;
import domain.assemblyLine.TaskType;
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
	 * //TODO fix doc
	 * Construct a new SchedulerContext with the given default Strategy and the
	 * TaskTypes this context should have a queue for
	 * 
	 * @param defaultStrategy
	 * 		The default strategy of this new SchedulerContext
	 * @param taskCategories
	 * 		The supported tasktypes of the new context 
	 * @post this.getCurrentStrategy == defaultStrategy
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * 		if either of the parameters is or contains null
	 */
	public SchedulerContext(SchedulingStrategy<StandardOrder> defaultStrategy,
			                List<TaskType> taskCategories) throws IllegalArgumentException {
		if(defaultStrategy == null)
			throw new IllegalArgumentException("Strategy should not be null.");
		if (taskCategories == null)
			throw new IllegalArgumentException(
					"taskCategories list should not be null.");
		if (taskCategories.contains(null))
			throw new IllegalArgumentException(
					"taskCategories list should not contain null");
		//Scheduling variables
		this.defaultStrategy = defaultStrategy;
		
		// Init SingleTaskOrder queues
		for (TaskType t: taskCategories) {
			this.taskOrderQueue.put(t, new ArrayList<SingleTaskOrder>());
		}
		
		this.setSchedulingStrategy(this.getDefaultStrategy());
	}
	
	//--------------------------------------------------------------------------
	// Strategy-related methods
	//--------------------------------------------------------------------------
	/**
	 * Get the current scheduling strategy of this SchedulerContext. 
	 * 
	 * @return the current scheduling strategy of this SchedulerContext.
	 */
	public SchedulingStrategy<StandardOrder> getCurrentSchedulingStrategy() {
		return this.currentStrategy;
	}
	
	/** 
	 * Set the current scheduling strategy of this SchedulerContext to the specified 
	 * SchedulingStrategy.
	 * 
	 * @param newStrategy 
	 * 		The new strategy of this SchedulerContext;
	 * 
	 * @postcondition | (new this).getSchedulingStrategy() == newStrategy
	 * @postcondition orders sorted according to newStrategy.
	 * @throws IllegalArgumentException
	 * 		| newStrategy == null
	 */
	public void setSchedulingStrategy(SchedulingStrategy<StandardOrder> newStrategy) throws IllegalArgumentException{
		if (newStrategy == null)
			throw new IllegalArgumentException();
		this.setSchedulingStrategyRaw(newStrategy);
		newStrategy.sort(this.orderQueue);
	}
	
	/**
	 * Set the current SchedulingStrategy of this SchedulerContext to newStrategy
	 * without anymore side effects.
	 * 
	 * @param newStrategy
	 * 		The new SchedulingStrategy of this SchedulerContext.
	 * 
	 * @postcondition | (new this).getCurrentSchedulingStrategy() == newStrategy
	 */
	private void setSchedulingStrategyRaw(SchedulingStrategy<StandardOrder> newStrategy) {
		this.currentStrategy = newStrategy;
	}
	
	/** The current strategy of this SchedulerContext. */
	private SchedulingStrategy<StandardOrder> currentStrategy;
	
	//--------------------------------------------------------------------------
	/**
	 * Get the default ordering strategy (Comparator<Order>) of this 
	 * SchedulerContext.
	 *  
	 * @return the default ordering strategy of this SchedulerContext.
	 */
	protected SchedulingStrategy<StandardOrder> getDefaultStrategy() {
		return this.defaultStrategy;
	}
	
	/** The default strategy of this SchedulerContext. */
	private final SchedulingStrategy<StandardOrder> defaultStrategy;
	
	//--------------------------------------------------------------------------
	// Order related methods.
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
		return this.getAllPendingOrders().contains(order);
	}

	/**
	 * Get all pending Orders in this SchedulerContext. This list is unordered.
	 * 
	 * @return a list of all pending Orders in this SchedulerContext.
	 */
	public List<OrderContainer> getAllPendingOrders() {
		List<OrderContainer> result = new ArrayList<>();
		result.addAll(this.getPendingStandardOrders());
		result.addAll(this.getPendingSingleTaskOrders());
		return result;
	}

	
	//--------------------------------------------------------------------------
	// Get StandardOrder methods.
	//--------------------------------------------------------------------------	
	/**
	 * Build a list of all Specification batches that are currently eligible
	 * for use in a batch strategy. All batches that are shared by at least three
	 * Orders are included.
	 * 
	 * @return The list of batches
	 */
	public List<Specification> getEligibleBatches() {
		Map<Specification, Integer> tally = new HashMap<Specification, Integer>();
		
		for (StandardOrder order : this.getOrderQueueRaw()) {
			Specification spec = order.getSpecifications();
			if (tally.containsKey(spec)) {
				tally.put(spec, tally.get(spec) + 1);
			} else {
				tally.put(spec, 1);
			}
		}
		
		List<Specification> toReturn = new ArrayList<Specification>();
		
		for (Map.Entry<Specification, Integer> candidate : tally.entrySet()) {
			if (candidate.getValue() >= 3) {
				toReturn.add(candidate.getKey());
			}
		}
		
		return toReturn;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Get all pending StandardOrders of this SchedulerContext.
	 * 
	 * @return all pending StandardOrders of this SchedulerContext.
	 */
	public List<OrderContainer> getPendingStandardOrders() {
		List<OrderContainer> result = new ArrayList<>();
		for (OrderContainer o : this.getOrderQueue()) {
			result.add(o);
		}
		return result;
	}
	
	/**
	 * Pop the next SingleTaskOrder to be scheduled of the specified TaskType from
	 * its respective queue.
	 * If the scheduling algorithm is done after popping, it will be set to the 
	 * default algorithm.
	 * 
	 * @param t
	 * 		The TaskType of the SingleTaskOrder to be popped. 
	 * 
	 * @return the next SingleTaskOrder to be scheduled of the specified type
	 * 
	 * @postcondition | !(new this).contains(result)
	 * @postcondition | !E (o) o.deadline && == o.TaskType == t < result.deadline
	 * @postcondition | currentSchedulingAlgorithm.isDone() -> (new this).getCurrentSchedulingAlgorithm() == this.getDefaultAlgorithm
	 * 
	 * @throws IllegalStateException
	 * 		| !this.hasSingleTaskOrdersOfType(t)
	 */
	StandardOrder popNextStandardOrder() throws IllegalStateException {
		if (!this.hasStandardOrders())
			throw new IllegalStateException();
		StandardOrder result = this.getOrderQueueRaw().remove(0);

		if (this.getCurrentSchedulingStrategy().isDone(this.getOrderQueueRaw()))
			this.setSchedulingStrategy(this.getDefaultStrategy());
		return result;
	}

	
	/**
	 * Get the next StandardOrder to be scheduled (does not modify the queue).
	 * 
	 * @return the next StandardOrder to be scheduled.
	 * 
	 * @throws IllegalStateException
	 * 		| !this.hasStandardOrders()
	 */
	public StandardOrder getNextStandardOrder() {
		if (!this.hasStandardOrders())
			throw new IllegalStateException();
		
		return this.getOrderQueue().get(0);
	}
	
	/**
	 * Check if the StandardOrders queue is not empty.
	 *  
	 * @return true if queue not empty, else false.
     */
	boolean hasStandardOrders() {
		return !this.getOrderQueue().isEmpty();
	}
	
	/**
	 * Return the orderQueue of this SchedulerContext.
	 * 
	 * @return the orderQueue of this SchedulerContext
	 */
	public List<StandardOrder> getOrderQueue() {
		return new ArrayList<>(this.getOrderQueueRaw());
	}
	
	/**
	 * Return the interal OrderQueue of this SchedulerContext;
	 * 
	 * @return the internal OrderQueue of this SchedulerContext
	 */
	private List<StandardOrder> getOrderQueueRaw() {
		return this.orderQueue;
	}
	
	/** The order queue of this SchedulerContext */
	private final List<StandardOrder> orderQueue = new ArrayList<>();
	
	//--------------------------------------------------------------------------
	// Get SingleTaskOrder methods
	//--------------------------------------------------------------------------
	/**
	 * Get all pending SingleTaskOrders of this SchedulerContext
	 * 
	 * @return all pending SingleTaskOrders of this SchedulerContext.
	 */
	public List<OrderContainer> getPendingSingleTaskOrders() {
		List<OrderContainer> result = new ArrayList<>();
		
		for (List<SingleTaskOrder> l : this.taskOrderQueue.values()) {
			for (OrderContainer o : l) {
				result.add(o);
			}
		}
		return result;
	}
	
	/** 
	 * Get the next SingleTaskOrder of type TaskType to be scheduled. 
	 * 
	 * @param t
	 * 		The TaskType of the next SingleTaskOrder to be scheduled.
	 * 
	 * @return The next SingleTaskOrder of the specified TaskType to be scheduled.
	 * 
	 * @throws IllegalStateException
	 * 		| !this.hasSingleTaskOrdersOfType(t)

	 */
	SingleTaskOrder getNextSingleTaskOrderOfType(TaskType t) {
		if (!this.hasSingleTaskOrdersOfType(t))
			throw new IllegalStateException();
		
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
	 * 
	 * @throws IllegalStateException
	 * 		| !this.hasSingleTaskOrdersOfType(t)
	 */
	SingleTaskOrder popNextSingleTaskOrderOfType(TaskType t) throws IllegalStateException {
		if (!this.hasSingleTaskOrdersOfType(t))
			throw new IllegalStateException();

		return this.taskOrderQueue.get(t).remove(0);
	}
	
	boolean hasSingleTaskOrders() {
		for (TaskType t : this.taskOrderQueue.keySet()) {
			if (this.hasSingleTaskOrdersOfType(t))
				return true;
		}
		return false;
	}
	
	/**
	 * check if the queue of the specified Tasktype SingleTaskOrders is not empty.
	 * 
	 * @param t
	 * 		The TaskType of the SingleTaskOrder queue that h
	 * 
	 * @return true if queue not empty, else false.
	 */
	boolean hasSingleTaskOrdersOfType(TaskType t) {
		return !this.getSingleTaskOrdersOfType(t).isEmpty();
	}
	
	public List<SingleTaskOrder> getNextSingleTaskOrders() {
		List<SingleTaskOrder> res = new ArrayList<>();
		
		for (List<SingleTaskOrder> l: this.taskOrderQueue.values()) {
			res.add(l.get(0));
		}
		
		return res;
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
		if(!taskOrderQueue.containsKey(t)){
			throw new IllegalArgumentException("Single task type is not correct for this system.");
		}
		return new ArrayList<SingleTaskOrder>(this.taskOrderQueue.get(t));
	}
	
	/** The queues of all SingleTaskOrders sorted by their TaskType. */
	private final Map<TaskType, List<SingleTaskOrder>> taskOrderQueue = new EnumMap<>(TaskType.class);

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
		this.getCurrentSchedulingStrategy().addTo(order, this.getOrderQueueRaw());
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
		if(!taskOrderQueue.containsKey(order.getSingleTaskOrderType())){
			throw new IllegalArgumentException("Single task type is not correct for this system.");
		}
		List<SingleTaskOrder> queue = taskOrderQueue.get(order.getSingleTaskOrderType());
		boolean added = false;
		for (int i = 0; i < queue.size(); i++) {
			if(!added && order.getDeadline().compareTo(queue.get(i).getDeadline())==-1){
				queue.add(i, order);
				added = true;
				break;
			}
		}
		if(!added)
			queue.add(order);
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
