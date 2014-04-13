package domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The ProductionSchedule class. Acts as interface between the manufacturer and 
 * the AssemblyLine. Provides methods for converting a Model and (valid) 
 * specification to an Order, as well as managing the overall Time of the system.  
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 * @invariant | this.getOverTime() >= 0
 */
public class ProductionSchedule {
	//--------------------------------------------------------------------------
	// Constructors
	//--------------------------------------------------------------------------
	/**
	 * Create a new ProductionSchedule with the given manufacturer and 
	 * assemblyLine.
	 * 
	 * @param manufacturer
	 * 		The manufacturer associated with this new ProductionSchedule.
	 * @precondition manufacturer != null
	 * @precondition assemblyLine != null
	 * 
	 * @postcondition (new this).getCurrentTime() == new DateTime(0, 0, 0)
	 */
	public ProductionSchedule(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
		manufacturer.setProductionSchedule(this);
		
		this.setCurrentTime(new DateTime(0, STARTHOUR, 0));
		this.setOverTime(0);
		this.setOrderIdentifier(0);
	}
	
	/**
	 * Set the assembly line to given assembly line
	 * 
	 * @param assemblyLine
	 * 		The assemblyLine this ProductionSchedule will use to perform assemblies
	 */
	public void setAssemblyLine(AssemblyLine assemblyLine){
		this.assemblyLine = assemblyLine;
	}
	
	// FIXME assumption that it takes another hour before next move. 
	//--------------------------------------------------------------------------
	// Time related methods and Properties. 
	//--------------------------------------------------------------------------
	/**
	 * Get the estimated completion of the specified OrderContainer calculated 
	 * by the ProductionSchedule. 
	 *  
	 * @param order
	 * 		The order of which the estimated completion time is calculated.
	 * 
	 * @precondition | order != null
	 * 
	 * @return | order.isCompleted() -> order.getEstimatedCompletionTime
	 * @return a DateTime of the estimated completion time of the Order. 
	 * 
	 * @throws IllegalArgumentException | ! order.isComplete || 
	 * 									| order in this.getPendingOrderContainers()
	 */
	public DateTime getEstimatedCompletionTime(OrderContainer order) throws IllegalArgumentException {				
		
		List<OrderContainer> pendingOrders = this.getPendingOrderContainers();
		int index = pendingOrders.indexOf(order);
		if (index >= 0) {
			return this.getEstimatedCompletionTime(index + this.getAssemblyLine().getAmountOfWorkPosts());
		} else {
			index = this.getAssemblyLine().getOrderPositionOnAssemblyLine(order);
			return this.getEstimatedCompletionTime(index);
		}		
	}
				
