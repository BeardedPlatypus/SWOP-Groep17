package domain;

import java.util.ArrayList;
import java.util.List;
import domain.order.*;

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
	 * Initialise this new AssemblyProcedure with the specified Order, AssemblyTasks
	 * and expected minutes.
	 * 
	 * @param order
	 * 		The Order that this procedure fulfills.
	 * @param tasks
	 * 		The sequence of AssemblyTasks that must be performed in order to fulfill the specified order.
	 * @param expectedMinutes
	 * 		The amount of minutes that this procedure is expected to spend on the AssemblyLine
	 * 		in total.
	 * 
	 * @throws IllegalArgumentException
	 * 		| order == null || tasks == null || expectedMinutes < 0
	 */
	public AssemblyProcedure(Order order, List<AssemblyTask> tasks, int expectedMinutes) throws IllegalArgumentException {
		if (order == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent order.");
		}
		if (tasks == null) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with non-existent tasks.");
		}
		if (expectedMinutes < 0) {
			throw new IllegalArgumentException("Cannot initialise an assembly procedure with a negative amount"
					+ "of expected minutes.");
		}
		this.assemblyOrder = order;
		this.tasks = tasks;
		this.expectedMinutes = expectedMinutes;
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
	 * Check if all tasks of type have been completed.
	 * 
	 * @param type
	 * 		Type of task to check for
	 * @return All tasks of type have been completed
	 */
	public boolean isFinished(TaskType type) {
		for (AssemblyTask task : this.getTasksInternal()) {
			if (task.getTaskType() == type && ! task.isCompleted()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the AssemblyTask with the specified task number has been finished.
	 * 
	 * @return The task is finished
	 * @throws IllegalArgumentException
	 * 		taskNum refers to a task that does not exist
	 */
	public boolean taskIsFinished(int taskNum) throws IllegalArgumentException {
		try {
			return this.getTasksInternal().get(taskNum).isCompleted();
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("taskNum refers to a task"
					+ "that does not exist");
		}
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
	/** The amount of minutes that this AssemblyProcedure is expected to
	 * spend on the AssemblyLine. */
	private final int expectedMinutes;
	
	/**
	 * Get the amount of minutes that this AssemblyProcedure is expected to
	 * spend on the AssemblyLine.
	 * 
	 * @return The amount of minutes.
	 */
	public int getExpectedMinutes() {
		return this.expectedMinutes;
	}
	
	/** The amount of time that this AssemblyProcedure has spent on the
	 * AssemblyLine so far. */
	private int elapsedMinutes;
	
	/**
	 * Add the specified amount of minutes to the time spent on the
	 * AssemblyLine so far.
	 * 
	 * @param minutes
	 * 		The amount of minutes to add
	 * @throws IllegalArgumentException
	 * 		minutes is negative
	 */
	public void addToElapsedMinutes(int minutes) throws IllegalArgumentException {
		if (minutes < 0) {
			throw new IllegalArgumentException("Cannot add a negative amount of time"
					+ "to the elapsed time");
		}
		this.setElapsedMinutes(this.getElapsedMinutes() + minutes);
	}
	
	/**
	 * Get the amount of minutes spent on the AssemblyLine so far.
	 * 
	 * @return The amount of minutes
	 */
	private int getElapsedMinutes() {
		return this.elapsedMinutes;
	}
	
	/**
	 * Set the time spent on the AssemblyLine so far to the specified value.
	 * 
	 * @param minutes
	 * 		The amount of minutes
	 */
	private void setElapsedMinutes(int minutes) {
		this.elapsedMinutes = minutes;
	}
	
	/**
	 * Calculate the delay on this AssemblyProcedure. It is assumed that this
	 * AssemblyProcedure has been finished before calling this method.
	 * 
	 * @return The delay
	 */
	private int calculateDelay() {
		return this.getElapsedMinutes() - this.getExpectedMinutes();
	}
	
	/**
	 * Make an event wherein all information of interest about this AssemblyProcedure
	 * is stored so it can be recorded.
	 * 
	 * @return A statistical object that contains information about this AssemblyProcedure
	 * @throws IllegalStateException
	 * 		This AssemblyProcedure is not yet finished
	 */
	public ProcedureStatistics makeStatisticsEvent() throws IllegalStateException {
		if (! this.isFinished()) {
			throw new IllegalStateException("Cannot record statistical information"
					+ "of an unfinished AssemblyProcedure");
		}
		return new ProcedureStatistics(this.calculateDelay());
	}
	

	//--------------------------------------------------------------------------
	//  AssemblyProcedureContainer immutable views.
	//--------------------------------------------------------------------------
	@Override
	public Order getOrder() {
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