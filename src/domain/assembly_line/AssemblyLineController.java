package domain.assembly_line;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Model;
import domain.clock.EventActor;
import domain.clock.EventConsumer;
import domain.order.Order;
import domain.production_schedule.OrderObserver;
import domain.production_schedule.OrderRequest;
import domain.production_schedule.OrderSubject;
import domain.production_schedule.SchedulerContext;

/**
 * The AssemblyLineController manages an AssemblyLine. It selects the orders to
 * schedule, manages state changes and requests next day events.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class AssemblyLineController implements EventActor, OrderObserver {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	public AssemblyLineController(AssemblyLine assemblyLine) {
		this.assemblyLine = assemblyLine;
	}
	
	//--------------------------------------------------------------------------
	// AssemblyLine-related methods.
	//--------------------------------------------------------------------------
	/**
	 * Get the AssemblyLine of this AssemblyLineController.
	 * 
	 * @return The AssemblyLine of this AssemblyLineController.
	 */
	protected AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}
	
	/** The AssemblyLine of this AssemblyLineController. */
	private final AssemblyLine assemblyLine;
	
	//--------------------------------------------------------------------------
	// Manufacturer
	//--------------------------------------------------------------------------
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The manufacturer of this AssemblyLineController. */
	private final Manufacturer manufacturer;
	
	//--------------------------------------------------------------------------
	// SchedulerContext
	//--------------------------------------------------------------------------
	/** 
	 * Get the SchedulerContext from which this AssemblyLineController requests
	 * its Orders.
	 * 
	 * @return The SchedulerContext from which this AssemblyLineController requests its Orders.
	 */
	private SchedulerContext getSchedulerContext() {
		return this.schedulerContext;
	}
	
	/** The SchedulerContext from which this AssemblyLineController requests its orders. */
	private final SchedulerContext schedulerContext;
	//--------------------------------------------------------------------------
	// Activate methods.
	//--------------------------------------------------------------------------
	@Override
	public void activate() {
		// Check for state change. 
		if (this.hasNewState()) {
			this.switchState();
		}
		
		// Get time. 
		DateTime currentTime = null; //TODO get time here. 
		int timeLeftMinutes = (FINISHHOUR * 60 - this.getOverTime()) - 
				currentTime.getHours() * 60 + currentTime.minutes;
		
		// Check if day should end.
		if (timeLeftMinutes < 0) {
			if (this.getAssemblyLine().isEmpty()) {
				this.scheduleEndDay(currentTime);
			} else {
				this.getAssemblyLine().advance(new ArrayList<Order>());
			}
		} else {
			if (this.getAssemblyLine().getCurrentState().acceptsOrders()) {
				
			} else {
				this.getAssemblyLine().advance(new ArrayList<Order>());
			}
		}
		
	}
	
	//--------------------------------------------------------------------------
	// Is current or in the future
	//--------------------------------------------------------------------------
	
	/** 
	 * Set this AsemblyLine to the specified new isCurrent state.
	 * 
	 * @param newIsCurrent
	 * 		The new state of this AssemblyLine's isCurrent.
	 * 
	 * @postscondition | (new this).isCurrent == newIsCurrent
	 */
	private void setIsCurrent(boolean newIsCurrent) {
		this.isCurrent = newIsCurrent;
	}
	
	/** If this AssemblyLine is current and active, or disabled because it is in the future. */
	private boolean isCurrent = true;
	
	//--------------------------------------------------------------------------
	// State-related methods.
	//--------------------------------------------------------------------------
	public void restoreToOperationalState() {
		
	}
	
	private void switchState() {
		this.getAssemblyLine().setCurrentState(this.getNewState().get());
	}
	
	private boolean hasNewState() {
		return this.getNewState().isPresent();
	}
	
	private Optional<AssemblyLineState> getNewState() {
		return this.newState;
	}
	
	/** State that should be updated next step. */
	private Optional<AssemblyLineState> newState = Optional.<AssemblyLineState> absent();
	
	//--------------------------------------------------------------------------
	// OrderSchedule Related methods.
	//--------------------------------------------------------------------------
	protected List<Order> calculateOrdersToSchedule() {
		return null;
	}

	/**
	 * Request a StandardOrder from the SchedulerContext of this AssemblyLineController, 
	 * that can be scheduled on this AssemblyLineController's AssemblyLine.
	 * 
	 * @return An StandardOrder from the SchedulerContext of this AssemblyLineController.
	 */
	private Optional<Order> requestStandardOrder() {
		List<Model> models = this.getAssemblyLine().getAcceptedModels();
		Model[] m = new Model[models.size()];
		return this.getSchedulerContext().getOrder(new OrderRequest(models.toArray(m)));
	}
	
	/**
	 * Request the SingleTaskOrder with the next deadline from the SchedulerContext
	 * of this AssemblyLineController, that can be scheduled on this 
	 * AssemblyLineController's AssemblyLine.
	 * 
	 * @return The SingleTaskOrder with the next deadline from the SchedulerContext.
	 */
	private Optional<Order> requestDeadlineOrder() {
		List<TaskType> taskTypes = this.getAssemblyLine().getTaskTypes();
		TaskType[] t = new TaskType[taskTypes.size()];
		return this.getSchedulerContext().getOrder(new OrderRequest(taskTypes.toArray(t)));
	}
	
	/**
	 * Request a SingleTaskOrder of the specified tasktype from the SchedulerContext
	 * of this AssemblyLineController.
	 * 
	 * @param taskType
	 * 		The taskTye of the requested SingleTaskOrder.
	 * @return The SingleTaskOrder of the specified TaskType from the SchedulerContext.
	 * 
	 * @throws IllegalArgumentException | taskType == null || !this.getAssemblyLine.tasktypes contains taskType
	 */
	private Optional<Order> requestSingleTaskOrder(TaskType taskType) throws IllegalArgumentException {
		if (taskType == null || !this.getAssemblyLine().getTaskTypes().contains(taskType)) {
			throw new IllegalArgumentException("Potato.");
		}
		
		TaskType[] t = {taskType};
		return this.getSchedulerContext().getOrder(new OrderRequest(t));
	}
	
	//--------------------------------------------------------------------------
	// Scheduling
	//--------------------------------------------------------------------------
	protected void scheduleEndDay(DateTime curTime) {
		// calculate overtime.
		int newOverTime = this.getOverTime() - (WORKHOURS * 60 - 
				((curTime.getHours() - STARTHOUR) * 60 + curTime.getMinutes()));
		this.setOverTime(newOverTime);

		// schedule event next day.
		DateTime timeTillNextDay = (new DateTime(curTime.days + 1, STARTHOUR, 0)).subtractTime(curTime);
		this.getEventConsumer().constructEvent(timeTillNextDay, this);
	}
	
	private EventConsumer getEventConsumer() {
		return this.eventConsumer;
	}
	
	private final EventConsumer eventConsumer;
	
	//--------------------------------------------------------------------------
	// Overtime
	//--------------------------------------------------------------------------
	/**
	 * Get the over time in minutes of this AssemblyLineController.
	 * 
	 * @return the OverTime in minutes of this AssemblyLineController.
	 */
	protected int getOverTime() {
		return this.overTime;
	}
	
	/**
	 * Set the overtime in minutes of this AssemblyLineController to newOverTime. 
	 * 
	 * @param newOverTime
	 * 		The new overtime in minutes of this AssemblyLineController.
	 * 
	 * @postcondition | newOverTime < 0 -> (new this).getOverTime() == 0
	 * @postcondition | otherwise       -> (new this).getOverTime() == newOverTime  
	 */
	protected void setOverTime(int newOverTime) {
		this.overTime = Math.max(0, newOverTime);
	}
	
	/** The current overTime in minutes of this AssemblyLineController. */
	private int overTime;
	
	//--------------------------------------------------------------------------
	/** Start of a workday. */
	private final static int STARTHOUR = 6;
	/** End of a workday. */
	private final static int FINISHHOUR = 22;
	/** Number of workhours in a shift. */
	private final static int WORKHOURS = FINISHHOUR - STARTHOUR;
	
	//--------------------------------------------------------------------------
	// Idle methods.
	//--------------------------------------------------------------------------
	/**
	 * Set the state of this AssemblyLineController to idle.
	 * This AssemblyLineController starts observing the SchedulerContext for new Orders.
	 * 
	 * @postcondition | (new this).isIdle == true
	 * @postcondition | (new this) observes this.getSchedulerContext()
	 */
	protected void goToIdle() {
		this.setIdle(true);
		
		//start observing scheduler.
		this.getSchedulerContext().attachOrderObserver(this);
	}
	
	/**
	 * Set the state of this AssemblyLineContraller to active (opposite of idle).
	 * This AssemblyLineController stops observing the SchedulerContext for new Orders.
	 * 
	 * @param order
	 * 		The new order that will be added to the this AssemblyLine.
	 */
	protected void goToActive(Order order) {
		//remove old references
		this.setIdle(false);
		this.getSchedulerContext().detachOrderObserver(this);
		//advance asemblyline
		
		List<Order> l = new ArrayList<>();
		l.add(order);
		this.getAssemblyLine().advance(l);
	}
	
	//--------------------------------------------------------------------------
	/** 
	 * Check if this AssemblyLine is idle.
	 * 
	 * @return if this AssemblyLine is idle.
	 */
	public boolean isIdle() {
		return this.isIdle;
	}
	
	/**
	 * Set the idle state of this AssemblyLineController to newIdleState.
	 * 
	 * @param newIdleState
	 * 		The new idle state of this AssemblyLineController
	 * 
	 * @postcondition | (new this).isIdle == newIdleState
	 */
	private void setIdle(boolean newIdleState) {
		this.isIdle = newIdleState;
	}
	
	/** If this AssemblyLine is idle. */
	private boolean isIdle = false;


	@Override
	public void notifyOrder() throws IllegalArgumentException { //FIXME maybe not too nice to assume an order can only have one Scheduler.
		Optional<Order> standardOrder = this.requestStandardOrder();
		if (standardOrder.isPresent()) {
			this.goToActive(standardOrder.get());
		} else {
			Optional<Order> singleTaskOrder = this.requestDeadlineOrder();
			if (singleTaskOrder.isPresent()) {
				this.goToActive(singleTaskOrder.get());
			}
		}
	}
}
