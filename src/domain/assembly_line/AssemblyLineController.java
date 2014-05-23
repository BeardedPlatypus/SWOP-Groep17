package domain.assembly_line;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import domain.DateTime;
import domain.assembly_line.virtual.VirtualAssemblyLine;
import domain.car.Model;
import domain.clock.Clock;
import domain.clock.EventActor;
import domain.clock.EventConsumer;
import domain.order.Order;
import domain.production_schedule.OrderObserver;
import domain.production_schedule.OrderRequest;
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
	public AssemblyLineController(SchedulerContext schedulerContext, 
								  Clock clock) {
		if (clock == null) {
			throw new IllegalArgumentException("Clock cannot be null.");
		}
		if (schedulerContext == null) {
			throw new IllegalArgumentException("schedulerContext cannot be null.");
		}
		
		this.schedulerContext = schedulerContext;
		this.clock = clock;
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
	
	/**
	 * Set the AssemblyLine of this AssemblyLineController to newAssemblyLine.
	 * 
	 * @param newAssemblyLine
	 * 		The new AssemblyLine of this AssemblyLineController.
	 * 
	 * @postcondition | (new this).getAssemblyLineController == newAssemblyLine
	 * 
	 * @throws IllegalArgumentException
	 * 		| newAssemblyLine == null
	 */
	void setAssemblyLine(AssemblyLine newAssemblyLine) throws IllegalArgumentException {
		if (newAssemblyLine == null) {
			throw new IllegalArgumentException("potato cannot be null.");
		}
		this.assemblyLine = newAssemblyLine;
	}
	
	/** The AssemblyLine of this AssemblyLineController. */
	private AssemblyLine assemblyLine;
	
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
		DateTime currentTime = this.getClock().getCurrentTime();  
		
		// Check if day should end.
		if (timeExceedsToday(currentTime)) {
			this.wrapUpDay(currentTime);
		} else if (this.getAssemblyLine().getCurrentState().acceptsOrders()) {
			VirtualAssemblyLine virt = this.getAssemblyLine().newVirtualAssemblyLine();
			List<Order> resultOrders = new ArrayList<>();
			
			Optional<Order> deadline = this.requestDeadlineOrder();
			if (deadline.isPresent()) {
				if (mustScheduleDeadline(currentTime, deadline.get())) {
					this.scheduleDeadline(deadline, virt, resultOrders, currentTime);
				} else {
					Optional<Order> standardOrder = this.requestStandardOrder();
					if (standardOrder.isPresent()) {
						List<Order> l = Lists.newArrayList(standardOrder.get());
						DateTime timeToSchedule = virt.timeToFinish(l);
						
						if (!timeExceedsToday(currentTime.addTime(timeToSchedule))) {
							resultOrders.add(standardOrder.get());
							resultOrders = this.addSingleTaskOrders(resultOrders, virt);
							this.advance(resultOrders);							
						} else {
							this.scheduleDeadline(deadline, virt, resultOrders, currentTime);						
						}
					} else {
						this.scheduleDeadline(deadline, virt, resultOrders, currentTime);					
					}
				}
			} else {
				Optional<Order> standardOrder = this.requestStandardOrder();
				
				if (standardOrder.isPresent()) {
					List<Order> inputOrders = new ArrayList<>();
					inputOrders.add(standardOrder.get());
					DateTime timeToSchedule = virt.timeToFinish(inputOrders);
					
					if (!timeExceedsToday(currentTime.addTime(timeToSchedule))) {
						// Can schedule standardOrder
						resultOrders.add(standardOrder.get());
						this.advance(resultOrders);
					} else {
						this.wrapUpDay(currentTime);
					}
				} else {
					// no orders available
					if (this.getAssemblyLine().isEmpty()) 
						// no orders and empty band -> go to idle.
						this.goToIdle();
					else {
						// advance the line without orders.
						this.advance(new ArrayList<Order>());
					}
				}
			}			
		} else {
			//AssemblyLine does not accept orders.
			this.advance(new ArrayList<Order>());
		}
	}
	
	protected void scheduleDeadline(Optional<Order> deadline, 
			VirtualAssemblyLine virt, 
			List<Order> resultOrders,
			DateTime currentTime) {
		List<Order> m = Lists.newArrayList(deadline.get()); 
		DateTime timeToScheduleDeadLine = virt.timeToFinish(m);

		if (!timeExceedsToday(currentTime.addTime(timeToScheduleDeadLine))) {
			resultOrders.add(deadline.get());
			resultOrders = this.addSingleTaskOrders(resultOrders, virt);
			this.advance(resultOrders);
		} else {
			resultOrders = this.addSingleTaskOrders(resultOrders, virt);
			if (resultOrders.isEmpty()) {	
				this.wrapUpDay(currentTime);
			} else {
				this.advance(resultOrders);
			}
		}						
	}
	
	protected List<Order> addSingleTaskOrders(List<Order> resultOrders,  
			                                  VirtualAssemblyLine virt) {
		DateTime t = virt.timeToFinish(resultOrders);
		
		List<TaskType> taskTypes = this.getAssemblyLine().getTaskTypes();
		taskTypes = Lists.reverse(taskTypes);
		
		List<Order> result = new ArrayList<Order>();
		
		for (TaskType task : taskTypes) {
			TaskType[] tl = {task};
			Optional<Order> o = this.getSchedulerContext().getOrder(new OrderRequest(tl));
			
			if (o.isPresent()) {
				List<Order> temp = new ArrayList<Order>(resultOrders);
				temp.add(o.get());
				temp.addAll(result);
				
				DateTime potato = virt.timeToFinish(temp);
				if (potato.equals(t)) {
					result.add(0, o.get());
				}
			}
		}
		List<Order> returnResults = new ArrayList<Order>(resultOrders);		
		returnResults.addAll(result);
		return returnResults;
	}

	protected void advance(List<Order> l) {
		for (Order o : l) {
			this.getSchedulerContext().removeOrder(o);
		}
		
		this.getAssemblyLine().advance(Lists.reverse(l));
	}
	
	//--------------------------------------------------------------------------
	// Potato methods
	//--------------------------------------------------------------------------
	public void setToOperational() {
		if (this.getAssemblyLine().getCurrentState().canRestoreToOperational()) {
			this.getEventConsumer().unregister(this);
			this.getEventConsumer().register(this);
			
			this.activate();
		}
	}
		
	//--------------------------------------------------------------------------
	// State-related methods.
	//--------------------------------------------------------------------------
	public void changeState(AssemblyLineState state) {
		this.setNextState(Optional.of(state));
	}
	
	private void switchState() {
		this.getAssemblyLine().setCurrentState(this.getNewState().get());
		this.setNextState(Optional.<AssemblyLineState> absent());
	}
	
	private boolean hasNewState() {
		return this.getNewState().isPresent();
	}
	
	private Optional<AssemblyLineState> getNewState() {
		return this.newState;
	}
	
	private void setNextState(Optional<AssemblyLineState> state) {
		if (state == null) {
			throw new IllegalArgumentException("Thomas is niet lief.");
		}
		
		this.newState = state;
	}
	
	/** State that should be updated next step. */
	private Optional<AssemblyLineState> newState = Optional.<AssemblyLineState> absent();
	
	//--------------------------------------------------------------------------
	// OrderSchedule Related methods.
	//--------------------------------------------------------------------------
	protected boolean timeExceedsToday(DateTime t) {
		int timeLeftMinutes = (FINISHHOUR * 60 - this.getOverTime()) - 
				t.getHours() * 60 + t.minutes;
		return timeLeftMinutes < 0; 
	}
	
	protected boolean mustScheduleDeadline(DateTime curTime, Order o) {
		 return curTime.getDays() >= o.getDeadline().get().getDays() - 1;
	}
	
	/**
	 * Request a StandardOrder from the SchedulerContext of this AssemblyLineController, 
	 * that can be scheduled on this AssemblyLineController's AssemblyLine.
	 * 
	 * @return An StandardOrder from the SchedulerContext of this AssemblyLineController.
	 */
	protected Optional<Order> requestStandardOrder() {
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
	protected Optional<Order> requestDeadlineOrder() {
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
	protected Optional<Order> requestSingleTaskOrder(TaskType taskType) throws IllegalArgumentException {
		if (taskType == null || !this.getAssemblyLine().getTaskTypes().contains(taskType)) {
			throw new IllegalArgumentException("Potato.");
		}
		
		TaskType[] t = {taskType};
		return this.getSchedulerContext().getOrder(new OrderRequest(t));
	}
	
	//--------------------------------------------------------------------------
	// Scheduling
	//--------------------------------------------------------------------------
	protected void wrapUpDay(DateTime curTime) { 
		if (this.getAssemblyLine().isEmpty()) {
			this.scheduleEndDay(curTime);
		} else {
			this.getAssemblyLine().advance(new ArrayList<Order>());
		}
	}

	protected void scheduleEndDay(DateTime curTime) {
		// calculate overtime.
		int newOverTime = this.getOverTime() - (WORKHOURS * 60 - 
				((curTime.getHours() - STARTHOUR) * 60 + curTime.getMinutes()));
		this.setOverTime(newOverTime);

		// schedule event next day.
		DateTime timeTillNextDay = (new DateTime(curTime.days + 1, STARTHOUR, 0)).subtractTime(curTime);
		this.getEventConsumer().constructEvent(timeTillNextDay, this);
	}
	
	/**
	 * Get the EventConsumer that this AssemblyLineController submits its events to
	 * 
	 * @return The EventConsumer that this AssemblyLineController submits its events.
	 */
	private EventConsumer getEventConsumer() {
		return this.getClock();
	}
	
	/**
	 * Get the Clock of this system.
	 * 
	 * @return The Clock of this sytem.
	 */
	private Clock getClock() {
		return this.clock;
	}
	
	/** The Clock of this AssemblyLineController. */
	private final Clock clock;
	
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
		//unsubscribe from clock.
		this.getEventConsumer().unregister(this);
		
		this.getAssemblyLine().setCurrentState(new IdleState());
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
		this.getEventConsumer().register(this);
		//advance asemblyline
		List<Order> l = new ArrayList<>();
		l.add(order);
		this.getAssemblyLine().advance(l);
		
		this.getAssemblyLine().setCurrentState(new ActiveState());
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
			this.getSchedulerContext().removeOrder(standardOrder.get());
			this.goToActive(standardOrder.get());
		} else {
			Optional<Order> singleTaskOrder = this.requestDeadlineOrder();
			if (singleTaskOrder.isPresent()) {
				this.getSchedulerContext().removeOrder(singleTaskOrder.get());
				this.goToActive(singleTaskOrder.get());
			}
		}
	}
}
