package domain.production_schedule.strategy;

import java.util.Collections;
import java.util.List;

import domain.order.Order;

/**
 * 
 * @author Martinus Wilhelmus Tegelaers, Thomas Vochten
 *
 */
public class FifoStrategy<O extends Order> extends SchedulingStrategy<O> {
	
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
	public int compare(O o, O p) {
		return this.getComparator().compare(o, p);
	}
	
	@Override
	public void sort(List<O> orderQueue) {
		Collections.sort(orderQueue, this.getComparator());
	}

	@Override
	public boolean isDone(List<O> orderQueue) {
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
