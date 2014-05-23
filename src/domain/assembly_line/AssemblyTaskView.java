package domain.assembly_line;

/**
 * A view of an assembly task. Use this class to expose a task to the outside world.
 */
public interface AssemblyTaskView {
	/** 
	 * Get the TaskType of this AssemblyTask.
	 * 
	 * @return the TaskType of this AssemblyTask
	 */
	public TaskType getTaskType();
	
	/**
	 * Check if this Task is completed.
	 * 
	 * @return True if completed, else false.
	 */
	public boolean isCompleted();
	
	/**
	 * Get the task number of this AssemblyTask.
	 * 
	 * @return The task number of this AssemblyTask.
	 */
	public int getTaskNumber();
	
	/**
	 * Get the name of the Option of this AssemblyTask.
	 * 
	 * @return The name of the Option of this AssTask
	 */
	public String getOptionName();
	
	/**
	 * Get the description of the Option of this AssemblyTask.
	 * 
	 * @return Get the description of the Option of this AssTask.
	 */
	public String getOptionDescription();
}