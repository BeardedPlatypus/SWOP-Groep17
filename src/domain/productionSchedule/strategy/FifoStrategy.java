package domain.productionSchedule.strategy;

import java.util.Collections;
import java.util.List;

import domain.order.Order;

/**
 * 
 * @author Martinus Wilhelmus Tegelaers, Thomas Vochten
 *
 */
public class FifoStrategy extends SchedulingStrategy {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialise a new FifoStrategy
	 */
	public FifoStrategy() {
		this.comparator = new FifoComparator();
	}
	
	//--------------------------------------------------------------------------
	// SchedulingStrategy methods.
	//--------------------------------------------------------------------------
	
	@Override
	public String getName() {
		return "First-in first-out strategy";
	}
	
	@Override
	public int compare(Order o, Order p) {
		return this.getComparator().compare(o, p);
	}
	
	@Override
	public void sort(List<Order> orderQueue) {
		Collections.sort(orderQueue, this.getComparator());
	}

	@Override
	public boolean isDone(List<Order> orderQueue) {
		return false;
	}
	
	//--------------------------------------------------------------------------
	// Comparator methods
	//--------------------------------------------------------------------------
	/** The comparator used to compare orders. */
	private FifoComparator comparator;
	
	private FifoComparator getComparator() {
		return this.comparator;
	}
}
