package domain.assemblyLine.virtualAss;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.assemblyLine.TaskType;
import domain.order.Order;

/**
 * The VirtualAssemblyLine provides an entity to calculate the time it would
 * take for the current state plus a sequence of orders to be finished.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class VirtualAssemblyLine {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Construct a new VirtualAssemblyLine with the giving taskTypeSequence and
	 * current state of the AssemblyLine.
	 * 
	 * @param taskTypeSequence
	 * 		The sequence of tasks of this new VirtualAssemblyLine. 
	 * @param curState
	 * 		The current list of orders on this VirtualAssemblyLine.
	 * 
	 * @throws IllegalArgumentException
	 * 		| curState == null || curState contains null
	 * 		| taskTypeSequence == null || taskTypeSequence.length == 0
	 */
	public VirtualAssemblyLine(TaskType[] taskTypeSequence,
			List<Optional<Order>> curState) throws IllegalArgumentException {
		if (curState == null || curState.contains(null)) {
			throw new IllegalArgumentException("The curState cannot be null or contain null.");
		}
		if (taskTypeSequence == null || taskTypeSequence.length == 0) {
			throw new IllegalArgumentException("taskTypeSequence cannot be null or zero.");
		}
		if (curState.size() != taskTypeSequence.length) {
			throw new IllegalArgumentException("curState and taskTypeSequence have to be the same length.");
		}
		
		for (int i = 0; i < taskTypeSequence.length; i++) {
			if (taskTypeSequence[i] == null) 
				throw new IllegalArgumentException("NO NULLS, VERY BAD.");
		}
		
		this.taskTypeSequence = taskTypeSequence;
		
		List<Optional<VirtualAssProc>> list = new ArrayList<>();
		for (Optional<Order> o : curState) {
			if (o.isPresent()) {
				list.add(Optional.of(new VirtualAssProc(o.get())));
			} else {
				list.add(Optional.<VirtualAssProc> absent());
			}
		}
		this.curState = list;
	}

	// --------------------------------------------------------------------------
	// timeToFinish methods and related sub functions.
	// --------------------------------------------------------------------------
	public DateTime timeToFinish(List<Order> inputOrders) throws IllegalArgumentException {
		if (inputOrders == null || inputOrders.contains(null)) {
			throw new IllegalArgumentException("The input orders cannot be null or contain null.");
		}
		
		int offset = inputOrders.size();
		List<Optional<VirtualAssProc>> orderSeq = this.getNewAssemblyLineOrderSeq(inputOrders);
		TaskType[] taskTypeSeq = this.getTaskTypeSequence();
		
		DateTime res = new DateTime(0, 0, 0);
		
		while (!orderSeqIsFinished(orderSeq)) {
			res = res.addTime(0, 0, minutesToFinishCurStep(orderSeq, offset, taskTypeSeq));
			advanceOrderSeq(orderSeq, offset, taskTypeSeq);
		}
		
		return res;
	}

	//--------------------------------------------------------------------------
	protected boolean orderSeqIsFinished(List<Optional<VirtualAssProc>> orderSeq) {
		for (Optional<VirtualAssProc> v : orderSeq) {
			if (v.isPresent()) {
				return false;
			}
		}
		return true;
	}
	
	protected int minutesToFinishCurStep(List<Optional<VirtualAssProc>> orderSeq,
									   int offset,
									   TaskType[] taskTypeSeq) {
		int timeStepMinutes = 0;
		for(int i = 0; i < taskTypeSeq.length; i++) {
			Optional<VirtualAssProc> proc = orderSeq.get(i + offset);
			if (proc.isPresent()) {
				int timeTask = proc.get().getMinutesOnPostOfType(taskTypeSeq[i]);
				timeStepMinutes = Math.max(timeStepMinutes, timeTask);
			}
		}
		return timeStepMinutes;
	}
	
	protected void advanceOrderSeq(List<Optional<VirtualAssProc>> orderSeq,
								   int offset,
								   TaskType[] taskTypeSeq) {
		//Remove last element.
		orderSeq.set((orderSeq.size()-1), Optional.<VirtualAssProc> absent());
		
		//Set all procedures to not finished.
		for (Optional<VirtualAssProc> v : orderSeq) {
			if (v.isPresent()) {
				v.get().setFinished(false);
			}
		}
		
		//Start shifting
		boolean hasShifted;
		do {
			hasShifted = false;
			// Because we removed the last instance we know for sure it is absent.
			for(int i = 0; i < orderSeq.size() - 1; i++) {
				Optional<VirtualAssProc> v = orderSeq.get(i);
				if (v.isPresent() && !v.get().hasFinished() && !orderSeq.get(i+1).isPresent()) {
					orderSeq.set(i+1, v);
					orderSeq.set(i, Optional.<VirtualAssProc> absent());
					hasShifted = true;
					
					if ((i+1) - offset >= 0 && v.get().getMinutesOnPostOfType(taskTypeSeq[i+1-offset]) > 0) {
						v.get().setFinished(true);
					}
				}
			}
		} while(hasShifted);
		
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Get a new List containing first the specified inputOrders, followed by
	 * the (optional) orders of the cur state of this VirtualAssemblyLine.
	 * 
	 * @param inputOrders
	 *            The orders that should be scheduled next.
	 * 
	 * @return A new list of orders containing first the specified inputOrders,
	 *         followed by the (optional) orders of the cur state of this
	 *         VirtualrAssemblyLine.
	 * 
	 * @throws IllegalArgumentException
	 *             | inputOrders == null || inputOrders.contains(null)
	 */
	private List<Optional<VirtualAssProc>> getNewAssemblyLineOrderSeq(List<Order> inputOrders) throws IllegalArgumentException {
		if (inputOrders == null || inputOrders.contains(null)) {
			throw new IllegalArgumentException(
					"The input orders cannot be null or contain null.");
		}

		List<Optional<VirtualAssProc>> newAssemblyLineOrderSeq = new ArrayList<>();

		for (Order order : inputOrders) {
			newAssemblyLineOrderSeq.add(Optional.of(new VirtualAssProc(order)));
		}

		newAssemblyLineOrderSeq.addAll(this.getCurState());
		return newAssemblyLineOrderSeq;
	}

	// --------------------------------------------------------------------------
	// Properties
	// --------------------------------------------------------------------------
	/**
	 * Get the current state of Orders on this VirtualAssemblyLine.
	 * 
	 * @return The current state of Orders on this VirtualAssemblyLine.
	 */
	private List<Optional<VirtualAssProc>> getCurState() {
		return this.curState;
	}

	/** List of the current position of orders on the VirtualAssemblyLine. */
	private final List<Optional<VirtualAssProc>> curState;

	// --------------------------------------------------------------------------
	/**
	 * Get the sequence of TaskTyes of the workposts of this
	 * VirtualAssemblyLine.
	 * 
	 * @return The sequence of TaskTypes of the workposts of this
	 *         VirtualAssemblyLine.
	 */
	private TaskType[] getTaskTypeSequence() {
		return this.taskTypeSequence;
	}

	/** The sequence of tasktypes of this VirtualAssemblyLine. */
	private final TaskType[] taskTypeSequence;
}