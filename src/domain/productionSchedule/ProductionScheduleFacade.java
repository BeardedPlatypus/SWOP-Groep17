package domain.productionSchedule;

import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.Model;
import domain.Option;
import domain.order.Order;
import domain.order.OrderContainer;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.productionSchedule.strategy.SchedulingStrategy;

/** 
 * The ProductionScheduleFacade provides an interface to the ProductionSchedule
 * subsystem. It provides the basic methods the rest of the domain can use to 
 * interact with this ProductionSchedule.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class ProductionScheduleFacade {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new ProductionScheduleFacade with the specified ClockManager
	 * and SchedulerContext.
	 * 
	 * @param clock
	 * 		The ClockManager of this new ProductionScheduleFacade
	 * @param sched
	 * 		The ProductionSchedule of this new ProductionScheduleFacade
	 * 
	 * @throws IllegalArgumentException 
	 * 		| clock == null || sched == null
	 */
	public ProductionScheduleFacade(ClockManager clock, 
									SchedulerContext sched) {
		if (clock == null)
			throw new IllegalArgumentException("Clock cannot be null.");
		if (sched == null) 
			throw new IllegalArgumentException("Scheduler cannot be null.");
		
		this.clockManager = clock;
		this.schedulerContext = sched;
		
	}
	
	//--------------------------------------------------------------------------
	// Manufacturer
	//--------------------------------------------------------------------------
	/**
	 * Set the Manufacturer of this ProductionScheduleFacade to the specified manufacturer. 
	 * 
	 * @param manufacturer
	 * 		The new manufacturer of this ProductionScheduleFacade 
	 * 
	 * @postcondition | (new this).getManufacturer() == manufacturer.
	 * 
	 * @throws IllegalArgumentException
	 * 		| manufacturer == null
	 * @throws IllegalStateException 
	 * 		| manufacturer.getProductionSchedule() == this 
	 * @throws IllegalStateException
	 * 		manufacturer has already been set.
	 */
	public void setManufacturer(Manufacturer manufacturer) throws IllegalStateException {
		if (manufacturer == null)
			throw new IllegalArgumentException("Manufacturer cannot be null.");
		if (manufacturer.getProductionSchedule() != this)
			throw new IllegalStateException("manufacturer.getOrderFactory() does not match this.");
		if (this.manufacturer != null) 
			throw new IllegalStateException("manufacturer has already been set.");
		
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Get the Manufacturer of this OrderFactory. 
	 * 
	 * @return The Manufacturer of this OrderFactory.
	 * 
	 * @throws IllegalArgumentException
	 * 		Manufacturer has not been set.
	 */
	public Manufacturer getManufacturer() {
		if (this.manufacturer == null)
			throw new IllegalStateException();
		
		return this.manufacturer;
	}

	/** The Manufacturer of this OrderFactory. */
	private Manufacturer manufacturer = null;

	//==========================================================================
	// SchedulerContext Methods
	//==========================================================================
	//--------------------------------------------------------------------------
	// Order related methods.
	//--------------------------------------------------------------------------	
	/**
	 * Get the pending StandardOrder OrderContainers of this ProductionScheduleFacade. 
	 * 
	 * @return The pending StandardOrders of this ProductionSchedule.
	 * 	       | ALL o: o == OrderContainer && o in this 
	 */
	public List<OrderContainer> getPendingStandardOrderContainers() {
		return this.getSchedulerContext().getPendingStandardOrders();
	}	
	
	/**
	 * Check if the specified order is an element of this ProductionSchedule.
	 * 
	 * @param order
	 * 		The Specified order that should be checked.
	 * 
	 * @return | order in this
	 * 
	 * @throws IllegalArgumentException
	 * 		| order == null
	 */
	public boolean contains(OrderContainer order) throws IllegalArgumentException {
		return this.getSchedulerContext().containsOrder(order);
	}

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
	public void submitStandardOrder(StandardOrder order) throws IllegalArgumentException{
		this.getSchedulerContext().addNewStandardOrder(order);
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
	public void submitSingleTaskOrder(SingleTaskOrder order) throws IllegalArgumentException {
		this.getSchedulerContext().addNewSingleTaskOrder(order);
	}
	
	
	/**
	 * Check if the specified Order is a valid pending Order to be scheduled.
	 * 
	 * @return | order != null && !order.isCompleted()
	 */
	public boolean isValidPendingOrder(Order order) {
		return this.getSchedulerContext().isValidPendingOrder(order);
	}
	
	//--------------------------------------------------------------------------
	// Strategy-related methods
	//--------------------------------------------------------------------------
	/** 
	 * Set the current scheduling strategy of this SchedulerContext to the specified 
	 * SchedulingStrategy.
	 * 
	 * @param newStrategy 
	 * 		The new strategy of this SchedulerContext;
	 * 
	 * @postcondition | (new this).getSchedulingStrategy() == newStrategy
	 */
	public void setNewSchedulingAlgorithm(SchedulingStrategy strat) {
		this.getSchedulerContext().setSchedulingStrategy(strat);
	}
	
	/**
	 * Get the current scheduling strategy of this SchedulerContext. 
	 * 
	 * @return the current scheduling strategy of this SchedulerContext.
	 */
	public SchedulingStrategy getCurrentSchedulingAlgorithm() {
		return this.getSchedulerContext().getCurrentSchedulingStrategy();
	}
	
	//--------------------------------------------------------------------------
	// Context
	//--------------------------------------------------------------------------
	/** 
	 * Get the SchedulerContext of this ProductionScheduleFacade. 
	 * 
	 * @return the SchedulerContext of this ProductionScheduleFacade.
	 */
	SchedulerContext getSchedulerContext() {
		return this.schedulerContext;
	}
	
	/** The SchedulerContext of this ProductionScheduleFacade. */
	private final SchedulerContext schedulerContext;
	
	//--------------------------------------------------------------------------
	// Intermediate methods
	//--------------------------------------------------------------------------
	/** 
	 * Get the estimated completion time of the specified order in this ProductionSchedule.
	 * 
	 * @param order
	 * 		The specified order of which the estimated completion time is requested.
	 * 
	 * @return The estimated completion time of the specified order.
	 * 
	 * @throws IllegalArgumentException
	 * 		| order == null || !this.contains(order)
	 */
	public DateTime getEstimatedCompletionTime(OrderContainer order) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
	
	//==========================================================================
	// Clock Methods
	//==========================================================================
	/**
	 * Get the current DateTime of this ClockManager.
	 * 
	 * @return the current DateTime of this ClockManager.
	 */
	public DateTime getCurrentTime() {
		return this.getClockManager().getCurrentTime();
	}
	
	/** @see {@link TimeObserver} */
	public void attachTimeObserver(TimeObserver t) throws IllegalArgumentException {
		this.getClockManager().attachTimeObserver(t);
	}
	
	/** @see {@link TimeObserver} */
	public void detachTimeObserver(TimeObserver t) throws IllegalArgumentException {
		this.getClockManager().detachTimeObserver(t);
	}	
	//--------------------------------------------------------------------------
	// Clock variables
	//--------------------------------------------------------------------------
	/** 
	 * Get the ClockManager of this ProductionScheduleFacade
	 * 
	 * @return the ClockManager of this ProductionScheduleFacade.
	 */
	ClockManager getClockManager() {
		return this.clockManager;
	}
	
	/** The ClockManager of this ProductionScheduleFacade. */
	private final ClockManager clockManager;


}