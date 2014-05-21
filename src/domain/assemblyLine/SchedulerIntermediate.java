package domain.assemblyLine;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.order.Order;
import domain.order.OrderView;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.productionSchedule.ProductionScheduleFacade;
import domain.productionSchedule.TimeObserver;

/**
 * The SchedulerIntermediate serves as layer between the ProductionSchedule, only
 * concerned with the ordering of Orders, and the AssemblyLine, only concerned
 * with working its own assembly band.
 * 
 * The SchedulerIntermediate contains all logic concerned with the overtime of 
 * its associated AssemblyLine, and contains all logic related to calculating
 * which Order to schedule, as well as the estimated completion time.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class SchedulerIntermediate implements TimeObserver {
	public SchedulerIntermediate(AssemblyLine assemblyLine) {
		this.assemblyLine = assemblyLine;
		this.setCurrentTime(new DateTime(0, 0, 0));
	}
	
	/**
	 * Set the Manufacturer of this SchedulerIntermediate to the specified manufacturer. 
	 * 
	 * @param manufacturer
	 * 		The new manufacturer of this SchedulerIntermediate. 
	 * 
	 * @postcondition | (new this).getManufacturer() == manufacturer.
	 * 
	 * @throws IllegalArgumentException
	 * 		| manufacturer == null
	 * @throws IllegalStateException 
	 * 		| manufacturer.getSchedulerIntermediate() == this 
	 * @throws IllegalStateException
	 * 		manufacturer has already been set.
	 */
	public void setManufacturer(Manufacturer manufacturer) throws IllegalStateException {
		if (manufacturer == null)
			throw new IllegalArgumentException("Manufacturer cannot be null.");
		if (manufacturer.getSchedulerIntermediate() != this)
			throw new IllegalStateException("manufacturer.getSchedulerIntermediate() does not match this.");
		if (this.manufacturer != null) 
			throw new IllegalStateException("manufacturer has already been set.");
		
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Get the Manufacturer of this SchedulerIntermediate. 
	 * 
	 * @return The Manufacturer of this SchedulerIntermediate.
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

	
	//--------------------------------------------------------------------------
	// AssemblyLine related functions
	//--------------------------------------------------------------------------
	/**
	 * Get the AssemblyLine of this SchedulerIntermediate.
	 * 
	 * @return the AssemblyLine of this SchedulerIntermediate.
	 */
	AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}
	
	/** The AssemblyLine of this SchedulerIntermediate. */
	private final AssemblyLine assemblyLine;
	
	//--------------------------------------------------------------------------
	// Logic Related to calculating stuff.
	//--------------------------------------------------------------------------
	void advance() {
		//FIXME refactor to make prettier.
		ProductionScheduleFacade prodSched = this.getManufacturer().getProductionSchedule();
		AssemblyLine assLine = this.getAssemblyLine(); 
		
		if (prodSched.hasStandardOrders() && canScheduleNextOrderToday()) {
			// can a singletask be ordered without time increase
			List<SingleTaskOrder> taskOrders = prodSched.getNextSingleTasks();
			StandardOrder so = prodSched.getNextScheduledStandardOrder();
			List<SingleTaskOrder> canBeAdded = new ArrayList<>();
			
			for(SingleTaskOrder o: taskOrders) {
				if (canScheduleWithoutTimeIncrease(so, o))
					canBeAdded.add(o);
			}
			
			if (!canBeAdded.isEmpty()) {
				SingleTaskOrder mostUrgent = canBeAdded.remove(0);
				
				for (SingleTaskOrder o : canBeAdded) {
					if (o.getDeadline().compareTo(mostUrgent.getDeadline()) == -1) {
						mostUrgent = o;
					}
				}
				assLine.advance(prodSched.popNextScheSingleTaskOrder(mostUrgent.getSingleTaskOrderType()));
			} else {
				assLine.advance(prodSched.popNextScheduledStandardOrder());
			}						
		} else {
			if (prodSched.hasSingleTaskOrders()) {
				List<SingleTaskOrder> taskOrders = prodSched.getNextSingleTasks();
				List<SingleTaskOrder> canBeAdded = new ArrayList<>();
				
				for (SingleTaskOrder o : taskOrders) {
					if (this.canScheduleOrderToday(o))
						canBeAdded.add(o);
				}
					
				if (canBeAdded.isEmpty()) {
					if (assLine.isEmpty()) {
						//increment to next day
						DateTime nextDay = new DateTime(this.getCurrentTime().getDays() + 1, 
								                        STARTHOUR, 0);
						
						prodSched.incrementTime(nextDay.subtractTime(this.getCurrentTime()));
						this.advance();
					} else {
						//put nothing on the assembly just continue.
						assLine.advance(null);
					}
				} else {
					SingleTaskOrder mostUrgent = canBeAdded.remove(0);
					
					for (SingleTaskOrder o : canBeAdded) {
						if (o.getDeadline().compareTo(mostUrgent.getDeadline()) == -1) {
							mostUrgent = o;
						}
					}
					assLine.advance(prodSched.popNextScheSingleTaskOrder(mostUrgent.getSingleTaskOrderType()));
				}
				
				
			} else if (!assLine.isEmpty()) {
				// put nothing on the assembly just continue
				assLine.advance(null);
			} else {
				this.setIdle(true);				
			}
				
		}
			
	}	
	
	Optional<Order> popNextOrderFromSchedule() {
		//FIXME
		return null;
	}
		
	public boolean isIdle() {
		return this.isIdle;
	}

	public void unIdle() {
		if (this.isIdle) {
			this.setIdle(false);
			this.advance();
		}
	}
	
	private void setIdle(boolean newIsIdle) { 
		
		this.isIdle = newIsIdle;
		
	}

	/** If this AssemblyLine is idle. */
	private boolean isIdle = true;
	
	private boolean canScheduleWithoutTimeIncrease(StandardOrder so, SingleTaskOrder to) {
		AssemblyLine assLine = this.getAssemblyLine();
		AssemblyProcedure procSo = assLine.makeAssemblyProcedure(so);
		AssemblyProcedure procTo = assLine.makeAssemblyProcedure(to);
		
		List<AssemblyProcedure> virtAss = assLine.getAssemblyProcedures();
		
		LinkedList<AssemblyProcedure> withoutTask = new LinkedList<>(virtAss);
		LinkedList<AssemblyProcedure> withTask = new LinkedList<>(virtAss);
		
		withoutTask.set(0, procSo);
		withTask.set(0, procTo);
		withTask.addFirst(procSo);
		
		return calculateTimeToFinishVirtual(withTask) == calculateTimeToFinishVirtual(withoutTask);
	}
	
	/**
	 * The number of minutes to finish the specified virtual AssemblyLine.
	 */
	int calculateTimeToFinishVirtual(List<AssemblyProcedure> virtualAss) {
		AssemblyLine assemblyLine = this.getAssemblyLine();
		LinkedList<AssemblyProcedure> virtAss = new LinkedList<>(virtualAss);
		int virtAssSize = virtAss.size();
		int assLineSize = assemblyLine.getAssemblyLineSize();
		int dif = virtAssSize - assLineSize;
		
		int totalTime = 0;
		
		for (int i = 0; i < virtAssSize + 1; i++) {
			int stepTimeMax = 0;
			for (int j = 0; j < assemblyLine.getAssemblyLineSize(); j++) {
				int timeWorkPost = assemblyLine.getTimeOnWorkPost(virtAss.get(dif + j), j);
				stepTimeMax = Math.max(stepTimeMax, timeWorkPost);
			}
			totalTime += stepTimeMax;
			virtAss.addFirst(null);
			virtAss.removeLast();
		}
		return totalTime;
	}
	
	boolean canScheduleOrderToday(Order order) {
		AssemblyLine assLine = this.getAssemblyLine();
		int currentDayMinutes = this.getCurrentTime().hours * 60 + 
                this.getCurrentTime().minutes;

		int timeLeft = FINISHHOUR * 60 - this.getOverTime() - currentDayMinutes; 
		
		if (timeLeft <= 0)
			return false;

		//Test if a full order can still be scheduled. 
		AssemblyProcedure virtProc = assLine.makeAssemblyProcedure(order);
		List<AssemblyProcedure> virtAss = assLine.getAssemblyProcedures();
		virtAss.set(0, virtProc);
		
		int timeToScheduleOrder = calculateTimeToFinishVirtual(virtAss);
		
		return  timeLeft >= timeToScheduleOrder;
	}
	
	
	boolean canScheduleNextOrderToday() {
		return this.canScheduleOrderToday(this.getManufacturer().getProductionSchedule().getNextScheduledStandardOrder());
	}
	
	
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


	public DateTime getEstimatedCompletionTime(OrderView order) {
		AssemblyLine line = this.getAssemblyLine();
		List<WorkPostView> posts = line.getWorkPostContainers();
		for (int i = 0; i < posts.size(); i++) {
			if(!posts.get(i).isEmpty()){
				if (posts.get(i).getAssemblyProcedureView().getOrderView().equals(order)) {
					int hours = line.getAssemblyLineSize() - (i);
					return this.getCurrentTime().addTime(0, hours, 0);
				}
			}
		}
		throw new IllegalStateException("Order was not found on AssemblyLine.");
	}

	public void setAssemblyLine(AssemblyLine assemblyLine2) {
		//TODO auto-generated method
	}

}
