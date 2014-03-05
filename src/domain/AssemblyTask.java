package domain;

/**
 * One single task that needs to be performed as part of the assembly process. Painting a car blue is an example
 * of an assembly task.
 */

public class AssemblyTask {
	
	/**
	 * Whether this task is held completed.
	 */
	private boolean isCompleted;
	
	/**
	 * Short name to refer to this task.
	 */
	private String name;
	
	/**
	 * Brief description of what exactly must be done to complete this task.
	 */
	private String actionInfo;
	
	/**
	 * The task's type. A task should only be assigned to a work post with the same type.
	 */
	private TaskType type;
	
	/**
	 * Initialises a new assembly task with the given name, action info and type.
	 * 
	 * @param name
	 * 		The new task's name.
	 * @param actionInfo
	 * 		A brief description of what exactly must be done to complete the new task.
	 * @param type
	 * 		The new task's type.
	 * @throws IllegalArgumentException
	 * 		A non-existent name was supplied.
	 * @throws IllegalArgumentException
	 * 		A non-existent action info was supplied.
	 * @throws IllegalArgumentException
	 * 		A non-existent task type was supplied.
	 */
	public AssemblyTask(String name, String actionInfo, TaskType type) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Cannot initialise task with non-existent name.");
		}
		if (actionInfo == null) {
			throw new IllegalArgumentException("Cannot initialise task with non-existent action info.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Cannot initialise task with non-existent task type.");
		}
		this.name = name;
		this.actionInfo = actionInfo;
		this.type = type;
		this.isCompleted = false;
		
	}

	/**
	 * Returns a view of this task. Use this method to expose this task to the outside world.
	 * @param taskNumber
	 * 		A number, determined by the order of tasks of the assembly procedure that contains this task.
	 * @return A view of this task with the same completion state, name, action info and task type.
	 * 		The view's number will be as specified in the 'param' entry.
	 */
	public AssemblyTaskInfo getTaskInfo(int taskNumber) {
		return new AssemblyTaskInfo(this.isCompleted(), this.getName(), this.getActionInfo(), taskNumber, this.getTaskType());
	}
	
	/**
	 * Getter for this task's type.
	 */
	public TaskType getTaskType() {
		return this.type;
	}
	
	/**
	 * Getter for this task's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter for this task's action info. The action info is a brief description of what exactly must be done to
	 * complete this task.
	 */
	public String getActionInfo() {
		return this.actionInfo;
	}
	
	/**
	 * Determines whether this task is held completed.
	 */
	public boolean isCompleted() {
		return this.isCompleted;
	}
	
	/**
	 * Sets the completion state of this task.
	 * @param completed
	 * 		The new completion state of this task.
	 * @post This task's new completion state will be set to the value of the parameter.
	 */
	public void setCompleted(boolean completed) {
		this.isCompleted = completed;
	}
}