package domain;

import java.util.List;

/**
 * Coordinates with the outside world in order to complete tasks at a single work post.
 *
 */
public class PerformAssemblyTaskHandler {
	
	/**
	 * The assembly line this handler operates on.
	 */
	public AssemblyLine assemblyLine;

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
	 * Gets views of the work posts part of this handler's assembly line.
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return this.assemblyLine.getWorkPosts();
	}

	/**
	 * Gets views of the tasks that are relevant to the specified work post.
	 * @param workPostNumber
	 * 		The number of the work post.
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a work post that does not exist.
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
	public void completeWorkpostTask(int workPostNumber, int taskNumber) {
		this.assemblyLine.completeWorkpostTask(workPostNumber, taskNumber);
	}
}