	/**
	 * Get the estimated completion based on the current position of the order
	 * in the assembly process. 
	 * The first n positions refer to the n workstations on the assembly line,
	 * The n+m positions, with m > 0, refer to the pending order list. 
	 * 
	 * @param positionOrder
	 * 		The position of the order in the assembly process. 
	 * 
	 * @return a DateTime of the estimated completion time of the Order. 
	 * 
	 * @throws IllegalArgumentException | positionOrder < 0
	 */
	public DateTime getEstimatedCompletionTime(int positionOrder) {
		// TODO: Write this method in Scala, it would be worthy of worshipping. 
		if (positionOrder < 0) {										            // Invalid Argument
			throw new IllegalArgumentException("position smaller than 0");			//
		}																			//
		                                                                            //
		int assemblyLineSize = this.getAssemblyLine().getAmountOfWorkPosts();       //
		int estHoursForCompletion = N_HOURS_MOVE * (positionOrder + 1);    		    // add one for the actions that are currently being executed to complete.	
		DateTime timeToReturn = this.getCurrentTime();
																					//        
		if (positionOrder < assemblyLineSize) {                                     // order is on the assembly line
			return timeToReturn.addTime(0, estHoursForCompletion, 0);
		} else {                                                                    // order is in pending orders
			int timeLeft = this.timeLeftMinutes(); 
					
			if (estHoursForCompletion * 60 <= timeLeft) {                     		// Task can be finished today.
				return timeToReturn.addTime(0, estHoursForCompletion, 0);
				
			} else {								                                // Task can't be finished today.
				estHoursForCompletion -= N_HOURS_MOVE;								// We're moving to the next day, so there is no current action.
				timeToReturn = this.makeNewDateTime(timeToReturn.getDays() + 1, STARTHOUR, 0);         											// Set to return time to the next day, since we cannot finish it today.

				if (timeLeft < 0) {                                                 // there is left overtime after today, which is specified by a neg timeLeftToday
					 timeLeft = WORKHOURS * 60 + timeLeft;
					
					if(estHoursForCompletion * 60 <= timeLeft) {                    // The order can be finished within the workday of tomorrow.
						return timeToReturn.addTime(0, estHoursForCompletion, 0); 
					} else {                                                        // The order cannot be finished within the workday of tomorrow
						timeToReturn = timeToReturn.addTime(1, 0, 0);			    // We cannot finish the task tomorrow, so we will move to the day after tomorrow. 
					}
				}
				
				int nCarsMoved = Math.max(0, ((timeLeft - 60 * N_HOURS_MOVE * assemblyLineSize) / 60 * N_HOURS_MOVE) + 1); // We do not let cars be moved in a negative direction.
				int newPos =  positionOrder - nCarsMoved;                  // The position after tomorrow has ended. 
				
				int carsPerDay = (WORKHOURS - N_HOURS_MOVE * assemblyLineSize) / N_HOURS_MOVE + 1; // number of cars that can be produced on a day. + 1 is from the first car.                  
				int extraDays = newPos / carsPerDay; 					   // Number of days that need to pass before order can be executed.
				int extraHours = (newPos % carsPerDay) * N_HOURS_MOVE;     // Number of hours before car can be finished on a day.   
				
				return timeToReturn.addTime(extraDays, extraHours, 0);
			}
		}	
	}
	
	//--------------------------------------------------------------------------
	/** 
	 * Get the current time of this ProductionSchedule. 
	 * 
	 * @return The current time of this ProductionSchedule.
	 */
	public DateTime getCurrentTime() {
		return this.currentTime;
	}
	
	/**
	 * Increment the current time of this ProductionSchedule by the specified 
	 * days, hours, and minutes. 
	 * 
	 * @param days
	 * 		Days to add to the current time of this ProductionSchedule.
	 * @param hours
	 * 		Hours to add to the current time of this ProductionSchedule.
	 * @param minutes
	 * 		Minutes to add to the current time of this ProductionSchedule.
	 * 
	 * @effect this.setCurrentTime(current_time.addTime(days, hours, minutes));
	 */
	private void incrementCurrentTime(int days, int hours, int minutes) {
		this.setCurrentTime(this.getCurrentTime().addTime(days, hours, minutes));
	}
	
	/**
	 * Set the currentTime of this ProductionSchedule to dt.
	 * @param dt
	 * 		The new current time of this ProductionSchedule. 
	 * 
	 * @precondition | dt != null
	 * @postcondition | (new this).getCurrentTime() = dt
	 */
	private void setCurrentTime(DateTime dt) {
		this.currentTime = dt;
	}
	
	/** The current time of this ProductionSchedule. */
	private DateTime currentTime;

	//--------------------------------------------------------------------------
	
	/** 
	 * Create a new DateTime (abstraction of constructor DateTime
	 * 
	 * @param days
	 * 		The number of days of the new DateTime.
	 * @param hours
	 * 		The number of hours of the new DateTime
	 * @param minutes
	 * 		The number of minutes of the new DateTime. 
	 * @return a new DateTime 
	 */
	protected DateTime makeNewDateTime(int days, int hours, int minutes) {
		return new DateTime(days, hours, minutes);
	}

