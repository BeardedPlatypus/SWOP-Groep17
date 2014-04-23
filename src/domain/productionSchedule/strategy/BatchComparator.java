package domain.productionSchedule.strategy;

import java.util.Comparator;

import domain.Specification;
import domain.order.Order;

/**
 * Imposes an ordering on Order objects based on whether their Specification
 * match those of the Specification favoured by the BatchComparator.
 * If both Specification objects match the favoured Specification, then
 * the submission time will determine the ordering. The same holds if both
 * Specification objects do not match the favoured Specification.
 * 
 * @author Thomas Vochten
 *
 */
public class BatchComparator implements Comparator<Order> {

	/** Initialise the new BatchComparator with the specified
	 *  Specification.
	 *  
	 *  @param spec
	 *  	The Specification that determines the ordering imposed by the new
	 *  	BatchComparator
	 *  @throws IllegalArgumentException
	 *  	spec is null
	 */
	public BatchComparator(Specification spec) throws IllegalArgumentException {
		if (spec == null) {
			throw new IllegalArgumentException("Cannot initialise a BatchComparator"
					+ "with a non-existent specification.");
		}
		this.spec = spec;
		this.fifoComp = new FifoComparator();
	}
	
	@Override
	public int compare(Order order1, Order order2) {
		boolean order1Matches = order1.getSpecifications()
				.equals(this.getSpecification());
		boolean order2Matches = order2.getSpecifications()
				.equals(this.getSpecification());
		if (order1Matches == order2Matches) {
			return this.getFifoComparator().compare(order1, order2);
		}
		if (order1Matches) {
			return -1;
		}
		else return 1;
	}

	/** Get the Specification that determines the ordering. */
	private Specification getSpecification() {
		return this.spec;
	}
	
	/** The Specification that determines the ordering. */
	private Specification spec;
	
	/** Get this BatchComparator's FifoComparator. */
	private FifoComparator getFifoComparator() {
		return this.fifoComp;
	}
	
	/** Used in case both Specifications match this BatchComparator's specification
	 *  or if neither Specification matches.  */
	private FifoComparator fifoComp;
}
