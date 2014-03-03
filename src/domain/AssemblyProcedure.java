import java.util.ArrayList;

public class AssemblyProcedure {
	public Car_Manufacturer unnamed_Car_Manufacturer_;
	public ProductionSchedule unnamed_ProductionSchedule_;
	public WorkPost unnamed_WorkPost_;
	public ArrayList<AssemblyTask> tasks = new ArrayList<AssemblyTask>();
	public AssemblyLine unnamed_AssemblyLine_;
	public Order assemblyOrder;

	private void generateTasks() {
		throw new UnsupportedOperationException();
	}

	public void getOrder() {
		throw new UnsupportedOperationException();
	}

	public AssemblyTaskInfo[] getAssemlyTasks(Object taskType) {
		throw new UnsupportedOperationException();
	}

	public void completeTask(Object intTask, Object taskType) {
		throw new UnsupportedOperationException();
	}

	public OrderContainer getOrderContainer() {
		throw new UnsupportedOperationException();
	}
}