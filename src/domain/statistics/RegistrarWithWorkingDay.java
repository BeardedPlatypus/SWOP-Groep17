package domain.statistics;

/**
 * Class for the subset of Registrar objects that must know what day it is.
 * @author Thomas Vochten
 *
 */
public abstract class RegistrarWithWorkingDay implements Registrar {
	
	//--------------------------------------------------------------------------
	// Processing statistics
	//--------------------------------------------------------------------------
	@Override
	public abstract void addStatistics(ProcedureStatistics statistics);

	@Override
	public abstract String getStatistics();

	//--------------------------------------------------------------------------
	// Manage the working day
	//--------------------------------------------------------------------------
	/**
	 * Signify that the day has been changed. Process statistics that have
	 * been gathered for the current day appropriately. This method has no effect
	 * if the specified day number is equal to this Registrar's active day.
	 * @param dayNumber
	 * 		The day to switch to.
	 * @post The day has been switched to dayNumber.
	 * @post The statistics gathered on the active working day have been finalised.
	 * @throws IllegalArgumentException
	 * 		dayNumber is smaller than the day number of the current
	 * 		working day.
	 */
	public void switchDay(int dayNumber) throws IllegalArgumentException {
		if (this.getActiveDay() == null) {
			this.setActiveDay(new WorkingDay(dayNumber));
			return;
		}
		if (! this.isValidDay(dayNumber)) {
			throw new IllegalArgumentException("Cannot advance day"
					+ "to a day with smaller day number than the current day.");
		}
		if (dayNumber == this.getActiveDay().getDayNumber()) {
			return;
		}
		this.finishUpActiveDay(dayNumber);
		this.setActiveDay(new WorkingDay(dayNumber));
	}
	
	/**
	 * Indicate whether the active day can be set to the specified day number.
	 * @param dayNumber
	 * 		The prospective day number.
	 * @return Whether the active day can be set to the specified day number.
	 */
	public boolean isValidDay(int dayNumber) {
		return dayNumber >= this.getActiveDay().getDayNumber();
	}
	
	/**
	 * Finish up the statistics gathered for the current day appropriately.
	 * @param dayNumber
	 * 		New value for day for those types of Registrars that need it.
	 */
	protected abstract void finishUpActiveDay(int dayNumber);
	
	/**
	 * Get the currently active day.
	 * 
	 * @return The currently active day.
	 */
	public WorkingDay getActiveDay() {
		return this.activeDay;
	}
	
	/**
	 * Set the currently active day to the specified day.
	 * @param activeDay
	 * 		The new active day.
	 */
	protected void setActiveDay(WorkingDay activeDay) {
		this.activeDay = activeDay;
	}
	
	/** The working day this Registrar is gathering information on */
	private WorkingDay activeDay;
}
