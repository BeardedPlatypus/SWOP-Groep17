package domain.production_schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

import domain.car.Model;
import domain.car.Specification;
import domain.assembly_line.TaskType;
import domain.order.Order;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.order.OrderView;
import domain.production_schedule.strategy.SchedulingStrategy;

/**
 * The SchedulerContext handles the scheduling of all orders of this system.
 * It provides methods for getting new AssemblyProcedures, ordering the orders, 
 * checking if orders can be scheduled. 
 * It is also responsible for keeping track of and minimising overtime. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public class SchedulerContext implements OrderSubject {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new SchedulerContext with the given default Strategy and the
	 * TaskTypes this context should have a queue for
	 * 
	 * @param defaultStrategy
	 * 		The default strategy of this new SchedulerContext
	 * @post this.getCurrentStrategy == defaultStrategy 
	 * 
	 * @throws IllegalArgumentException | defaultStrategy == null
	 */
	public SchedulerContext(SchedulingStrategy<StandardOrder> defaultStrategy) throws IllegalArgumentException {
		if(defaultStrategy == null)
			throw new IllegalArgumentException("Strategy should not be null.");
		
		//Scheduling variables
		this.defaultStrategy = defaultStrategy;		
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
		newStrategy.sort(this.standardOrderQueue);
	}
	
	/**
	 * Set the current SchedulingStrategy of this SchedulerContext to newStrategy
	 * without any side effects.
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
	 * Get the default ordering strategy of this SchedulerContext.
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
	public boolean containsOrder(OrderView order) {
		return this.getAllPendingOrders().contains(order);
	}

	/**
	 * Get all pending Orders in this SchedulerContext. This list is unordered.
	 * 
	 * @return a list of all pending Orders in this SchedulerContext.
	 */
	public List<OrderView> getAllPendingOrders() {
		List<OrderView> result = new ArrayList<>();
		result.addAll(this.getPendingStandardOrders());
		result.addAll(this.getPendingSingleTaskOrders());
		return result;
	}

	//--------------------------------------------------------------------------
	// Get orders
	//--------------------------------------------------------------------------
	public Optional<Order> getOrder(OrderRequest request) {
		switch (request.getOrderType()) {
		case STANDARD:
			return this.getNextStandardOrder(request.getModels());
		case SINGLETASK:
			return this.getNextSingleTaskOrder(request.getTaskTypes());
		default:
			throw new IllegalStateException("Enum type not in cases.");
		}
	}
	
	public Optional<Order> popOrder(OrderRequest request) {
		switch (request.getOrderType()) {
		case STANDARD:
			return this.popNextStandardOrder(request.getModels());
		case SINGLETASK:
			return this.popNextSingleTaskOrder(request.getTaskTypes());
		default:
			throw new IllegalStateException("Enum type not in cases.");
		}
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
		
		for (StandardOrder order : this.getStandardOrderQueueRaw()) {
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
	public List<OrderView> getPendingStandardOrders() {
		List<OrderView> result = new ArrayList<>();
		for (OrderView o : this.getStandardOrderQueue()) {
			result.add(o);
		}
		return result;
	}
	
	/**
	 * Get the next StandardOrder to be scheduled (does not modify the queue)
	 * that matches on of the models in the specified set. 
	 * If no order exists with a model in the specified set the optional will 
	 * be absent.
	 * 
	 * @param acceptedModels
	 * 		The set of models that the result is allowed to contain. 
	 * 
	 * @return An optional of the next StandardOrder to be scheduled if it 
	 * 		   exists, else an absent Optional.
	 * 
	 * @throws IllegalArgumentException
	 * 		| acceptedModels == null || acceptedModels.contains(null)
	 */
	Optional<Order> getNextStandardOrder(Set<Model> acceptedModels) {
		if (acceptedModels == null)
			throw new IllegalArgumentException("acceptedModels cannot be null");
		if (acceptedModels.contains(null))
			throw new IllegalArgumentException("acceptedModels cannot contain null");
		Order result = null;
		
		for (StandardOrder order : this.getStandardOrderQueue()) {
			if (acceptedModels.contains(order.getModel())) {
				result = order;
				break;
			}
		}		
		return Optional.fromNullable(result);
	}
	
	
	/**
	 * Pop the next StandardOrder that has a model belonging to acceptedModels 
	 * from the internal queue. If no order having a model belonging to 
	 * acceptedModels exists, the result will be absent. 
	 * After requesting the update, the strategy will be updated if it is
	 * finished. 
	 * 
	 * @param acceptedModels
	 * 		The set of models that the result is allowed to contain. 
	 * @return An optional containing the popped Order if it exists.
	 * 
	 * @throws IllegalArgumentException
	 * 		| acceptedModels == null || acceptedModels.contains(null)
	 */
	Optional<Order> popNextStandardOrder(Set<Model> acceptedModels) throws IllegalArgumentException {
		Optional<Order> result = this.getNextStandardOrder(acceptedModels);
		
		if (result.isPresent())
			this.getStandardOrderQueueRaw().remove(result.get());

		if (this.getCurrentSchedulingStrategy().isDone(this.getStandardOrderQueueRaw()))
			this.setSchedulingStrategy(this.getDefaultStrategy());
		
		return result;
	}
	//--------------------------------------------------------------------------	
	/**
	 * Check if the StandardOrders queue is not empty.
	 *  
	 * @return true if queue not empty, else false.
     */
	boolean hasStandardOrders() {
		return !this.getStandardOrderQueue().isEmpty();
	}
	
	/**
	 * Get the standardOrderQueue of this SchedulerContext.
	 * 
	 * @return the standardOrderQueue of this SchedulerContext.
	 */
	public List<StandardOrder> getStandardOrderQueue() {
		return new ArrayList<>(this.getStandardOrderQueueRaw());
	}
	
	/**
	 * Get the internal StandardOrderQueue of this SchedulerContext;
	 * 
	 * @return the internal OrderQueue of this SchedulerContext
	 */
	private List<StandardOrder> getStandardOrderQueueRaw() {
		return this.standardOrderQueue;
	}
	
	/** The order queue of this SchedulerContext */
	private final List<StandardOrder> standardOrderQueue = new ArrayList<>();
	
	//--------------------------------------------------------------------------
	// Get SingleTaskOrder methods
	//--------------------------------------------------------------------------	
	/**
	 * Get all pending StandardOrders of this SchedulerContext.
	 * 
	 * @return all pending StandardOrders of this SchedulerContext.
	 */
	public List<OrderView> getPendingSingleTaskOrders() {
		List<OrderView> result = new ArrayList<>();
		for (OrderView o : this.getSingleTaskOrderQueue()) {
			result.add(o);
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
	Optional<Order> getNextSingleTaskOrder(Set<TaskType> acceptedTaskTypes) {
		if (acceptedTaskTypes == null)
			throw new IllegalArgumentException("acceptedModels cannot be null");
		if (acceptedTaskTypes.contains(null))
			throw new IllegalArgumentException("acceptedModels cannot contain null");
		
		Order result = null;
		
		for (SingleTaskOrder order : this.getSingleTaskOrderQueue()) {
			if (acceptedTaskTypes.contains(order.getSingleTaskOrderType())) {
				result = order;
				break;
			}
		}		
		
		return Optional.fromNullable(result);
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
	Optional<Order> popNextSingleTaskOrder(Set<TaskType> acceptedTaskTypes) throws IllegalStateException {
		Optional<Order> result = this.getNextSingleTaskOrder(acceptedTaskTypes);
		
		if (result.isPresent())
			this.getSingleTaskOrderQueueRaw().remove(result.get());		
		return result;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Check if the SingleTaskOrder queue is not empty.
	 * 
	 * @return True if the queue is not empty, else false.
	 */
	public boolean hasSingleTaskOrders() {
		return this.getSingleTaskOrderQueue().isEmpty();
	}
	
	/**
	 * Get the standardOrderQueue of this SchedulerContext.
	 * 
	 * @return the standardOrderQueue of this SchedulerContext.
	 */
	public List<SingleTaskOrder> getSingleTaskOrderQueue() {
		return new ArrayList<>(this.getSingleTaskOrderQueueRaw());
	}
	
	/**
	 * Return the internal singleTaskOrderQueue of this SchedulerContext;
	 * 
	 * @return the internal singleTaskOrderQueue of this SchedulerContext
	 */
	private List<SingleTaskOrder> getSingleTaskOrderQueueRaw() {
		return this.singleTaskOrderQueue;
	}
	
	/** List containing all the SingleTaskOrders of this system. */
	private final List<SingleTaskOrder> singleTaskOrderQueue = new ArrayList<>();
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
		this.getCurrentSchedulingStrategy().addTo(order, this.getStandardOrderQueueRaw());
		this.notifyNewOrder();
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

		List<SingleTaskOrder> queue = this.getSingleTaskOrderQueueRaw();
		boolean hasAdded = false; 
		
		for (int i = 0; i < queue.size(); i++) {
			if (queue.get(i).getDeadline().get().compareTo(order.getDeadline().get()) > 0) {
				
				queue.add(i, order);
				hasAdded = true;
				break;
			}
		}
		if (!hasAdded) {
			queue.add(order);
		}
		this.notifyNewOrder();
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Check if the specified Order is a valid pending Order to be scheduled.
	 * 
	 * @return | order != null && !order.isCompleted()
	 */
	public boolean isValidPendingOrder(Order order) {
		return order != null && !order.isCompleted();
	}

	//--------------------------------------------------------------------------
	// OrderSubject methods.
	//--------------------------------------------------------------------------
	@Override
	public void attachOrderObserver(OrderObserver o) throws IllegalArgumentException {
		if (o == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		
		if (!this.getOrderObserversRaw().contains(o)) { 
			this.getOrderObserversRaw().add(o); 
		}
	}

	@Override
	public void detachOrderObserver(OrderObserver o) throws IllegalArgumentException {
		if (o == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		this.getOrderObserversRaw().remove(o);
	}

	@Override
	public void notifyNewOrder() {
		for(OrderObserver o: this.getOrderObserversRaw()) {
			o.notifyOrder(this);
		}
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Get the raw instance of the OrderObservers of this OrderSubject.
	 * 
	 * @return a raw instance of the OrderOBservers of this OrderSubject.
	 */
	private List<OrderObserver> getOrderObserversRaw() {
		return this.orderObservers;
	}
	
	/** The OrderObservers of this OrderSubject. */
	private final List<OrderObserver> orderObservers = new ArrayList<>();
}