package domain.assemblyLine;

import java.util.List;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.order.Order;

/**
 * The LayoutManipulator class contains the logic related to advancing
 * an AssemblyLine.
 * 
 * @author Thomas Vochten
 *
 */
public class LayoutManipulator {
	
	/**
	 * Initialise a new LayoutManipulator with the specified state.
	 * 
	 * @param state
	 * 		The state of the new LayoutManipulator
	 */
	public LayoutManipulator(AssemblyLineState state) {
		this.state = state;
	}
	
	/** Delegate to ask for new Orders and to manipulate WorkPosts */
	private final AssemblyLineState state;
	
	/**
	 * Get the AssemblyLineState
	 */
	private AssemblyLineState getState() {
		return this.state;
	}
	
	/**
	 * Advance the AssemblyLine such that no AssemblyProcedures can skip
	 * their WorkPosts
	 * 
	 * @param orders
	 * 		The orders to put on the AssemblyLine
	 * @throws IllegalStateException
	 * 		Concrete state does not allow the AssemblyLine to advance
	 * @throws IllegalArgumentException
	 * 		orders is null or contains null
	 */
	void advanceAssemblyLine(List<Order> orders) throws IllegalStateException,
		IllegalArgumentException {
		if (! this.isValidListOrders(orders)) {
			throw new IllegalArgumentException("Cannot advance AssemblyLine with"
					+ "null orders");
		}
		this.initialWorkPostShift();
		this.shiftLoop(orders);
		this.getState().ensureStateConsistency();
	}
	
	/**
	 * Advance this AssemblyLine by one WorkPost. All AssemblyProcedures
	 * are shifted forward by one WorkPost. Concerning the last AssemblyProcedure:
	 * its order is retrieved and information about it is reported to the StatisticsLogger
	 */
	private void initialWorkPostShift() throws IllegalStateException{
		Optional<AssemblyProcedure> finishedProcedure = 
				this.getLastWorkPost().getAssemblyProcedure();
		
		this.getLastWorkPost()
			.addToElapsedMinutes((int) this.getElapsedTime().getInMinutes());
		
		for(int i = this.getAssemblyLineSize() - 1; i > 0 ; i--){
			this.getWorkPost(i - 1).
				addToElapsedMinutes((int) this.getElapsedTime().getInMinutes());
			this.getWorkPost(i)
				.takeAssemblyProcedureFrom(this.getWorkPost(i - 1));
		}

		this.handleFinishedAssemblyProcedure(finishedProcedure);
	}
	
	/**
	 * After the initial work post shift, keep advancing parts of the
	 * AssemblyLine individually until no more AssemblyProcedures can skip
	 * a WorkPost and until the first WorkPost has an active AssemblyProcedure.
	 */
	private void shiftLoop(List<Order> orders) {
		boolean loopHadAnEffect;
		do {
			loopHadAnEffect = false;
			if (this.isWorkPostFinished(this.getLastWorkPost())) {
				this.rollFinishedAssemblyProcedureOffLine();
			}
			if (this.getFirstWorkPost().isEmpty()) {
				loopHadAnEffect = this.putNextOrderOnFirstWorkPost(orders);
			}
			for (int i = this.getAssemblyLineSize() - 1; i > 0; i--) {
				loopHadAnEffect = this.workPostShiftStep(i, i - 1) || loopHadAnEffect;
			}
		} while (loopHadAnEffect);
	}
	
	/**
	 * When necessary, take the completed AssemblyProcedure from the last WorkPost
	 * and process it.
	 */
	private void rollFinishedAssemblyProcedureOffLine() {
		Optional<AssemblyProcedure> finishedProcedure = this.getLastWorkPost()
				.getAssemblyProcedure();
		this.handleFinishedAssemblyProcedure(finishedProcedure);
		this.getLastWorkPost().setAssemblyProcedure(null);
	}
	
