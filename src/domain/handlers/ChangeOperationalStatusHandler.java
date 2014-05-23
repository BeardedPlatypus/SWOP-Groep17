package domain.handlers;

import java.util.List;

import domain.Manufacturer;
import domain.assembly_line.AssemblyLineState;
import domain.assembly_line.AssemblyLineStateView;

/**
 * This handler fulfills the "Change Assembly Line's Operational Status"
 * use case
 * 
 * @author Thomas Vochten
 *
 */
public class ChangeOperationalStatusHandler {

	/**
	 * Initialise a new ChangeOperationalStatusHandler with the specified manufacturer.
	 * 
	 * @param manufacturer
	 * 		The manufacturer
	 */
	public ChangeOperationalStatusHandler(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	/** Entry point into the system */
	private Manufacturer manufacturer;
	
	/**
	 * Get the Manufacturer
	 * 
	 * @return the Manufacturer
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	//TODO select assembly line
	
	/**
	 * Get the AssemblyLineStates that the manager can select.
	 * 
	 * @return The AssemblyLineStates
	 */
	public List<AssemblyLineStateView> getAvailableStates() {
		return this.getManufacturer().getAvailableStates();
	}
	
	/**
	 * Set the state of the specified AssemblyLine to the specified AssemblyLineState.
	 * 
	 * @param assemblyLineNum
	 * 		The AssemblyLine whose state is changed
	 * @param stateNum
	 * 		The new state of the AssemblyLine
	 */
	public void setAssemblyLineState(int assemblyLineNum, int stateNum) {
		AssemblyLineState state = this.getManufacturer().getStateInstance(stateNum);
		this.getManufacturer().setAssemblyLineState(assemblyLineNum, state);
	}
	
	/**
	 * Get a list with the states of all assemblylines. The list contains only
	 * states. These states are in order of how the assemblyLines occur on the
	 * assemblyFloor.
	 * 
	 * One can thus imply that the states are in the correct order relative to
	 * the assemblylines in the system.
	 * 
	 * @return a list of AssemblyLineStates of the AssemblyLines in the system,
	 * 		in their actual respective order in the system.
	 */
	public List<AssemblyLineStateView> getAssemblyLineStates(){
		return this.getManufacturer().getCurrentLineStates();
	}
	
}
