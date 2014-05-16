package domain.assemblyLine;

import domain.order.Order;

/**
 * This State class represents the fact that the AssemblyLine is broken
 * and cannot perform any work.
 * 
 * @author Thomas Vochten
 *
 */
public class BrokenState extends AssemblyLineState {

	public BrokenState(AssemblyLine line) throws IllegalArgumentException {
		super(line);
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
	public void advanceAssemblyLine() throws IllegalStateException {
		throw new IllegalStateException("Cannot advance assembly line"
				+ "if it is broken");
	}
	
	/**
	 * Throw IllegalStateException as the AssemblyLine cannot process
	 * new orders in this state.
	 */
	@Override
	protected Order popNextOrderFromSchedule() throws IllegalStateException {
		throw new IllegalStateException("Cannot consume next order"
				+ "if the assembly line is broken");
	}
	
	/**
	 * Throw IllegalStateException as the AssemblyLine cannot process
	 * new orders in this state.
	 */
	@Override
	protected Order peekNextOrderFromSchedule() throws IllegalStateException {
		throw new IllegalStateException("Cannot peek next order"
				+ "if the assembly line is broken");
	}
	
	@Override
	protected void checkStateTransition() {
		// no special treatment required
	}
	
	@Override
	protected void finaliseSetState() {
		// no special treatment required
	}

}
