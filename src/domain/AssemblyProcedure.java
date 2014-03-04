package domain;

import java.util.ArrayList;
import java.util.List;

public class AssemblyProcedure {
	
	public ArrayList<AssemblyTask> tasks = new ArrayList<AssemblyTask>();
	public Order assemblyOrder;
	
	public AssemblyProcedure(Order order) throws IllegalArgumentException {
		if (order == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent order.");
		}
		this.assemblyOrder = order;
		this.generateTasks();
	}

	private void generateTasks() {
		throw new UnsupportedOperationException();
	}

	public Order getOrder() {
		return this.assemblyOrder;
	}

	public List<AssemblyTaskInfo> getAssemblyTasks(TaskType taskType) {
		List<AssemblyTaskInfo> toReturn = new ArrayList<AssemblyTaskInfo>();
		int counter = 0;
		for (AssemblyTask task : this.tasks) {
			if (task.getTaskType() == taskType) {
				toReturn.add(task.getTaskInfo(counter++));
			}
		}
		return toReturn;
	}

	public void completeTask(int intTask, TaskType taskType) throws IllegalArgumentException {
		if (intTask < 0) {
			throw new IllegalArgumentException("Task number must be positive.");
		}
		List<AssemblyTask> correctTasks = this.getAllTasksOfType(taskType);
		if (intTask >= correctTasks.size()) {
			throw new IllegalArgumentException("Task number went outside the range of possible tasks.");
		}
		correctTasks.get(intTask).setCompleted(true);
	}
	
	private List<AssemblyTask> getAllTasksOfType(TaskType taskType) {
		List<AssemblyTask> toReturn = new ArrayList<AssemblyTask>();
		for (AssemblyTask task : this.tasks) {
			if (task.getTaskType() == taskType) {
				toReturn.add(task);
			}
		}
		return toReturn;
	}

	public OrderContainer getOrderContainer() {
		return this.getOrder();
	}
}