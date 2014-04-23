package domain;

/**
 * Interface for statistical bookkeeping. A Registrar is passed an event in the form
 * of ProcedureStatistics so that it can record relevant information.
 * 
 * @author Thomas Vochten
 *
 */
public interface Registrar {

	/**
	 * Register an event. Each implementation must decide which
	 * information is relevant.
	 * @param statistics
	 * 		Event to collect statistics about.
	 */
	public void addStatistics(ProcedureStatistics statistics);
	
	/**
	 * Collect the information registered by this Registrar and format it
	 * into a human-readable string.
	 * @return
	 * 		A human-readable string containing a report about statistics
	 * 		gathered by this Registrar.
	 */
	public String getStatistics();
}