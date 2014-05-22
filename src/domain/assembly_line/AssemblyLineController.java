package domain.assembly_line;

import java.util.List;

import domain.clock.EventActor;
import domain.order.Order;

/**
 * The AssemblyLineController manages an AssemblyLine. It selects the orders to
 * schedule, manages state changes and requests next day events.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class AssemblyLineController implements EventActor {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	public AssemblyLineController(AssemblyLine assemblyLine) {
		this.assemblyLine = assemblyLine;
	}
	
	//--------------------------------------------------------------------------
	// AssemblyLine-related methods.
	//--------------------------------------------------------------------------
	/**
	 * Get the AssemblyLine of this AssemblyLineController.
	 * 
	 * @return The AssemblyLine of this AssemblyLineController.
	 */
	protected AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}
	
	/** The AssemblyLine of this AssemblyLineController. */
	private final AssemblyLine assemblyLine;

	//--------------------------------------------------------------------------
	// Activate methods.
	//--------------------------------------------------------------------------
	@Override
	public void activate() {
		// Check for state change. 
		
		// Get time. 
		
		
	}
	
	//--------------------------------------------------------------------------
	// State-related methods.
	//--------------------------------------------------------------------------
	public void restoreToOperationalState() {
		
	}
	
	private void switchState() {
		
	}
	
	
	
	//--------------------------------------------------------------------------
	// OrderSchedule Related methods.
	//--------------------------------------------------------------------------
	
	
	
	
	protected List<Order> calculateOrdersToSchedule() {
		return null;
	}

}
