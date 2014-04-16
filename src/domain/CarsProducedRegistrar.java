package domain;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

/**
 * Class of Registrar objects that keeps track of the number of cars
 * produced in a day.
 * @author Thomas Vochten
 *
 */
public class CarsProducedRegistrar extends RegistrarWithWorkingDay {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialises a new CarsProducedRegistrar
	 */
	public CarsProducedRegistrar() {
		this.carsProduced = new ArrayList<Pair<Integer, WorkingDay>>();
		this.setActiveDay(new WorkingDay(0));
		this.selector = new MedianSelector();
	}
	
	//--------------------------------------------------------------------------
	// Management of statistics
	//--------------------------------------------------------------------------
	@Override
	public void addStatistics(ProcedureStatistics statistics) {
		this.incrementForCurrentDay();
	}

	@Override
	public String getStatistics() {
		StringBuilder builder = new StringBuilder();
		builder.append("==== NUMBER OF CARS PRODUCED STATISTICS ====\n");
		builder.append("Average: " + this.getAverage() + "\n");
		builder.append("Median: " + this.getMedian() + "\n");
		builder.append("Exact numbers for the last two days: \n");
		builder.append(this.statsForNLastDays(2));
		builder.append("==== END NUMBER OF CARS PRODUCED STATISTICS ====\n");
		return builder.toString();
		
		
	}
	
	private String statsForNLastDays(int days) {
		StringBuilder builder = new StringBuilder();
		int start = this.getCarsProducedNumbers().size() - days;
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < this.getCarsProducedNumbers().size(); i++) {
			Pair<Integer, WorkingDay> nthLast = this.getCarsProducedNumbers().get(i);
			builder.append(nthLast.getValue0() + " cars produced"
					+ " on day " + nthLast.getValue1().getDayNumber() + "\n");
		}
		return builder.toString();
	}

	@Override
	protected void finishUpActiveDay(int dayNumber) {
		Pair<Integer, WorkingDay> newElem =
				new Pair<Integer, WorkingDay>(this.getCurrentDayAmount(), this.getActiveDay());
		this.getCarsProducedNumbers().add(newElem);
		this.updateAverage();
		this.setCurrentDayAmount(0);
		for (int i = this.getActiveDay().getDayNumber() + 1; i < dayNumber; i++) {
			this.getCarsProducedNumbers()
				.add(new Pair<Integer, WorkingDay>(this.getCurrentDayAmount(),
				new WorkingDay(i)));
			this.updateAverage();
		}
	}
	
	//--------------------------------------------------------------------------
	// Bookkeeping
	//--------------------------------------------------------------------------
	/** List of number of cars produced, paired with the day. */
	private List<Pair<Integer, WorkingDay>> carsProduced;
	
	/** The running average of cars produced in a day */
	private double average = 0;
	
	/** The number of cars produced on the current day. */
	private int currentDayAmount = 0;
	
	// --------------------------------------------------------------------------.
	/** Responsible for finding the median of the number of cars produced */
	private MedianSelector selector;
	
	/**
	 * Get this CarsProducedRegistrar's MedianSelector
	 * @return The MedianSelector
	 */
	private MedianSelector getMedianSelector() {
		return this.selector;
	}
	
	
	//--------------------------------------------------------------------------
	// Querying statistics
	//--------------------------------------------------------------------------
	/**
	 * Get the list of number of cars produced, paired with the day.
	 * @return The list of number of cars produced
	 */
	protected List<Pair<Integer, WorkingDay>> getCarsProducedNumbers() {
		return this.carsProduced;
	}
	
	/**
	 * Get the amount of cars produced on the current day.
	 * @return The amount of cars produced
	 */
	protected int getCurrentDayAmount() {
		return this.currentDayAmount;
	}
	
	/**
	 * Get the running average of cars produced in a day.
	 * @return The running average
	 */
	protected double getAverage() {
		return this.average;
	}
	
	/**
	 * Get the median of cars produced in a day
	 * @return The median
	 */
	protected double getMedian() {
		return this.getMedianSelector().findMedian(this.getCarsProducedNumbers());
	}
	
	//--------------------------------------------------------------------------
	// Manipulating specific statistics
	//--------------------------------------------------------------------------
	/**
	 * Increment the cars produced counter for the current day.
	 */
	private void incrementForCurrentDay() {
		this.setCurrentDayAmount(this.getCurrentDayAmount() + 1);
	}
	
	/**
	 * Set the amount of cars produced for the current day to the specified
	 * value.
	 * @param currentDayAmount
	 * 		The value the current day amount will be set to.
	 */
	private void setCurrentDayAmount(int currentDayAmount) {
		this.currentDayAmount = currentDayAmount;
	}
	
	/**
	 * Update the running average. It is assumed that exactly one element
	 * has been added to the list of cars produced numbers since this method
	 * was last called.
	 */
	private void updateAverage() {
		int latestProduced = this.getCarsProducedNumbers()
				.get(this.getCarsProducedNumbers().size() - 1).getValue0();
		double newAverage = this.getAverage() + 
				((latestProduced - this.getAverage()) / this.getCarsProducedNumbers().size());
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
