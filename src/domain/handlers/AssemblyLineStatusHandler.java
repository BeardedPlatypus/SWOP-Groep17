package domain.handlers;

import java.util.List;

import domain.Manufacturer;
import domain.assembly_line.AssemblyLineView;

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
	 * Return a list of all assemblylines in the system.
	 * These are immutable and can be inspected.
	 * 
	 * @return all assemblylines in the system
	 */
	public List<AssemblyLineView> getLineViews() {
		return this.getManufacturer().getAssemblyLineViews();
	}
	
	
}
