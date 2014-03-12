package domain;

/**
 * A view of an assembly task. Use this class to expose a task to the outside world.
 */

public interface AssemblyTaskContainer {
	
	/**
	 * @return
	 * 		The Type of the contained task
	 */
	public TaskType getTaskType();
	
	/**
	 * @return
	 * 		The name of the contained task
	 */
	public String getName();
	
	/**
	 * @return
	 * 		The tasknumber of the contained task, depicting the order in which tasks are to be executed
	 */
	public int getTaskNumber();
	
	/**
	 * @return
	 * 		A short description on how the task should be carried out, as plain text
	 */
	public String getActionInfo();
	
	/**
	 * @return
	 * 		Whether or not the assembly task is completed
	 */
	public boolean isCompleted();
}