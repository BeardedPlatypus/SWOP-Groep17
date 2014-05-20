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
	/**
	 * Construct a new VirtualAssemblyLine with the giving taskTypeSequence and
	 * current state of the AssemblyLine.
	 */
	public VirtualAssemblyLine(TaskType[] taskTypeSequence,
			List<Optional<Order>> curState) {
		this.taskTypeSequence = taskTypeSequence;
		this.curState = new ArrayList<>(curState);
	}

	// --------------------------------------------------------------------------
	// timeToFinish methods and related sub functions.
	// --------------------------------------------------------------------------
	public DateTime timeToFinish(List<Order> inputOrders) {
		List<Optional<Order>> orderSeq = this
				.getNewAssemblyLineOrderSeq(inputOrders);
		TaskType[] taskTypeSeq = this.getTaskTypeSequence();
		
		
		return null;
	}

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
	private List<Optional<Order>> getNewAssemblyLineOrderSeq(
			List<Order> inputOrders) throws IllegalArgumentException {
		if (inputOrders == null || inputOrders.contains(null)) {
			throw new IllegalArgumentException(
					"The input orders cannot be null or contain null.");
		}

		List<Optional<Order>> newAssemblyLineOrderSeq = new ArrayList<>();

		for (Order order : inputOrders) {
			newAssemblyLineOrderSeq.add(Optional.of(order));
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
	private List<Optional<Order>> getCurState() {
		return this.curState;
	}

	/** List of the current position of orders on the VirtualAssemblyLine. */
	private final List<Optional<Order>> curState;

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
