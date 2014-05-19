package domain.assemblyLine;

/**
 * When an AssemblyLine is operational, it is capable of completing tasks
 * and putting Orders on the line.
 * 
 * @author Thomas Vochten
 *
 */
public class OperationalState extends AssemblyLineState {
	
	@Override
	public String getName() {
		return "Operational";
	}

	/**
	 * When setting an AssemblyLine's state to OperationalState,
	 * it must still be decided which subclass of OperationalState to use.
	 */
	@Override
	protected void finaliseSetState() {
		if (super.isEmpty()) {
			this.checkSetIdleState();
		}
		else {
			super.setState(new ActiveState());
		}
	}
	
	/**
	 * If the AssemblyLine is empty, advance the assembly line and then
	 * check if it is still empty.
	 */
	private void checkSetIdleState() {
		super.advanceAssemblyLine();
		if (super.isEmpty()) {
			super.setState(new IdleState());
		}
		else {
			super.setState(new ActiveState());
		}
	}

	@Override
	public OperationalState clone() {
		return new OperationalState();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		return other instanceof OperationalState;
	}

}
