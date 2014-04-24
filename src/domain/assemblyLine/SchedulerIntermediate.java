package domain.assemblyLine;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import domain.DateTime;
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
public class SchedulerIntermediate implements TimeObserver{
	public SchedulerIntermediate(AssemblyLine assemblyLine) {
		this.assemblyLine = assemblyLine;
	}
	
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
	/**
	 * The number of minutes to finish the specified virtual AssemblyLine.
	 */
	int calculateTimeToFinishVirtual(List<AssemblyProcedure> virtualAss) {
		AssemblyLine assemblyLine = this.getAssemblyLine();
		LinkedList<AssemblyProcedure> virtAss = new LinkedList<>(virtualAss);
		int totalTime = 0;
		
		for (int i = 0; i < assemblyLine.getAssemblyLineSize() + 1; i++) {
			for (AssemblyProcedure proc : virtAss) {
				
			}
			
		}
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

}
