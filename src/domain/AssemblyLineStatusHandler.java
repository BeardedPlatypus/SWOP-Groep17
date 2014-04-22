package domain;

import java.util.List;

/**
 * Coordinates with the outside world in order to view the assembly line status.
 * 
 * @author Simon Slangen
 */
public class AssemblyLineStatusHandler {
	
	private final Manufacturer manufacturer;
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------	
	/**
	 * Instantiate a new AssemblyLineStatusHandler with the specified {@link Manufacturer}.
	 * 
	 * @param 	manufacturer
	 * 			manufacturer that has the assembly line.
	 * @throws 	IllegalArgumentException
	 * 			manufacturer == null
	 */
	public AssemblyLineStatusHandler(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException();
		}
		
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Returns the manufacturer.
	 * 
	 * @return	The manufacturer.
	 */
	private Manufacturer getManufacturer() {
		return manufacturer;
	}
	
	/**
	 * Returns a list of workpost containers, containing information about the assembly line status.
	 * 
	 * @return List of workpost containers.
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return getManufacturer().getWorkPostContainers();
	}
}
