package domain.statistics;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.order.CompletedOrderEvent;
import domain.order.CompletedOrderObserver;
import domain.clock.TimeObserver;

/**
 * Class responsible for registering relevant events for statistical bookkeeping.
 * @author Thomas Vochten
 *
 */
public class StatisticsLogger implements TimeObserver, CompletedOrderObserver {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialises a new StatisticsLogger. It will have no registrars at first.
	 */
	public StatisticsLogger() {
		this.registrarsWorkingDay = new ArrayList<RegistrarWithWorkingDay>();
		this.registrars = new ArrayList<Registrar>();
	}

	//--------------------------------------------------------------------------
	// Reporting
	//--------------------------------------------------------------------------
	/**
	 * Report a new statistical event to this StatisticsLogger.
	 * @param statistics
	 * 		The statistical event to report.
	 */
	public void addStatistics(ProcedureStatistics statistics) {
		for (RegistrarWithWorkingDay registrar : this.getRegistrarsWorkingDay()) {
			registrar.addStatistics(statistics);
		}
		for (Registrar registrar : this.getRegistrars()) {
			registrar.addStatistics(statistics);
		}
	}
	
	/**
	 * Get a report on all statistical variables this StatisticsLogger is watching.
	 * @return 
	 * 		A report that takes the form of a String object. The client is responsible
	 * 		for deriving meaning from that report.
	 */
	public String getReport() {
		StringBuilder report = new StringBuilder();
		for (RegistrarWithWorkingDay registrar : this.getRegistrarsWorkingDay()) {
			report.append(registrar.getStatistics());
		}
		for (Registrar registrar : this.getRegistrars()) {
			report.append(registrar.getStatistics());
		}
		return report.toString();
	}
	
	//--------------------------------------------------------------------------
	// Manipulating registrars
	//--------------------------------------------------------------------------
	/** Registrar objects that this StatisticsLogger passes events to,
	 * with a notion of the day */
	private List<RegistrarWithWorkingDay> registrarsWorkingDay;
	
	/** Registrar objects that this StatisticsLogger passes events to */
	private List<Registrar> registrars;
	
	/**
	 * Get the list of RegistrarWithWorkingDays that this StatisticsLogger passes events to.
	 * @return The RegistrarWithWorkingDays
	 */
	private List<RegistrarWithWorkingDay> getRegistrarsWorkingDay() {
		return this.registrarsWorkingDay;
	}
	
	/**
	 * Get the list of Registrars that this StatisticsLogger passes events to.
	 * @return The Registrars
	 */
	private List<Registrar> getRegistrars() {
		return this.registrars;
	}
	
	/**
	 * Add a RegistrarWithWorkingDay to this StatisticsLogger.
	 * @param registrar
	 * 		RegistrarWithWorkingDay to be added to this StatisticsLogger
	 * @throws IllegalArgumentException
	 * 		registrar is null
	 */
	public void addRegistrar(RegistrarWithWorkingDay registrar) throws IllegalArgumentException {
		if (registrar == null) {
			throw new IllegalArgumentException("Cannot add null Registrar to StatisticsLogger");
		}
		this.getRegistrarsWorkingDay().add(registrar);
	}
	
	/**
	 * Add a Registrar to this StatisticsLogger.
	 * 
	 * @param registrar
	 * 		Registrar to be added to this StatisticsLogger
	 * @throws IllegalArgumentException
	 * 		registrar is null
	 */
	public void addRegistrar(Registrar registrar) throws IllegalArgumentException {
		if (registrar == null) {
			throw new IllegalArgumentException("Cannot add null Registrar to StatisticsLogger");
		}
		this.getRegistrars().add(registrar);
	}
	
	//--------------------------------------------------------------------------
	// Observing the time
	//--------------------------------------------------------------------------
	@Override
	public void update(DateTime time) {
		int dayNumber = time.getDays();
		for (RegistrarWithWorkingDay registrar : this.getRegistrarsWorkingDay()) {
			if (! registrar.isValidDay(dayNumber)) {
				return;
			}
		}
		for (RegistrarWithWorkingDay registrar : this.getRegistrarsWorkingDay()) {
			registrar.switchDay(dayNumber);
		}
	}
	
	// -------------------------------------------------------------------------
	
	@Override
	public void updateCompletedOrder(CompletedOrderEvent event)
			throws IllegalArgumentException {
		this.addStatistics(event.getProcedureStatistics());
	}
}