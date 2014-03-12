package domain;

import java.util.List;

/**
 * Coordinates with the outside world in order to complete assembly tasks of a single workpost.
 * 
 *
 */
public class PerformAssemblyTaskHandler {
	
	/**
	 * The assembly line this handler operates on.
	 */
	private AssemblyLine assemblyLine;

	/**
	 * Instantiates a new handler with the given assembly line.
	 * @param assemblyLine
	 * 		The assembly line that the new handler will operate on.
	 * @throws IllegalArgumentException
	 * 		assemblyLine is non-existent.
	 */
	public PerformAssemblyTaskHandler(AssemblyLine assemblyLine) throws IllegalArgumentException {
		if (assemblyLine == null) {
			throw new IllegalArgumentException("Cannot initialise perform assembly task handler with non-existent assembly line.");
		}
		this.assemblyLine = assemblyLine;
	}
	
	/**
	 * 
	 * Gets views of the contained assembly line's work posts.
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return this.assemblyLine.getWorkPostContainers();
	}
	
	/**
	 * Gets views of the tasks at the specified work post.
	 * @param workPostNumber
	 * 		Specifies the work post of interest.
	 * @return
	 * 		A list of tasks at the specified work post.
	 * @throws workPostNumber refers to a work post that does not exist.
	 */
	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) throws IllegalArgumentException {
		return this.assemblyLine.getAssemblyTasksAtPost(workPostNumber);
	}

	/**
	 * Performs the specified task at the specified work post.
	 * @param workPostNumber
	 * 		The number of the work post.
	 * @param taskNumber
	 * 		The number of the task.
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a work post that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task with a type incompatible with the given
	 * 		work post.
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		this.assemblyLine.completeWorkpostTask(workPostNumber, taskNumber);
	}
}