	/**
	 * When necessary, consume the next Order from the ProductionSchedule
	 * and put it on the AssemblyLine.
	 * 
	 * @return The first WorkPost is no longer empty
	 */
	private boolean putNextOrderOnFirstWorkPost(List<Order> orders) {
		Optional<Order> nextOrder = this.popNextOrderFromSchedule(orders);
		if (nextOrder != null && nextOrder.isPresent()) {
			AssemblyProcedure nextProcedure = this.makeAssemblyProcedure(nextOrder);
			this.getFirstWorkPost().setAssemblyProcedure(Optional.fromNullable(nextProcedure));
		}
		return ! this.getFirstWorkPost().isEmpty();
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
		boolean currentEmpty = this.getWorkPost(currentWorkPostNum).isEmpty();
		boolean predecessorFinished = this.isWorkPostFinished(predecessorWorkPostNum);
		if (currentEmpty && predecessorFinished) {
			this.getWorkPost(currentWorkPostNum)
				.takeAssemblyProcedureFrom(this.getWorkPost(predecessorWorkPostNum));
			return true;
		}
		return false;
	}
	
	/**
	 * See {@link AssemblyLineState#getWorkPost(int) getWorkPost(int)}
	 */
	private WorkPost getWorkPost(int workPostNum) throws IllegalArgumentException {
		return this.getState().getWorkPost(workPostNum);
	}
	
	/**
	 * See {@link AssemblyLineState#getFirstWorkPost() getFirstWorkPost()}
	 */
	private WorkPost getFirstWorkPost() {
		return this.getState().getFirstWorkPost();
	}
	
	/**
	 * See {@link AssemblyLineState#getLastWorkPost() getLastWorkPost()}
	 */
	private WorkPost getLastWorkPost() {
		return this.getState().getLastWorkPost();
	}
	
	/**
	 * Determine whether the specified WorkPost is both not empty and is finished.
	 * 
	 * @param workPostNum
	 * 		The number of the WorkPost to check
	 * @return The specified WorkPost is both not empty and finished.
	 */
	private boolean isWorkPostFinished(int workPostNum) {
		return this.isWorkPostFinished(this.getWorkPost(workPostNum));
	}
	
	/**
	 * Determine whether the specified WorkPost is both not empty and is finished.
	 * 
	 * @param workPost
	 * 		The WorkPost to check
	 * @return The specified WorkPost is both not empty and finished.
	 */
	private boolean isWorkPostFinished(WorkPost workPost) {
		return ! workPost.isEmpty() && workPost.isFinished();
	}
	
	/**
	 * See {@link AssemblyLineState#getElapsedTime() getElapsedTime()}
	 */
	private DateTime getElapsedTime() {
		return this.getState().getElapsedTime();
	}
	
	/**
	 * See {@link AssemblyLineState#getAssemblyLineSize() getAssemblyLineSize()}
	 */
	private int getAssemblyLineSize() {
		return this.getState().getAssemblyLineSize();
	}
	
	/**
	 * See {@link AssemblyLineState#handleFinishedAssemblyProcedure() handleFinishedAssemblyProcedure()}
	 */
	private void handleFinishedAssemblyProcedure(Optional<AssemblyProcedure> procedure) {
		this.getState().handleFinishedAssemblyProcedure(procedure);
	}

	/**
	 * See {@link AssemblyLineState#makeAssemblyProcedure() makeAssemblyProcedure()}
	 */
	private AssemblyProcedure makeAssemblyProcedure(Optional<Order> order) throws IllegalArgumentException {
		return this.getState().makeAssemblyProcedure(order);
	}
	
	/**
	 * Take a new Order from the given list of Orders
	 */
	private Optional<Order> popNextOrderFromSchedule(List<Order> orders) {
		if (orders.isEmpty()) {
			return Optional.absent();
		} else {
			return Optional.fromNullable(orders.remove(0));
		}
	}
	
	/**
	 * The list of orders cannot be null or contain null
	 * 
	 * @param orders
	 * 		The list of orders to check
	 * @return orders is not null and does not contain null
	 */
	private boolean isValidListOrders(List<Order> orders) {
		return ! (orders == null || orders.contains(null));
	}
}