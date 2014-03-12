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
	 * @param assemblyLine
	 * 		The AssemblyLine associated with this new ProductionSchedule.
	 * 
	 * @precondition manufacturer != null
	 * @precondition assemblyLine != null
	 * 
	 * @postcondition (new this).getCurrentTime() == new DateTime(0, 0, 0)
	 */
	public ProductionSchedule(Manufacturer manufacturer, AssemblyLine assemblyLine) {
		this.manufacturer = manufacturer;
		this.assemblyLine = assemblyLine;
		
		this.setCurrentTime(new DateTime(0, STARTHOUR, 0));
		this.setOverTime(0);
		this.setOrderIdentifier(0);
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
		if (order.isCompleted())
			return order.getEstimatedCompletionTime();
		
		List<OrderContainer> pendingOrders = this.getPendingOrderContainers();
		List<OrderContainer> activeOrders = this.getAssemblyLine().getActiveOrderContainers();
		
		int index = pendingOrders.indexOf(order);
		if (index >= 0) { //Pending orders branch
			int estHoursForCompletion = N_HOURS_MOVE * (index + this.getAssemblyLine().getSize());
			int timeLeftToday = 22 * 60 - this.getCurrentTime().getHours() * 60 
									    - this.getCurrentTime().getMinutes()
									    - this.getOverTime();
			
			if (estHoursForCompletion * 60 <= timeLeftToday) {
				return this.getCurrentTime().addTime(0, estHoursForCompletion, 0);
			} else {
				int timeLeftHours = timeLeftToday / 60; 
				if (timeLeftToday % 60 > 0)
					timeLeftHours -= 1;
				
				estHoursForCompletion = estHoursForCompletion - timeLeftHours;
				int day = this.getCurrentTime().getDays() + 1 + (estHoursForCompletion / WORKHOURS);
				int hours = STARTHOUR + (estHoursForCompletion % WORKHOURS);
				return makeNewDateTime(day, hours, 0);
			}
		} else {
			index = activeOrders.indexOf(order);
			if (index >= 0) { //Active orders branch. 
				return this.getEstimatedCompletionTime(index);
			} else { // Order does not exist in this ProductionSchedule.
				throw new IllegalArgumentException("order does not exist within this ProductionSchedule.");	
			}
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
		if (positionOrder < 0) { // Invalid Argument
			throw new IllegalArgumentException("position smaller than 0");
		} else if (positionOrder < this.getAssemblyLine().getSize()) { // on assembly line
			int estHoursForCompletion = N_HOURS_MOVE * (this.getAssemblyLine().getSize() - positionOrder);
			return this.getCurrentTime().addTime(0, estHoursForCompletion, 0);
		
		} else { // in pending orders
			
			
			int estHoursForCompletion = N_HOURS_MOVE * (positionOrder + 1); // add one for current action on AssemblyLine to complete.
			int timeLeftToday = 22 * 60 - this.getCurrentTime().getHours() * 60 
									    - this.getCurrentTime().getMinutes()
									    - this.getOverTime();
			
			if (estHoursForCompletion * 60 <= timeLeftToday) {
				return this.getCurrentTime().addTime(0, estHoursForCompletion, 0);
			} else {				
				int numberMovesToday  
				
				return null;
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
		if ( timeLeft >= this.getAssemblyLine().getSize() * 60)
			throw new IllegalArgumentException("Time left exceeds time to fabricate another car.");
		
		this.setOverTime(-1 * timeLeft);
		this.setCurrentTime(makeNewDateTime(endTime.getDays() + 1, STARTHOUR, 0));		
	}
	
	protected DateTime makeNewDateTime(int days, int hours, int minutes) {
		return new DateTime(days, hours, minutes);
	}

	// FIXME verify if these are correctly put here. 
	/** Number of workhours in a shift. */
	private final static int WORKHOURS = 16;
	/** Start of a workday. */
	private final static int STARTHOUR = 6;
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
		int curPos = this.getPendingOrderContainers().size() + this.getAssemblyLine().getSize();
		
		Order newOrder = makeNewOrder(model, specs, 
				                      this.getCurrentOrderIdentifier(),
				                      this.getEstimatedCompletionTime(curPos));		
		this.addToPendingOrders(newOrder);
		this.incrementOrderIdentifier();
	}
	
	/** Isolated Order Constructor, mostly for testing purposes. */
	protected Order makeNewOrder(Model model, Specification specs, int orderNumber, 
			                     DateTime estimatedTime) {
		return new Order(model, specs, orderNumber, estimatedTime);
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
	 * @throws IndexOutOfBoundsException | this.getPendingOrderContainers().size() == 0;
	 */
	public Order getNextOrderToSchedule() throws IndexOutOfBoundsException{
		List<OrderContainer> pendingOrders = this.getPendingOrderContainers();
		
		if (pendingOrders.size() == 0)
			return null;
		// if get time exceeds time for day return null
		
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
	private final AssemblyLine assemblyLine;
	
	/**
	 * Remove the Order that is next in line to be scheduled from 
	 * This ProductionSchedule. 
	 * 
	 * @postcondition !(new this).getPendingOrders().contains(this.getNextOrderToSchedule())
	 * 
	 * @throws IndexOutofBoundsException | this.getPendingOrderContainers().size() == 0;
	 */
	public void removeNextOrderToSchedule() {
		this.pendingOrders.remove(0);
	}
}