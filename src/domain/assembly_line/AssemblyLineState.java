package domain.assembly_line;

import java.util.List;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.order.Order;

/**
 * The AssemblyLineState determines whether trying to complete tasks
 * or requesting new orders has an effect.
 * 
 * @author Thomas Vochten
 *
 */
public abstract class AssemblyLineState implements AssemblyLineStateView {
	
	/**
	 * Initialise a new AssemblyLineState
	 */
	public AssemblyLineState() throws IllegalArgumentException {
		this.layoutManipulator = new LayoutManipulator(this);
	}
	
	/**
	 * Make a clone of this AssemblyLineState, without the AssemblyLine set.
	 */
	@Override
	public abstract AssemblyLineState clone();
	
	/**
	 * Get the name of this AssemblyLineState
	 * 
	 * @return the name
	 */
	public abstract String getName();
	
	/** The AssemblyLine to control the state of */
	protected AssemblyLine assemblyLine;
	
	/**
	 * Set the AssemblyLine of this AssemblyLineState to the specified AssemblyLine
	 * 
	 * @param line
	 * 		The AssemblyLine of interest
	 * @throws IllegalArgumentException
	 * 		line is null
	 */
	public void setAssemblyLine(AssemblyLine line) throws IllegalArgumentException {
		if (line == null) {
			throw new IllegalArgumentException("Cannot set null AssemblyLine"
					+ " in AssemblyLineState");
		}
		this.assemblyLine = line;
	}
	
