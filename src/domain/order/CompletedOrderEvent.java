package domain.order;

import domain.statistics.ProcedureStatistics;

/**
 * Contains information about a completed Order, meant to be consumed
 * by a CompletedOrderObserver.
 * 
 * @author Thomas Vochten
 *
 */
public class CompletedOrderEvent {

	/**
	 * Initialise a new CompletedOrderEvent
	 * 
	 * @param completedOrder
	 * 		The completed Order
	 * @param statistics
	 * 		Statistics about the completed Order
	 * @throws IllegalArgumentException
	 * 		completedOrder is null
	 * @throws IllegalArgumentException
	 * 		statistics is null
	 */
	public CompletedOrderEvent(Order completedOrder, ProcedureStatistics statistics)
		throws IllegalArgumentException {
		if (completedOrder == null) {
			throw new IllegalArgumentException("Cannot initialise a CompletedOrderEvent"
					+ "with null completedOrder");
		}
		if (statistics == null) {
			throw new IllegalArgumentException("Cannot initialise a CompletedOrderEvent"
					+ "with null completedOrder");
		}
		this.completedOrder = completedOrder;
		this.statistics = statistics;
	}
	
	/** The completed Order */
	private final Order completedOrder;
	
	/**
	 * @return The completed Order
	 */
	public Order getCompletedOrder() {
		return this.completedOrder;
	}
	
	/** The statistics about the completed Order */
	private final ProcedureStatistics statistics;
	
	/**
	 * @return Statistics about the completed Order
	 */
	public ProcedureStatistics getProcedureStatistics() {
		return this.statistics;
	}
}
