package domain.assemblyLine;

public class ActiveState extends OperationalState {

	@Override
	public ActiveState clone() {
		return new ActiveState();
	}
	
	@Override
	public String getName() {
		return "Operational (active)";
	}
	
	/**
	 * Should be called after the AssemblyLine has advanced
	 */
	@Override
	public void ensureStateConsistency() {
		if (super.isEmpty()) {
			super.setState(new IdleState());
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		return other instanceof ActiveState;
	}

}
