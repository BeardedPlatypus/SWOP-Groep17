package domain.assemblyLine;

import com.google.common.base.Optional;

import domain.order.Order;

/**
 * This State class represents the fact that the AssemblyLine is broken
 * and cannot perform any work.
 * 
 * @author Thomas Vochten
 *
 */
public class BrokenState extends AssemblyLineState {
	
	@Override
	public String getName() {
		return "Broken";
	}
	
	@Override
	public BrokenState clone() {
		return new BrokenState();
	}
	
	/**
	 * Throw IllegalStateException as no work can be performed in this state.
	 */
	@Override
	public void completeWorkpostTask(int workPostNum, int taskNum, int minutes)
			throws IllegalStateException {
		throw new IllegalStateException("Cannot perform work if the"
				+ "assembly line is broken");
	}

	/**
	 * Throw IllegalStateException as the AssemblyLine cannot be advanced
	 * in this state.
	 */
	@Override
	void advanceAssemblyLine() throws IllegalStateException {
		throw new IllegalStateException("Cannot advance assembly line"
				+ "if it is broken");
	}
	
	/**
	 * Throw IllegalStateException as the AssemblyLine cannot process
	 * new orders in this state.
	 */
	@Override
	protected Optional<Order> popNextOrderFromSchedule() throws IllegalStateException {
		throw new IllegalStateException("Cannot consume next order"
				+ "if the assembly line is broken");
	}
	
	@Override
	protected void ensureStateConsistency() {
		// no special treatment required
	}
	
	@Override
	protected void finaliseSetState() {
		// no special treatment required
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		return other instanceof BrokenState;
	}

}