	// FIXME verify if these are correctly put here. 
	/** Number of workhours in a shift. */
	private final static int WORKHOURS = 16;
	/** Start of a workday. */
	private final static int STARTHOUR = 6;
	/** End of a workday. */
	private final static int FINISHHOUR = 22;
	/** Estimated number of hours to move to next station. */
	private final static int N_HOURS_MOVE = 1;
	
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
	// Order related methods and Properties. 
	//--------------------------------------------------------------------------
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
	 * @throws IllegalArgumentException
	 * 		| !model.isValidSpecifications(specs)
	 */
	public void addNewOrder(Model model, Specification specs) throws NullPointerException, IllegalArgumentException{
		if (!model.isValidSpecification(specs)) {
			throw new IllegalArgumentException("invalid specification.");
		}
		
		Order newOrder = makeNewOrder(model, specs, 
				                      this.getCurrentOrderIdentifier());		
		this.addToPendingOrders(newOrder);
		this.incrementOrderIdentifier();
	}

	/** Isolated Order Constructor, mostly for testing purposes. */
	protected Order makeNewOrder(Model model, Specification specs, int orderNumber) {
		return new Order(model, specs, orderNumber);
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Get a list of pending order containers in this ProductionSchedule. 
	 * 
	 * @return List of pending order containers in this ProductionSchedule.
	 */
	public List<OrderContainer> getPendingOrderContainers() {
		// FIXME make sure this is the best way to do this ('cause you're creating a lot of additional fluff.
		return new ArrayList<OrderContainer>(this.getPendingOrders());
	}
	
	
	/**
	 * Get a list of pending containers in this ProductionSchedule and on the assembly line. 
	 * 
	 * @return List of pending order containers in this ProductionSchedule and on the assembly line.
	 */
	public List<OrderContainer> getIncompleteOrderContainers(){
		ArrayList<OrderContainer> incompleteOrders = new ArrayList<OrderContainer>();
		incompleteOrders.addAll(this.getAssemblyLine().getActiveOrderContainers());
		incompleteOrders.addAll(this.getPendingOrderContainers());
		return incompleteOrders;
	}
	
	/**
	 * Get (a copy of the) list of pending orders in this ProductionSchedule.
	 * 
	 * @return List of pending orders in this ProductionSchedule.
	 */
	private List<Order> getPendingOrders() {
		return new ArrayList<Order>(this.pendingOrders);
	}
	
	/**
	 * Add the specified order to the pending orders of this ProductionSchedule.
	 * 
	 * @param order
	 * 		The order to be added to the pending orders of this ProductionSchedule.
	 * 
	 * @postcondition (new this).getPendingOrders.getLast() == order
	 */
	private void addToPendingOrders(Order order) {
		this.pendingOrders.add(order);
	}
	
	/** The pending orders of this ProductionSchedule. */
	private final List<Order> pendingOrders = new LinkedList<Order>();

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
	/**
	 * Get the manufacturer associated with this ProductionSchedule.
	 * 
	 * @return The manufacturer associated with ProductionSchedule.
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The Manufacturer of this ProcedureSchedule. */
	private final Manufacturer manufacturer;

	//--------------------------------------------------------------------------
	// AssemblyLine Related Methods and properties. 
	//--------------------------------------------------------------------------
	/**
	 * Get the next order of this ProductionSchedule that is to be scheduled
	 * on the AssemblyLine that is associated with this ProductionSchedule. 
	 * 
	 * @return The next order of this ProductionSchedule that is to be scheduled.
	 * 		   if empty or hasNoTime, return null. 
	 */
	public Order getNextOrderToSchedule() {
		List<OrderContainer> pendingOrders = this.getPendingOrderContainers();
		
		if (pendingOrders.isEmpty())
			return null;
		if (!this.hasTimeToScheduleNext())
			return null;
		 
		return this.getPendingOrders().get(0);
	}
	
	/**
	 * Get the AssemblyLine associated with this ProductionSchedule.
	 * 
	 * @return The AssemblyLine associated with thisProductionSchedule.
	 */
	private AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}
	
