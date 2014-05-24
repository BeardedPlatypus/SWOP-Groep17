package domain.assembly_line;

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