	/**
	 * Get the AssemblyLine of which this State controls the work flow.
	 * 
	 * @return The AssemblyLine
	 */
	protected AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}
	
	/**
	 * Indicate whether this AssemblyLineState's AssemblyLine has been set
	 * 
	 * @return The AssemblyLine is not null
	 */
	protected boolean assemblyLineIsSet() {
		return this.getAssemblyLine() != null;
	}
	
	/**
	 * Checks whether the AssemblyLine has been set and throws an IllegalStateException
	 * if not.
	 * 
	 * @throws IllegalStateException
	 * 		assemblyLineIsSet() == true
	 */
	private void checkAssemblyLineSet() throws IllegalStateException {
		if (! this.assemblyLineIsSet()) {
			throw new IllegalStateException("AssemblyLine has not yet been set in"
					+ "AssemblyLineState");
		}
	}
	
	/** Responsible for advancing the AssemblyLine */
	private LayoutManipulator layoutManipulator;
	
	/**
	 * Advances the AssemblyLine piecewise until no more orders can be
	 * placed on the line and no AssemblyProcedures can be shifted
	 * 
	 * @throws IllegalStateException
	 * 		assemblyLineIsSet() == false
	 * @throws IllegalArgumentException
	 * 		orders is null or contains null
	 */
	void advanceAssemblyLine(List<Order> orders) throws IllegalStateException,
		IllegalArgumentException {
		this.checkAssemblyLineSet();
		this.getLayoutManipulator().advanceAssemblyLine(orders);
	}
	
	/**
	 * Get the LayoutManipulator
	 * 
	 * @return the LayoutManipulator
	 */
	private LayoutManipulator getLayoutManipulator() {
		return this.layoutManipulator;
	}
	
	/**
	 * Ask the given {@link WorkPost} to complete {@link AssemblyTask} with given number on its current {@link AssemblyProcedure}
	 * 
	 * @param workPostNumber
	 * 		The number of the {@link WorkPost} we want to address
	 * @param taskNumber
	 * 		The number of the {@link AssemblyTask} that should be marked as completed at given {@link WorkPost}
	 * @throws IllegalArgumentException
	 * 		When the workPostNumber is not a valid index
	 * @throws IllegalArgumentException
	 * 		See {@link WorkPost#completeTask(int, int) completeTask(int, int)}
	 * @throws IllegalStateException
	 * 		See {@link WorkPost#completeTask(int, int) completeTask(int, int)}
	 * @throws IllegalStateException
	 * 		Concrete state does not allow the completion of tasks
	 * @throws IllegalStateException
	 * 		assemblyLineIsSet() == false
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException,
			IllegalStateException{
		if(!this.isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		this.checkAssemblyLineSet();
		this.getWorkPost(workPostNumber).completeTask(taskNumber, minutes);
	}
	
	
	
	/**
	 * When the AssemblyLine has advanced, do what is necessary to ensure
	 * the AssemblyLine state is consistent. Each subclass must ensure behaviour
	 * as specified in the assignment.
	 */
	protected abstract void ensureStateConsistency();
	
	/**
	 * See {@link AssemblyLine#isValidWorkPost(int) isValidWorkPost(int)}
	 */
	boolean isValidWorkPost(int workPostNum) {
		return this.getAssemblyLine().isValidWorkPost(workPostNum);
	}
	
	/**
	 * See {@link AssemblyLine#isEmpty() isEmpty()}
	 */
	boolean isEmpty() {
		return this.getAssemblyLine().isEmpty();
	}
	
	/**
	 * See {@link AssemblyLine#getWorkPost(int) getWorkPost(int)}
	 */
	WorkPost getWorkPost(int workPostNum) throws IllegalArgumentException {
		this.checkAssemblyLineSet();
		return this.getAssemblyLine().getWorkPost(workPostNum);
	}
	
	/**
	 * See {@link AssemblyLine#getFirstWorkPost() getFirstWorkPost()}
	 */
	WorkPost getFirstWorkPost() {
		this.checkAssemblyLineSet();
		return this.getAssemblyLine().getFirstWorkPost();
	}
	
	/**
	 * See {@link AssemblyLine#getLastWorkPost() getLastWorkPost()}
	 */
	WorkPost getLastWorkPost() {
		this.checkAssemblyLineSet();
		return this.getAssemblyLine().getLastWorkPost();
	}
	
	/**
	 * See {@link AssemblyLine#getElapsedTime() getElapsedTime()}
	 */
	DateTime getElapsedTime() {
		this.checkAssemblyLineSet();
		return this.getAssemblyLine().getElapsedTime();
	}
	
	/**
	 * See {@link AssemblyLine#getAssemblyLineSize() getAssemblyLineSize()}
	 */
	int getAssemblyLineSize() {
		this.checkAssemblyLineSet();
		return this.getAssemblyLine().getAssemblyLineSize();
	}
	
	/**
	 * See {@link AssemblyLine#makeAssemblyProcedure(Order) makeAssemblyProcedure(Order)}
	 */
	AssemblyProcedure makeAssemblyProcedure(Optional<Order> order) throws IllegalArgumentException {
		this.checkAssemblyLineSet();
		return this.getAssemblyLine().makeAssemblyProcedure(order);
	}
	
	/**
	 * See {@link AssemblyLine#handleFinishedAssemblyProcedure(Order) handleFinishedAssemblyProcedure(Order)}
	 */
	void handleFinishedAssemblyProcedure(Optional<AssemblyProcedure> procedure) {
		this.checkAssemblyLineSet();
		this.getAssemblyLine().handleFinishedAssemblyProcedure(procedure);
	}
	
//	/**
//	 * Ask the ProductionSchedule for the next Order the AssemblyLine can process
//	 * and consumes it.
//	 * 
//	 * @return The next Order
//	 */
//	protected Optional<Order> popNextOrderFromSchedule() {
//		this.checkAssemblyLineSet();
//		return this.getAssemblyLine().popNextOrderFromSchedule();
//	}
	
	/**
	 * Set the state of this AssemblyLineState's AssemblyLine
	 * to the specified AssemblyLineState
	 * 
	 * @param state
	 * 		The new state of the AssemblyLine
	 */
	protected void setState(AssemblyLineState state) {
		this.getAssemblyLine().setCurrentState(state);
	}
	
	/**
	 * When setting the state, perform more work to ensure valid state.
	 */
	protected abstract void finaliseSetState();
	
	abstract boolean acceptsOrders();
	
	abstract boolean canRestoreToOperational();

}
