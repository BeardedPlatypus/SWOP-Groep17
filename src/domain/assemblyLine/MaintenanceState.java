package domain.assemblyLine;

import com.google.common.base.Optional;

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
	
	@Override
	public String getName() {
		return "In maintenance";
	}
	
	public MaintenanceState clone() {
		return new MaintenanceState();
	}

	@Override
	protected Optional<Order> popNextOrderFromSchedule() {
		//FIXME optional?
		return Optional.absent();
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
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		return other instanceof MaintenanceState;
	}

}
