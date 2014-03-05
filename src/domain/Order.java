public class Order implements OrderContainer{
	private boolean completed;
	private Model model;
	private Specifications specifications;
	private DateTime estimatedCompletionTime;
	public Production_System unnamed_Production_System_;
	public Manufacturer unnamed_Manufacturer_;
	public AssemblyProcedure unnamed_AssemblyProcedure_;
	public Model orderModel;

	@Override
	public boolean isCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean isCompleted) {
		this.completed = isCompleted;
	}

	public String toString() {
		throw new UnsupportedOperationException();
	}

	public void updateEstComplTime(Object dateTime) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Specifications getSpecifications() {
		// TODO Auto-generated method stub
		return null;
	}
}