package domain.production_schedule.strategy;

import java.util.Collections;
import java.util.List;

import domain.car.Specification;
import domain.order.Order;

/**
 * 
 * @author Martinus Wilhelmus Tegelaers, Thomas Vochten
 *
 */
public class BatchStrategy<O extends Order> extends SchedulingStrategy<O> {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialise a new BatchStrategy with the specified Specification
	 * 
	 * @param specs
	 * 		The Specification used to compare Orders
	 * @throws IllegalArgumentException
	 * 		specs is null
	 */
	public BatchStrategy(Specification specs) throws IllegalArgumentException {
		if (specs == null) {
			throw new IllegalArgumentException("Cannot initialise a BatchStrategy"
					+ "with null specs");
		}
		this.comparator = new BatchComparator(specs);
	}
	
	//--------------------------------------------------------------------------
	// Scheduling Strategy methods.
	//--------------------------------------------------------------------------
	@Override
	public String getName() {
		return "Batch strategy";
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
		return ! this.getComparator().getSpecification()
				.equals(orderQueue.get(0).getSpecifications());
	}
	
	//--------------------------------------------------------------------------
	// Comparator methods
	//--------------------------------------------------------------------------
	 /** The comparator used to compare Orders. */
	private BatchComparator comparator;
	
	/**
	 * Get this BatchStrategy's BatchComparator.
	 * 
	 * @return The BatchComparator
	 */
	private BatchComparator getComparator() {
		return this.comparator;
	}
}
