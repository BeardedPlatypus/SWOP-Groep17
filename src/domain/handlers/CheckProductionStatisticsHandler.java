package domain.handlers;

import domain.Manufacturer;

/**
 * Class responsible for delivering statistical reports whenever the user
 * desires.
 * 
 * @author Thomas Vochten
 *
 */
public class CheckProductionStatisticsHandler {
	
	/**
	 * Initialise a new CheckProductionStatisticsHandler with the specified
	 * Manufacturer.
	 * 
	 * @param manufacturer
	 * 		The manufacturer of the new CheckProductionStatisticsHandler
	 * @throws IllegalArgumentException
	 * 		Manufacturer is null
	 */
	public CheckProductionStatisticsHandler(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException();
		}
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Query the gathered statistics. The end user is responsible for
	 * meaningful analysis of the report.
	 * 
	 * @return A report in the form of a String
	 */
	public String getStatisticsReport() {
		return this.getManufacturer().getStatisticsReport();
	}
	
	/** The manufacturer to pass requests onto. */
	private Manufacturer manufacturer;
	
	/**
	 * Get this CheckProductionStatisticsHandler's Manufacturer.
	 * 
	 * @return The Manufacturer
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}

}
