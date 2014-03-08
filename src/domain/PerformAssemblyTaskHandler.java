package domain;

import java.util.List;

/**
 * Coordinates with the outside world in order to complete assembly tasks of a single workpost.
 * 
 * @author Thomas Vochten
 *
 */
public class PerformAssemblyTaskHandler {
	
	/**
	 * Contains the workposts.
	 */
	private AssemblyLine assemblyLine;

	/**
	 * Initialises this handler with the given assembly line.
	 * 
	 * @param assemblyLine
	 * 		An assembly line this handler will get work posts from.
	 * @throws IllegalArgumentException
	 * 		assemblyLine == null
	 */
	public PerformAssemblyTaskHandler(AssemblyLine assemblyLine) throws IllegalArgumentException {
		if (assemblyLine == null) {
			throw new IllegalArgumentException("Cannot initialise perform assembly task handler with non-existent assembly line.");
		}
		this.assemblyLine = assemblyLine;
	}
	
	/**
	 * Gets views of the contained assembly line's work posts.
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return this.assemblyLine.getWorkPosts();
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
	 * Completes the specified task at the specified work post. Has no effect
	 * if the specified task has already been completed.
	 * @param workPostNumber
	 * 		The number of the work post of which a task is to be completed.
	 * @param taskNumber
	 * 		The number of the task to be completed.
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a work post that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task that does not exist.
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		this.assemblyLine.completeWorkpostTask(workPostNumber, taskNumber);
	}
}