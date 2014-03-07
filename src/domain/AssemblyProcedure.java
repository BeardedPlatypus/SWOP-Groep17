package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * An assembly procedure specifies a sequence of tasks that must be completed in order to fulfill an order.
 * 
 * @invariant getAssemblyTasks() != null
 * @invariant getOrder() != null && getOrderContainer != null
 *
 */
public class AssemblyProcedure implements AssemblyProcedureContainer {
	
	/**
	 * The tasks that this procedure contains
	 */
	private final List<AssemblyTask> tasks;
	/**
	 * The order that this procedure fulfills.
	 */
	private final Order assemblyOrder;
	
	/**
	 * Initialises the new procedure with the given order and the given tasks.
	 * @param order
	 * 		The order that this procedure fulfills.
	 * @param tasks
	 * 		The sequence of tasks that must be performed in order to fulfill the given order.
	 * @throws IllegalArgumentException
	 */
	public AssemblyProcedure(Order order, List<AssemblyTask> tasks) throws IllegalArgumentException {
		if (order == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent order.");
		}
		if (tasks == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent tasks.");
		}
		this.assemblyOrder = order;
		this.tasks = tasks;
	}
	
	/**
	 * Get the order fulfilled by this assembly procedure.
	 */
	public Order getOrder() {
		return this.assemblyOrder;
	}

	/**
	 * Gets the views of the tasks specified by this procedure.
	 */
	public List<AssemblyTaskContainer> getAssemblyTasks(){
		return new ArrayList<AssemblyTaskContainer>(tasks);
	}
	
	/**
	 * Gets the views of the tasks specified by this procedure that are of the given type.
	 * @param taskType
	 * 		The type of task to include in the result.
	 * @return
	 * 		A list of task views that are of the given type.
	 */
	public List<AssemblyTaskContainer> getAssemblyTasks(TaskType taskType) {
		ArrayList<AssemblyTaskContainer> typeTasks = new ArrayList<AssemblyTaskContainer>();
		for(AssemblyTaskContainer task : this.getAssemblyTasks()){
			if(task.getTaskType().equals(taskType))
				typeTasks.add(task);
		}
		return typeTasks;
	}

	/**
	 * Complete the task specified with intTask of taskType within this
	 * assembly procedure.
	 * @param intTask
	 * 		Specifies the task of taskType that is completed.
	 * @param taskType
	 * 		The type of the task that is completed.
	 * @post let task = this.getAssemblyTasks(taskType).get(intTask) in
	 * 			! task.isCompleted() => (new task).isCompleted() == true
	 * @throws IllegalArgumentException
	 * 		An invalid task number is supplied | 0 > intTask || intTask > getAssemblyTasks.size()
	 * @throws IllegalArgumentException
	 * 		A task of a type different from taskType was selected
	 */
	public void completeTask(int intTask, TaskType taskType) throws IllegalArgumentException {
		if (intTask < 0 || intTask >= this.getAssemblyTasks().size()) {
			throw new IllegalArgumentException("Task number is not a correct value for this procedure.");
		}
		if(tasks.get(intTask).getTaskType()!=taskType)
			throw new IllegalArgumentException("Chosen task is not a task of the correct type.");
		tasks.get(intTask).setCompleted(true);
	}

	/**
	 * Gets a view of the order fulfilled by this assembly procedure.
	 */
	public OrderContainer getOrderContainer() {
		return this.getOrder();
	}
}
