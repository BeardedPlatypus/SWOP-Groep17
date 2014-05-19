package domain.assemblyLine;

public class IdleState extends OperationalState {

	@Override
	public IdleState clone() {
		return new IdleState();
	}

	@Override
	public String getName() {
		return "Operational (idle)";
	}

	@Override
	protected void advanceAssemblyLine() {
		// disallow AssemblyLine advancements if idle
	}
	
	@Override
	protected void finaliseSetState() {
		
	}

}