	/** The AssemblyLine that is associated with this ProcedureSchedule. */
	private AssemblyLine assemblyLine;
	
	/**
	 * Get and remove the next pending Order from this ProductionSchedule, 
	 * returns null if no next order exists. 
	 *
	 * @postcondition !(new this).getPendingOrders().contains(this.getNextOrderToSchedule())
	 * 
	 * @return next pending Order || null
	 */
	Order popNextOrderFromSchedule() {
		// we need access to the original list, so the actual private variable is used.
		if (!this.pendingOrders.isEmpty())
			return this.pendingOrders.remove(0);
		else {
			return null;
		}
	}

	//--------------------------------------------------------------------------
	/**
	 * Advance the current time of this ProductionSchedule  by the specified amount.  
	 * 
	 * @param minutes
	 * 		The number of minutes that have passed since the last 	
	 * 
	 * @postcondition | assemblyLine.isEmpty() && !hasTimeToScheduleNext() -> (new this).getCurrentTime() == new DateTime(days + 1, STARTHOUR, 0)
	 *                | otherwise                                          -> (new this).getCurrentTime() == currentTime.addTime(0, 0, minutes)
	 */
	public void advanceTime(int minutes) {
		this.incrementCurrentTime(0, 0, minutes);

		if (!this.hasTimeToScheduleNext() && this.getAssemblyLine().isEmpty()) {
			int newOverTime = Math.abs(Math.min(0, this.timeLeftMinutes()));
			this.setOverTime(newOverTime);
			DateTime newTime = this.makeNewDateTime(this.getCurrentTime().getDays() + 1, STARTHOUR, 0);
			this.setCurrentTime(newTime);
		}
	}
	
	/**
	 * Check if this ProductSchedule has time to schedule a next car, assuming
	 * the car immediately enters the first WorkPost of the AssemblyLine.
	 * 
	 * @return True if a next car can be scheduled
	 */
	public boolean hasTimeToScheduleNext() {
		return (this.timeLeftMinutes() >= (N_HOURS_MOVE * 60) * this.getAssemblyLine().getAmountOfWorkPosts()); 
	}

	/** 
	 * End the current day of this ProductionSchedule at the specified endTime
	 * this will update the over-time, current-time. 
	 * 
	 * @param endTime
	 * 		The time at which this day has ended. 
	 * 
	 * @precondition | this.assemblyLine.isEmpty()
	 * 
	 * @postcondition | (new this).getCurrentTime() == new DateTime(endTime.days +1, 6, 0)
	 * @postcondition | (new this).getOverTime() == this.getOverTime() + endTime.hours * 60 + endTime.minutes - 18 * 60 
	 * 
	 * @throws IllegalArgumentException | there is still time to finish another car assuming no delays take place
	 * 									| >>> this.getOverTime() + endTime.hours * 60 + endTime.minutes - 18 * 60
	 */
	public void endDay(DateTime endTime) {
		int timeLeft = WORKHOURS * 60 - ((endTime.getHours() - STARTHOUR) * 60 + endTime.getMinutes()) - this.getOverTime();
		if ( timeLeft >= this.getAssemblyLine().getAmountOfWorkPosts() * 60)
			throw new IllegalArgumentException("Time left exceeds time to fabricate another car.");
		
		this.setOverTime(-1 * timeLeft);
		this.setCurrentTime(makeNewDateTime(endTime.getDays() + 1, STARTHOUR, 0));		
	}

	/** 
	 * Calculate the time in minutes left today. 
	 * 
	 * @return the time left today. 
	 */
	private int timeLeftMinutes() {
		return FINISHHOUR * 60 - this.getCurrentTime().getHours() * 60 
				               - this.getCurrentTime().getMinutes()
    			               - this.getOverTime();
	}
	
	/**
	 * Complete order, pass it from the assembly line to the manufacturer.
	 * 
	 *  @precondition | order.isCompleted()
	 */
	void completeOrder(Order order) {
		this.getManufacturer().addCompleteOrder(order);
	}
}