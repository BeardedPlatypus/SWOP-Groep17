public class Order {
	private boolean completed;
	private Model model;
	private Specifications specifications;
	private DateTime estimatedCompletionTime;
	public Production_System unnamed_Production_System_;
	public Manufacturer unnamed_Manufacturer_;
	public AssemblyProcedure unnamed_AssemblyProcedure_;
	public Model orderModel;

	public boolean isCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean isCompleted) {
		this.completed = isCompleted;
	}

	public void toString() {
		throw new UnsupportedOperationException();
	}

	public void updateEstComplTime(Object dateTime) {
		throw new UnsupportedOperationException();
	}

	public OrderContainer getOrderContainer() {
		throw new UnsupportedOperationException();
	}
}