package domain.assembly_line;

import domain.car.Option;

//TODO: update class documentation
/**
 * One single task that needs to be performed as part of the assembly process. Painting a car blue is an example
 * of an assembly task.
 */
public class AssemblyTask implements AssemblyTaskView {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	//TODO: update constructor
	/**
	 * Initialises a new assembly task with the given name, action info and type.
	 * 
	 * @param option
	 * 		The Option that the new AssemblyTask must implement.
	 * @param taskNumber
	 * 		The number of the new AssemblyTask.
	 * 
	 * @throws IllegalArgumentException
	 * 		A non-existent option was supplied.
	 * @throws IllegalArgumentException
	 * 		A negative taskNumber was supplied.
	 */
	public AssemblyTask(Option option, int taskNumber) throws IllegalArgumentException {
		if (option == null) {
			throw new IllegalArgumentException("Cannot initialise task with non-existent Option.");
		}
		if (taskNumber < 0) {
			throw new IllegalArgumentException("Cannot initialise task with negative taskNumber.");
		}
		
		this.option = option;
		this.taskNumber = taskNumber;
		this.isCompleted = false;	
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
	public Option getOption() {
		return this.option;
	}
	
	/** The option from which this AssemblyTask is generated. */
	private final Option option;	
	//--------------------------------------------------------------------------
	// TaskType related methods and variables.
	//--------------------------------------------------------------------------
	@Override
	public TaskType getTaskType() {
		return this.getOption().getType();
	}

	//--------------------------------------------------------------------------
	@Override
	public int getTaskNumber() {
		return this.taskNumber;
	}
	
	/** The task's number in the AssemblyProcedure. */
	private int taskNumber;
}