package domain.productionSchedule;

import domain.Model;
import domain.Specification;
import domain.TaskType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import domain.DateTime;
import domain.assemblyLine.OrderObserver;
import domain.assemblyLine.OrderSubject;
import domain.order.Order;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.SingleTaskCatalog;
import domain.order.OrderContainer;

/**
 * The SchedulerContext handles the scheduling of all orders of this system.
 * It provides methods for getting new AssemblyProcedures, ordering the orders, 
 * checking if orders can be scheduled. 
 * It is also responsible for keeping track of and minimising overtime. 
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class SchedulerContext implements TimeObserver, OrderSubject {
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
	public SchedulerContext(DateTime initTime, int overTime, 
			                Comparator<Order> defaultStrategy,
			                List<TaskType> taskCategories) throws IllegalArgumentException {
		//Scheduling variables
		this.defaultStrategy = defaultStrategy;
		
		// Init SingleTaskOrder queues
		for (TaskType t: taskCategories) {
			this.taskOrderQueue.put(t, new ArrayList<SingleTaskOrder>());
		}
		
		// TimeObserver Related variables. 
		this.setCurrentTime(initTime);
		this.setOverTime(overTime);
	}
	
	//--------------------------------------------------------------------------
	// Strategy-related methods
	//--------------------------------------------------------------------------
	/**
	 * Get the current scheduling strategy of this SchedulerContext. 
	 * 
	 * @return the current scheduling strategy of this SchedulerContext.
	 */
	public Comparator<Order> getSchedulingStrategy() {
		throw new UnsupportedOperationException();
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
	public void setSchedulingStrategy(Comparator<Order> newStrategy) {
		throw new UnsupportedOperationException();
	}
	
	/** The current strategy of this SchedulerContext. */
	private Comparator<Order> currentStrategy;
	
	//--------------------------------------------------------------------------
	
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
	//TODO add defensive style here
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
	/**
	 * Get the default ordering strategy (Comparator<Order>) of this 
	 * SchedulerContext.
	 *  
	 * @return the default ordering strategy of this SchedulerContext.
	 */
	protected Comparator<Order> getDefaultStrategy() {
		return this.defaultStrategy;
	}
	
	/** The default strategy of this SchedulerContext. */
	private final Comparator<Order> defaultStrategy;
	
	//--------------------------------------------------------------------------
	// Scheduling related methods.
	//--------------------------------------------------------------------------
	/**
	 * Check if it is still possible to schedule an Order today.
	 * 
	 * @param timePerAdvance
	 * 
	 * @return True if an order can still be scheduled, false otherwise.
	 */
	public boolean canScheduleToday(int[] timePerAdvance) {
		int currentDayMinutes = this.getCurrentTime().hours * 60 + 
				                this.getCurrentTime().minutes;

		//Test if the end of the day has already passed.
		if (currentDayMinutes >= FINISHHOUR * 60 - this.getOverTime())
			return false;
		
		//Test if a full order can still be scheduled. 
		
		//Test if a custom order can still be scheduled. 
		
		
		return false;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Get the current estimated completion time of the specified OrderContainer.
	 * 
	 * @param order
	 * 		The order of which the estimated completion time should be obtained.
	 * 
	 * @return The estimated completion time of the specified order.
	 * 
	 * @throws IllegalStateException
	 * 		| !this.containsOrder(order)
	 */
	public DateTime getEstimatedCompletionTime(OrderContainer order) {
		throw new UnsupportedOperationException();
	}
	
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
	
	//--------------------------------------------------------------------------
	// Make order methods.
	//--------------------------------------------------------------------------
	//FIXME old functionality.
	/**
	 * create from the specified model and specs and internal time a new
	 * order object and add this to the pendingOrders of this ProductionSchedule.
	 * 
	 * @param model
	 * 		The model of this new Order
	 * @param specs
	 * 		The specification of this new Order. 
	 * 
	 * @postcondition
	 * 		| o = new (Order(model, specs) && (new this).getPendingOrders().last = o 
	 * 
	 * @throws NullPointerException 
	 * 		| model == null || specs == null
	 */
	public void addNewOrder(Model model, Specification specs) throws NullPointerException, IllegalArgumentException{
		//TODO check if this can be removed, should be checked at a different system.
//		if (!model.isValidSpecification(specs)) {
//			throw new IllegalArgumentException("invalid specification.");
//		}
		
		Order newOrder = makeNewStandardOrder(model, specs, 
				                              this.getCurrentOrderIdentifier(),
				                              this.getCurrentTime());		
		this.addToPendingOrders(newOrder);
		this.incrementOrderIdentifier();
	}

	/** Isolated StandardOrder Constructor, mostly for testing purposes. */
	protected StandardOrder makeNewStandardOrder(Model model, Specification specs, 
			int orderNumber, DateTime submissionTime) {
		return new StandardOrder(model, specs, orderNumber, submissionTime);
	}
	
	//--------------------------------------------------------------------------
	public void addNewSingleTaskOrder(Model model, Specification specs, DateTime deadline) {
		throw new UnsupportedOperationException();
	}
	
	/** Isolated SingleTaskOrder Constructor, mostly for testing purposes. */
	protected SingleTaskOrder makenNewSingleTaskOrder(Model model, 
			                                          Specification specification, 
			                                          int orderNumber, 
			                                          DateTime submissionTime, 
			                                          DateTime deadline) {
		return new SingleTaskOrder(model, specification, orderNumber, submissionTime, deadline);
	}
	
	//--------------------------------------------------------------------------
	/** 
	 * Get the current order identifier of this ProductionSchedule.
	 * 
	 * @return The current order identifier of this ProductionSchedule.
	 */
	private int getCurrentOrderIdentifier() {
		return this.currentIdentifier;
	}
	
	/**
	 * Increment the current order identifier of this ProductionSchedule by one. 
	 * 
	 * @effect | this.setCurrentIdentifier(this.getCurrentIdentifier() + 1)
	 */
	private void incrementOrderIdentifier() {
		this.setOrderIdentifier(this.getCurrentOrderIdentifier() + 1);
	}
	
	/**
	 * Set the currentOrderIdentifier of this ProductionSchedule to newIdentifier.
	 * 
	 * @param newIdentifier
	 * 		The new value of the currentIdentifier.
	 * 
	 * @postcondition | (new this).getCurrentIdentifier() == newIdentifier.
	 */
	private void setOrderIdentifier(int newIdentifier) {
		this.currentIdentifier = newIdentifier;
	}
	
	/** The next unused order identifier issued by this ProductionSchedule. */
	private int currentIdentifier;
	
	//--------------------------------------------------------------------------
	// TimeObserver related methods.
	//--------------------------------------------------------------------------
	/** 
	 * Set the current DateTime to the observed DateTime. If a new day has 
	 * occurred, the overtime is updated. 
	 * 
	 * @postcondition | (new this).getCurrentTime() == time
	 * @postcondition | time.days == this.getCurrentTime().days + 1 &&
	 *                | time.hours == START_HOUR -> 
	 *                | (new this).overtime == this.overtime - (WORKHOURS - workedHours)
	 */
	@Override
	public void update(DateTime time) throws IllegalArgumentException {
		if (time == null) {
			throw new IllegalArgumentException("time cannot be null");
		}
		
		// Check for new day
		DateTime curTime = this.getCurrentTime();
		
		if (time.getHours() == STARTHOUR && 
			time.getDays() == curTime.getDays() + 1) {
			int overTime = this.getOverTime() -                              // current overtime minus    
					       (WORKHOURS * 60 -                                 // the time in a day minus
					    		   ((curTime.getHours() - STARTHOUR) * 60 +     // the time used.
					    				   curTime.getMinutes()));
			this.setOverTime(overTime);
		}
		
		// Set new time.
		this.setCurrentTime(time);
	}	
	
	//--------------------------------------------------------------------------
	/** 
	 * Get the current DateTime of this SchedulerContext. 
	 * 
	 * @return the current DateTime of this ScheduleContext.
	 */
	protected DateTime getCurrentTime() {
		return this.currentTime;
	}
	
	/**
	 * Set the current DateTime to the Specified DateTime.
	 * 
	 * @param time
	 * 		The DateTime to which the new current time is said. 
	 * 
	 * @postcondition | (new this).getCurrentTime() == time
	 * 
	 * @throws IllegalArgumentException
	 * 		| time == null
	 */
	protected void setCurrentTime(DateTime time) {
		if (time == null) {
			throw new IllegalArgumentException("time cannot be null.");
		}
		
		this.currentTime = time;
		
	}
	
	/** The current DateTime of this ScheduleContext */
	private DateTime currentTime;
	
	//--------------------------------------------------------------------------	
	/**
	 * Get the over time in minutes of this ProductionSchedule.
	 * 
	 * @return the OverTime in minutes of this ProductionSchedule.
	 */
	protected int getOverTime() {
		return this.overTime;
	}
	
	/**
	 * Set the overtime in minutes of this ProductionSchedule to newOverTime. 
	 * 
	 * @param newOverTime
	 * 		The new overtime in minutes of this ProductionSchedule.
	 * 
	 * @postcondition | newOverTime < 0 -> (new this).getOverTime() == 0
	 * @postcondition | otherwise       -> (new this).getOverTime() == newOverTime  
	 */
	protected void setOverTime(int newOverTime) {
		this.overTime = Math.max(0, newOverTime);
	}
	
	/** The current overTime in minutes of this ProductionSchedule. */
	private int overTime;
	
	//--------------------------------------------------------------------------
	/** Start of a workday. */
	private final static int STARTHOUR = 6;
	/** End of a workday. */
	private final static int FINISHHOUR = 22;
	/** Number of workhours in a shift. */
	private final static int WORKHOURS = FINISHHOUR - STARTHOUR;

	//--------------------------------------------------------------------------
	// OrderSubject methods.
	//--------------------------------------------------------------------------
	@Override
	public void attachOrderObserver(OrderObserver t)
			throws IllegalArgumentException {
		if (t == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		
		if (!this.observers.contains(t)) { 
			this.observers.add(t);
			t.updateOrder(this);
		}		
	}

	@Override
	public void detachOrderObserver(OrderObserver t) {
		if (t == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}

		if (this.observers.contains(t)) {
			this.observers.remove(t);
		}
	}
	
	/** The OrderObservers that observe this OrderSubject */
	private final List<OrderObserver> observers = new ArrayList<>();

	//--------------------------------------------------------------------------
	@Override
	public void notifyHasOrders() {
		for(OrderObserver obs : this.getOrderObservers()) {
			obs.updateOrder(this);
		}
	}

	@Override
	public List<OrderObserver> getOrderObservers() {
		return new ArrayList<OrderObserver>(this.observers);
	}

	//TODO
	@Override
	public boolean hasOrders() {
		return false;
	}

}
