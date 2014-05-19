package domain.assemblyLine;

import domain.DateTime;
import domain.order.Order;

/**
 * The AssemblyLineState determines whether trying to complete tasks
 * or requesting new orders has an effect.
 * 
 * @author Thomas Vochten
 *
 */
public abstract class AssemblyLineState {
	
	/**
	 * Initialise a new AssemblyLineState with the specified AssemblyLine
	 * 
	 * @param line
	 * 		The AssemblyLine to control the state of
	 * @throws IllegalArgumentException
	 * 		line is null
	 */
	public AssemblyLineState(AssemblyLine line) throws IllegalArgumentException {
		if (line == null) {
			throw new IllegalArgumentException("Cannot initialise an"
					+ "AssemblyLineState with null AssemblyLine");
		}
		this.assemblyLine = line;
	}
	
	/** The AssemblyLine to control the state of */
	protected final AssemblyLine assemblyLine;
	
	/**
	 * Get the AssemblyLine of which this State controls the work flow.
	 * 
	 * @return The AssemblyLine
	 */
	protected AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
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
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException,
			IllegalStateException{
		if(!this.isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
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
	 * See {@link AssemblyLine#getWorkPost(int) getWorkPost(int)}
	 */
	WorkPost getWorkPost(int workPostNum) throws IllegalArgumentException {
		return this.getAssemblyLine().getWorkPost(workPostNum);
	}
	
	/**
	 * See {@link AssemblyLine#getFirstWorkPost() getFirstWorkPost()}
	 */
	WorkPost getFirstWorkPost() {
		return this.getAssemblyLine().getFirstWorkPost();
	}
	
	/**
	 * See {@link AssemblyLine#getLastWorkPost() getLastWorkPost()}
	 */
	WorkPost getLastWorkPost() {
		return this.getAssemblyLine().getLastWorkPost();
	}
	
	/**
	 * See {@link AssemblyLine#getElapsedTime() getElapsedTime()}
	 */
	DateTime getElapsedTime() {
		return this.getAssemblyLine().getElapsedTime();
	}
	
	/**
	 * See {@link AssemblyLine#getAssemblyLineSize() getAssemblyLineSize()}
	 */
	int getAssemblyLineSize() {
		return this.getAssemblyLine().getAssemblyLineSize();
	}
	
	/**
	 * See {@link AssemblyLine#makeAssemblyProcedure(Order) makeAssemblyProcedure(Order)}
	 */
	AssemblyProcedure makeAssemblyProcedure(Order order) throws IllegalArgumentException {
		return this.getAssemblyLine().makeAssemblyProcedure(order);
	}
	
	/**
	 * See {@link AssemblyLine#handleFinishedAssemblyProcedure(Order) handleFinishedAssemblyProcedure(Order)}
	 */
	void handleFinishedAssemblyProcedure(AssemblyProcedure procedure) {
		this.getAssemblyLine().handleFinishedAssemblyProcedure(procedure);
	}
	
	/**
	 * Ask the ProductionSchedule for the next Order the AssemblyLine can process
	 * and consumes it.
	 * 
	 * @return The next Order
	 */
	protected Order popNextOrderFromSchedule() {
		return this.getAssemblyLine().popNextOrderFromSchedule();
	}
	
	/**
	 * Peeks at the next Order the AssemblyLine can process.
	 * 
	 * @return The next Order
	 */
	protected Order peekNextOrderFromSchedule() {
		return this.getAssemblyLine().peekNextOrderFromSchedule();
	}
	
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

}
