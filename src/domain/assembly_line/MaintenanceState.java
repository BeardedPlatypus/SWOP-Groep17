package domain.assembly_line;

import java.util.List;

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
	protected void finaliseSetState() {
	}

	@Override
	protected void ensureStateConsistency() {
		
	}

	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		return other instanceof MaintenanceState;
	}

	@Override
	boolean acceptsOrders() {
		return false;
	}

	@Override
	void advanceAssemblyLine(List<Order> orders) throws IllegalStateException,
			IllegalArgumentException {
		super.advanceAssemblyLine(orders);
		if (this.getAssemblyLine().isEmpty()) {
			this.getAssemblyLine().getAssemblyLineController().changeState(new ActiveState());
			this.getAssemblyLine().getEventConsumer().constructEvent(new DateTime(0, 4, 0), this.getAssemblyLine().getAssemblyLineController());
		}
	}

	@Override
	boolean canRestoreToOperational() {
		return true;
	}
}