package domain;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

/**
 * Class to register the delay between rolling on the AssemblyLine and
 * rolling off the AssemblyLine.
 * 
 * @author Thomas Vochten
 *
 */
public class DelayRegistrar extends RegistrarWithWorkingDay {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialises a new DelayRegistrar.
	 */
	public DelayRegistrar() {
		this.setActiveDay(new WorkingDay(0));
		this.delays = new ArrayList<Pair<Integer, WorkingDay>>();
		this.selector = new MedianSelector();
	}
	
	//--------------------------------------------------------------------------
	// Management of statistics
	//--------------------------------------------------------------------------
	@Override
	public void addStatistics(ProcedureStatistics statistics) {
		int delay = statistics.getDelay();
		if (delay != 0) {
			Pair<Integer, WorkingDay> newElem = new Pair<Integer, WorkingDay>(delay,
					super.getActiveDay());
			this.getDelays().add(newElem);
			this.updateAverage();
		}
	}

	@Override
	public String getStatistics() {
		StringBuilder builder = new StringBuilder();
		builder.append("==== DELAY STATISTICS ====\n");
		builder.append("Average: " + this.getAverage() + "\n");
		builder.append("Median: " + this.getMedian() + "\n");
		builder.append("The last two delays: \n");
		builder.append(this.statsForNLastDays(2));
		builder.append("==== END DELAY STATISTICS ====\n");
		return builder.toString();
		
		
	}
	
	private String statsForNLastDays(int days) {
		StringBuilder builder = new StringBuilder();
		int start = this.getDelays().size() - days;
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < this.getDelays().size(); i++) {
			Pair<Integer, WorkingDay> nthLast = this.getDelays().get(i);
			builder.append("Delay of " + nthLast.getValue0() + " minutes"
					+ " on day " + nthLast.getValue1().getDayNumber() + "\n");
		}
		return builder.toString();
	}

	@Override
	protected void finishUpActiveDay(int dayNumber) {
		// no special treatment of days needed at this time
	}
	
	//--------------------------------------------------------------------------
	// Bookkeeping
	//--------------------------------------------------------------------------
	/** Specific instances of delays, paired with the days they occurred on. */
	private List<Pair<Integer, WorkingDay>> delays;
	
	/** A running average of the delays. */
	private double average = 0;
	
	//--------------------------------------------------------------------------
	// Querying the statistics
	//--------------------------------------------------------------------------
	/**
	 * Get the delays registered so far, paired with the days they occurred on.
	 * @return
	 * 		The delays
	 */
	protected List<Pair<Integer, WorkingDay>> getDelays() {
		return this.delays;
	}
	
	/**
	 * Get the running average.
	 * @return
	 * 		The running average.
	 */
	protected double getAverage() {
		return this.average;
	}
	
	/**
	 * Get the median.
	 * @return The median
	 */
	protected double getMedian() {
		if (this.getDelays().isEmpty()) {
			return 0;
		}
		return this.getMedianSelector().findMedian(this.getDelays());
	}
	
	// --------------------------------------------------------------------------
	/** Responsible for finding the median of the delays */
	private MedianSelector selector;
	
	/**
	 * Get this DelayRegistrar's MedianSelector.
	 * @return The MedianSelector
	 */
	private MedianSelector getMedianSelector() {
		return this.selector;
	}
	
	//--------------------------------------------------------------------------
	// Manipulating specific statistics
	//--------------------------------------------------------------------------
	/**
	 * Updates the average. It is assumed that exactly one element has been added
	 * to the list of delays before calling this method.
	 */
	private void updateAverage() {
		int latestDelay = this.getDelays().get(this.getDelays().size() - 1).getValue0();
		double newAverage = this.getAverage() + 
				((latestDelay - this.getAverage()) / this.getDelays().size());
		this.setAverage(newAverage);
	}
	
	/**
	 * Set the running average to the specified value.
	 * @param average
	 * 		The value the running average will be set to.
	 */
	private void setAverage(double average) {
		this.average = average;
	}
}
