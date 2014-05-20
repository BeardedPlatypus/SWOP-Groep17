package domain.assemblyLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The StateCatalog contains all possible AssemblyLineStates that the user
 * can select
 * 
 * @author Thomas Vochten
 *
 */
public class StateCatalog {
	
	/**
	 * Initialise a new StateCatalog. There will be no states available yet.
	 */
	public StateCatalog() {
		
	}
	
	/** The AssemblyLineStates available to the user */
	private List<AssemblyLineState> availableStates = new ArrayList<AssemblyLineState>();
	
	/**
	 * Get the states that are available to the user
	 * 
	 * @return The states
	 */
	public List<IAssemblyLineState> getAvailableStates() {
		return new ArrayList<IAssemblyLineState>(this.getStates());
	}
	
	/**
	 * Get states for internal use.
	 * 
	 * @return The states
	 */
	private List<AssemblyLineState> getStates() {
		return this.availableStates;
	}
	
	/**
	 * Add the specified state to the list of available states.
	 * 
	 * @param state
	 * 		The state to add
	 * @throws IllegalArgumentException
	 * 		checkDuplicate(state) == true
	 */
	public void addToAvailableStates(AssemblyLineState state) throws IllegalArgumentException {
		if (this.checkDuplicate(state)) {
			throw new IllegalArgumentException("StateCatalog already contains the specified state");
		}
		this.getStates().add(state);
	}
	
	/**
	 * Get a state instance specified by stateNumber
	 * 
	 * @param stateNumber
	 * 		Index into the list of states
	 * @return The state selected by stateNumber
	 * @throws IllegalArgumentException
	 * 		isValidStateNumber(stateNumber) == false
	 */
	public AssemblyLineState getStateInstance(int stateNumber) throws IllegalArgumentException {
		if (! isValidStateNumber(stateNumber)) {
			throw new IllegalArgumentException("stateNumber is not a valid index");
		}
		return this.getStates().get(stateNumber).clone();
	}
	
	/**
	 * Determine whether the specified state number is a valid index into
	 * the available states.
	 * 
	 * @param stateNumber
	 * 		The index
	 * @return stateNumber is a valid index
	 */
	public boolean isValidStateNumber(int stateNumber) {
		return stateNumber >= 0 && stateNumber < this.getStates().size();
	}
	
	/**
	 * Determine whether this StateCatalog already contains the specified state
	 * 
	 * @param state
	 * 		The state of interest
	 * @return This StateCatalog contains the specified state
	 */
	public boolean checkDuplicate(AssemblyLineState state) {
		return this.getStates().contains(state);
	}
}
