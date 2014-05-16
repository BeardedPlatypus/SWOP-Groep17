package domain.assemblyLine;

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
		if(!this.getAssemblyLine().isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		this.getAssemblyLine().getWorkPost(workPostNumber).completeTask(taskNumber, minutes);
	}
	
	/**
	 * Advance the AssemblyLine such that no AssemblyProcedures can skip
	 * their WorkPosts
	 * 
	 * @throws IllegalStateException
	 * 		Concrete state does not allow the AssemblyLine to advance
	 */
	public void advanceAssemblyLine() throws IllegalStateException {
		this.initialWorkPostShift();
		this.shiftLoop();
		this.checkStateTransition();
	}
	
	/**
	 * When the AssemblyLine has advanced, check whether a state transition is
	 * necessary.
	 */
	protected abstract void checkStateTransition();
	
	/**
	 * Advance this AssemblyLine by one WorkPost. All AssemblyProcedures
	 * are shifted forward by one WorkPost. Concerning the last AssemblyProcedure:
	 * its order is retrieved and information about it is reported to the StatisticsLogger
	 */
	private void initialWorkPostShift() throws IllegalStateException{
		AssemblyProcedure finishedProcedure = this.getAssemblyLine().
				getLastWorkPost().getAssemblyProcedure();
		
		this.getAssemblyLine().getLastWorkPost()
			.addToElapsedMinutes((int) this.getAssemblyLine().getElapsedTime().getInMinutes());
		for(int i = this.getAssemblyLine().getAssemblyLineSize() - 1; i > 0 ; i--){
			this.getAssemblyLine().getWorkPost(i - 1).
				addToElapsedMinutes((int) this.getAssemblyLine().getElapsedTime().getInMinutes());
			this.getAssemblyLine().getWorkPost(i)
				.takeAssemblyProcedureFrom(this.getAssemblyLine().getWorkPost(i - 1));
		}
		
		this.getAssemblyLine().handleFinishedAssemblyProcedure(finishedProcedure);
	}
	
	/**
	 * After the initial work post shift, keep advancing parts of the
	 * AssemblyLine individually until no more AssemblyProcedures can skip
	 * a WorkPost and until the first WorkPost has an active AssemblyProcedure.
	 */
	private void shiftLoop() {
		boolean loopHadAnEffect;
		do {
			loopHadAnEffect = false;
			if (this.getAssemblyLine().getLastWorkPost().isFinished()) {
				this.rollFinishedAssemblyProcedureOffLine();
			}
			if (this.getAssemblyLine().getFirstWorkPost().isEmpty()) {
				loopHadAnEffect = this.putNextOrderOnFirstWorkPost();
			}
			for (int i = this.getAssemblyLine().getAssemblyLineSize(); i > 0; i--) {
				loopHadAnEffect = loopHadAnEffect || this.workPostShiftStep(i, i - 1);
			}
		} while (loopHadAnEffect);
	}
	
	/**
	 * When necessary, take the completed AssemblyProcedure from the last WorkPost
	 * and process it.
	 */
	private void rollFinishedAssemblyProcedureOffLine() {
		AssemblyProcedure finishedProcedure = this.getAssemblyLine().getLastWorkPost()
				.getAssemblyProcedure();
		this.getAssemblyLine().handleFinishedAssemblyProcedure(finishedProcedure);
		//FIXME replace null by Optional
		this.getAssemblyLine().getLastWorkPost().setAssemblyProcedure(null);
	}
	
	/**
	 * When necessary, consume the next Order from the ProductionSchedule
	 * and put it on the AssemblyLine.
	 * 
	 * @return The first WorkPost is no longer empty
	 */
	private boolean putNextOrderOnFirstWorkPost() {
		Order nextOrder = this.popNextOrderFromSchedule();
		//FIXME optional?
		if (nextOrder != null) {
			AssemblyProcedure nextProcedure = this.getAssemblyLine()
					.makeAssemblyProcedure(nextOrder);
			this.getAssemblyLine().getFirstWorkPost().setAssemblyProcedure(nextProcedure);
		}
		return ! this.getAssemblyLine().getFirstWorkPost().isEmpty();
	}
	
	/**
	 * If the current WorkPost is empty and the preceding WorkPost is finished,
	 * put the AssemblyProcedure from the preceding WorkPost on the current WorkPost
	 * 
	 * @param currentWorkPostNum
	 * 		The WorkPost that takes the AssemblyProcedure
	 * @param predecessorWorkPostNum
	 * 		The WorkPost that loses its AssemblyProcedure
	 * @return The current WorkPost took the AssemblyProcedure from the preceding
	 * 		WorkPost
	 */
	private boolean workPostShiftStep(int currentWorkPostNum, int predecessorWorkPostNum) {
		boolean currentEmpty = this.getAssemblyLine().getWorkPost(currentWorkPostNum).isEmpty();
		boolean predecessorFinished = this.getAssemblyLine().getWorkPost(predecessorWorkPostNum).isFinished();
		if (currentEmpty && predecessorFinished) {
			this.getAssemblyLine().getWorkPost(currentWorkPostNum)
				.takeAssemblyProcedureFrom(this.getAssemblyLine().getWorkPost(predecessorWorkPostNum));
			return true;
		}
		return false;
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
