package domain;

//TODO: update class documentation
/**
 * One single task that needs to be performed as part of the assembly process. Painting a car blue is an example
 * of an assembly task.
 */
public class AssemblyTask implements AssemblyTaskContainer {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	//TODO: update constructor
	/**
	 * Initialises a new assembly task with the given name, action info and type.
	 * 
	 * @param name
	 * 		The new task's name.
	 * @param actionInfo
	 * 		A brief description of what exactly must be done to complete 
	 * 		the new task.
	 * @param type
	 * 		The new task's type.
	 * 
	 * @throws IllegalArgumentException
	 * 		A non-existent name was supplied.
	 * @throws IllegalArgumentException
	 * 		A non-existent action info was supplied.
	 * @throws IllegalArgumentException
	 * 		A non-existent task type was supplied.
	 */
	public AssemblyTask(String name, String actionInfo, TaskType type, int taskNumber) throws IllegalArgumentException {
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
		this.taskNumber = taskNumber;	
	}
	
	//--------------------------------------------------------------------------
	// Description related methods and variables. 
	//--------------------------------------------------------------------------
	@Override
	public boolean isCompleted() {
		return this.isCompleted;
	}
	
	/**
	 * Set the completion state of this task.
	 * 
	 * @param completed
	 * 		The new completion state of this task.
	 * 
	 * @post This task's new completion state will be set to the value of the parameter.
	 */
	public void setCompleted(boolean completed) {
		this.isCompleted = completed;
	}
	
	/** Whether this task is held completed. */
	private boolean isCompleted;
	//--------------------------------------------------------------------------
	@Override
	public String getOptionName() {
		return this.getOption().getName();
	}

	@Override
	public String getOptionDescription() {
		return this.getOption().getDescription();
	}

	/**
	 * Get the Option of this AssemblyTask.
	 * 
	 * @return the Option of this AssemblyTask.
	 */
	private Option getOption() {
		return this.option;
	}
	
	/** The option from which this AssemblyTask is generated. */
	private final Option option;	
	//--------------------------------------------------------------------------
	// TaskType related methods and variables.
	//--------------------------------------------------------------------------
	@Override
	public TaskType getTaskType() {
		return this.type;
	}
	
	/** The task's type. */
	private TaskType type;

	//--------------------------------------------------------------------------
	@Override
	public int getTaskNumber() {
		return this.taskNumber;
	}
	
	/** The task's number in the AssemblyProcedure. */
	private int taskNumber;
}