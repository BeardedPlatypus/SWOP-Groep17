package domain.assemblyLine;

import java.util.List;

import domain.order.Order;

class IdleState extends OperationalState {

	@Override
	public IdleState clone() {
		return new IdleState();
	}

	@Override
	public String getName() {
		return "Operational (idle)";
	}
	
	@Override
	protected void finaliseSetState() {
		
	}
}
