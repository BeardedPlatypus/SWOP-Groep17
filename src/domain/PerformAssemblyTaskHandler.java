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
	public AssemblyLine assemblyLine;

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
	 * Returns views of the contained assembly line's work posts.
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return this.assemblyLine.getWorkPosts();
	}

	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) {
		return this.assemblyLine.getAssemblyTasksAtPost(workPostNumber);
	}

	public void completeWorkpostTask(int workPostNumber, int taskNumber) {
		this.assemblyLine.completeWorkpostTask(workPostNumber, taskNumber);
	}
}