package domain.statistics;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.statistics.Registrar;
import domain.productionSchedule.TimeObserver;

/**
 * Class responsible for registering relevant events for statistical bookkeeping.
 * @author Thomas Vochten
 *
 */
public class StatisticsLogger implements TimeObserver {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialises a new StatisticsLogger. It will have no registrars at first.
	 */
	public StatisticsLogger() {
		this.registrars = new ArrayList<RegistrarWithWorkingDay>();
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
		for (RegistrarWithWorkingDay registrar : this.getRegistrars()) {
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
		for (RegistrarWithWorkingDay registrar : this.getRegistrars()) {
			report.append(registrar.getStatistics());
		}
		return report.toString();
	}
	
	//--------------------------------------------------------------------------
	// Manipulating registrars
	//--------------------------------------------------------------------------
	/** Registrar objects that this StatisticsLogger passes events to. */
	private List<RegistrarWithWorkingDay> registrars;
	
	/**
	 * Get the list of Registrars that this StatisticsLogger passes events to.
	 * @return The Registrars
	 */
	private List<RegistrarWithWorkingDay> getRegistrars() {
		return this.registrars;
	}
	
	/**
	 * Add a Registrar to this StatisticsLogger.
	 * @param registrar
	 * 		Registrar to be added to this StatisticsLogger
	 */
	public void addRegistrar(RegistrarWithWorkingDay registrar) {
		this.getRegistrars().add(registrar);
	}
	
	//--------------------------------------------------------------------------
	// Observing the time
	//--------------------------------------------------------------------------
	@Override
	public void update(DateTime time) {
		int dayNumber = time.getDays();
		for (RegistrarWithWorkingDay registrar : this.getRegistrars()) {
			if (! registrar.isValidDay(dayNumber)) {
				return;
			}
		}
		for (RegistrarWithWorkingDay registrar : this.getRegistrars()) {
			registrar.switchDay(dayNumber);
		}
	}
}