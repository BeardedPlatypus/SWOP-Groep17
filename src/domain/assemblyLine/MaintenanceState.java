package domain.assemblyLine;

import domain.DateTime;
import domain.order.Order;

/**
 * When in maintenance, completion of AssemblyProcedures is still allowed.
 * When the AssemblyLine is empty, maintenance starts and is automatically
 * lifted after four hours have passed. The manager can also lift maintenance
 * 
 * 
 * @author Thomas Vochten
 *
 */
public class MaintenanceState extends AssemblyLineState {

	public MaintenanceState(AssemblyLine line) throws IllegalArgumentException {
		super(line);
	}

	@Override
	protected Order popNextOrderFromSchedule() {
		//FIXME optional?
		return null;
	}
	
	@Override
	protected Order peekNextOrderFromSchedule() {
		//FIXME optional?
		return null;
	}
	
	private DateTime timeOfLastAdvance;
	
	@Override
	protected void finaliseSetState() {
		this.checkEmptyAssemblyLine();
	}

	@Override
	protected void ensureStateConsistency() {
		this.checkEmptyAssemblyLine();
		
	}
	
	private void checkEmptyAssemblyLine() {
		if (super.getAssemblyLine().isEmpty()) {
			//TODO request time and set operational time to four hours later
		}
		
	}
	
	

}
