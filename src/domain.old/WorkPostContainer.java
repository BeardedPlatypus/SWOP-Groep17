package domain;

/**
 * Exposes a view of this work post to the outside world.
 */

import java.util.List;

public interface WorkPostContainer {
	
	/**
	 * Gets this work post's number.
	 */
	public int getWorkPostNumber();
	
	/**
	 * Gets this work post's name.
	 */
	public String getName();
	
	/**
	 * Gets this work post's type.
	 */
	public TaskType getWorkPostType();

	/**
	 * Gets the views for the assembly tasks that are relevant to this work post.
	 */
	public List<AssemblyTaskContainer> getMatchingAssemblyTasks();
	
}