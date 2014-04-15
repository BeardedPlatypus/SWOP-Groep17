package domain;

import java.util.ArrayList;
import java.util.List;

import domain.AssemblyTask;

/**
 * An assembly procedure specifies a sequence of tasks that must be completed in order to fulfil an order.
 * 
 * @invariant getAssemblyTasks() != null
 * @invariant getOrder() != null && getOrderContainer != null
 */
public class AssemblyProcedure implements AssemblyProcedureContainer {
	//--------------------------------------------------------------------------
	// Constructor 
	//--------------------------------------------------------------------------
	/**
	 * Initialise this new AssemblyProcedure with the specified Order and AssemblyTasks.
	 * 
	 * @param order
	 * 		The Order that this procedure fulfils.
	 * @param tasks
	 * 		The sequence of AssemblyTasks that must be performed in order to fulfil the specified order.
	 * 
	 * @throws IllegalArgumentException
	 * 		| order == null || tasks == null
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

	//--------------------------------------------------------------------------
	// Task related methods and variables. 
	//--------------------------------------------------------------------------
	/**
	 * Complete the task specified with intTask of taskType within this
	 * assembly procedure.
	 * 
	 * @param intTask
	 * 		The number that corresponds to the task of taskType that is completed.
	 * @param taskType
	 * 		The type of the task that is completed.
	 * 
	 * @post let task = this.getAssemblyTasks(taskType).get(intTask) in
	 * 			! task.isCompleted() => (new task).isCompleted() == true
	 * 
	 * @throws IllegalArgumentException
	 * 		An invalid task number is supplied | 0 > intTask || intTask >= getAssemblyTasks.size()
	 * @throws IllegalArgumentException
	 * 		A task of a type different from taskType was selected
	 */
	public void completeTask(int intTask, TaskType taskType) throws IllegalArgumentException {
		if (! isValidTaskNumber(intTask)) {
			throw new IllegalArgumentException("Task number is not a correct value for this procedure.");
		}
		if(tasks.get(intTask).getTaskType()!=taskType)
			throw new IllegalArgumentException("Chosen task is not a task of the correct type.");
		tasks.get(intTask).setCompleted(true);
	}
	
	/**
	 * Get the specified AssemblyTask from this AssemblyProcedure.
	 * 
	 * @param intTask
	 * 		The int that specifies an AssemblyTask from this AssemblyProcedure.
	 * 
	 * @return The AssemblyTask that corresponds with the given intTask. 
	 * 
	 * @throws IllegalArgumentException
	 * 		intTask is an invalid index into the list of tasks.
	 */
	public AssemblyTask getTask(int intTask) throws IllegalArgumentException {
		if (! this.isValidTaskNumber(intTask)) {
			throw new IllegalArgumentException("intTask refers to a non-existent"
					+ "task");
		}
		return this.getTasksInternal().get(intTask);
	}

	/** 
	 * Check if this AssemblyProcedure is finished.
	 * 
	 * @return True if this AssemblyProcedure is finished, else false. 
	 */
	public boolean isFinished() {
		for (AssemblyTask task : this.getTasksInternal()) {
			if (! task.isCompleted()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check whether the specified task number refers to an existent task.
	 * @param intTask
	 * 		Index into the list of tasks.
	 * @return
	 * 		Whether intTask is a valid index into the list of tasks.
	 */
	private boolean isValidTaskNumber(int intTask) {
		return intTask >= 0 && intTask < this.getAssemblyTasks().size();
	}
	
	/**
	 * Get this AssemblyProcedure's AssemblyTask objects for internal use.
	 * @return
	 * 		This AssemblyProcedure's AssemblyTask objects.
	 */
	private List<AssemblyTask> getTasksInternal() {
		return this.tasks;
	}
	
	/** The tasks that this procedure contains */
	private final List<AssemblyTask> tasks;
	//--------------------------------------------------------------------------
	// Order related methods and variables. 
	//--------------------------------------------------------------------------
	/**
	 * Get the Order of this AssemblyProcedure. 
	 * 
	 * @return the Order of this AssemblyProcedure. 
	 */
	private Order getOrderInternal() {
		return this.assemblyOrder;
	}
	
	/** The Order that this AssemblyProcedure fulfills. */
	private final Order assemblyOrder;

	//--------------------------------------------------------------------------
	// getStatistics related methods and variables. 
	//--------------------------------------------------------------------------	
	public void makeStatisticsEvent() {
		throw new UnsupportedOperationException();
	}

	//--------------------------------------------------------------------------
	//  AssemblyProcedureContainer immutable views.
	//--------------------------------------------------------------------------
	@Override
	public OrderContainer getOrder() {
		return this.getOrderInternal();
	}

	@Override
	public List<AssemblyTaskContainer> getAssemblyTasks() {
		return new ArrayList<AssemblyTaskContainer>(tasks);
	}

	@Override
	public List<AssemblyTaskContainer> getAssemblyTasks(TaskType taskType) {
		ArrayList<AssemblyTaskContainer> typeTasks = new ArrayList<AssemblyTaskContainer>();
		for(AssemblyTaskContainer task : this.getAssemblyTasks()){
			if(task.getTaskType().equals(taskType))
				typeTasks.add(task);
		}
		return typeTasks;
	}
}