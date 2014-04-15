package domain;

/**
 * Order class of the Assembly Line software system.
 * It provides a mostly immutable set of properties of an order, the model,
 * specification, and initialisation are immutable and set at creation.
 * An Order is created as !isCompleted, and can only be set to completed once.
 * The estimatedCompletionTime can be updated as time progresses.
 * 
 * Order objects are created by the ProductionScheduler, which keeps track of
 * them until they are placed on the AssemblyLine.
 * Once completed, they are placed in the completedOrderSet of the Manufacturer.
 * 
 * @author Martinus Wilhelmus Tegelaers
 * 
 * @invariant order.getModel() != null
 * @invariant order.getSpecifications() != null
 * @invariant order.getInitialisationTime() != null
 * @invariant order.getEstimatedCompletionTime() != null
 * @invariant order.getModel().isValidSpecification(order.getSpecifications())
 */
public class Order implements OrderContainer {
	public Model model;
	public Specification specifications;

	public void getSpecification() {
		throw new UnsupportedOperationException();
	}

	public DateTime getSubmissionTime() {
		throw new UnsupportedOperationException();
	}

	public void isCompleted() {
		throw new UnsupportedOperationException();
	}

	public void getCompletionTime() {
		throw new UnsupportedOperationException();
	}

	public Model getModel() {
		return this.model;
	}

	public Specification getSpecifications() {
		return this.specifications;
	}
}