package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for work posts that are part of an assembly line.
 */

public class WorkPost implements WorkPostContainer {
	
	/**
	 * The work post's name.
	 */
	private String name;
	
	/**
	 * The work post's number.
	 */
	private int workPostNum;
	
	/**
	 * The work post's type.
	 */
	private TaskType workPostType;
	
	/**
	 * The assembly procedure the work post is currently working on.
	 */
	private AssemblyProcedure activeAssembly;
	
	/**
	 * Initialises a new work post with the given work post type.
	 * @param workPostType
	 * 		The type of the new work post.
	 * @post
	 * 		The new work post is associated with the given work post type.
	 * @throws IllegalArgumentException
	 */
	public WorkPost(TaskType workPostType, int workPostNum) throws IllegalArgumentException {
		if (workPostType == null) {
			throw new IllegalArgumentException("Cannot initialise a work post with a non-existent work post type.");
		}
		this.workPostType = workPostType;
		this.name = workPostType.toString();
		this.workPostNum = workPostNum;
	}
	
	/**
	 * Getter for the work post type.
	 */
	public TaskType getTaskType() {
		return this.workPostType;
	}

	@Override
	public List<AssemblyTaskContainer> getMatchingAssemblyTasks() {
		if (this.getAssemblyProcedure() == null) {
			return new ArrayList<AssemblyTaskContainer>();
		}
		return this.activeAssembly.getAssemblyTasks(this.getTaskType());
	}
	
	/**
	 * Gets the domain object version of the assembly procedure
	 * that this work post is currently working on.
	 */
	protected AssemblyProcedure getAssemblyProcedure(){
		return activeAssembly;
	}
	
	/**
	 * Gets a view of the assembly procedure that this work post
	 * is currently working on.
	 */
	public AssemblyProcedureContainer getAssemblyProcedureContainer() {
		return this.activeAssembly;
	}
	
	/**
	 * Sets the assembly procedure for this work post to work on
	 * to the given assembly procedure.
	 * @param assemblyProcedure
	 * 		The assembly procedure for this work post to work on.
	 */
	public void setAssemblyProcedure(AssemblyProcedure assemblyProcedure) {
		this.activeAssembly = assemblyProcedure;
	}

	/**
	 * Completes the work post's assembly procedure's task specified by intTask.
	 * @param intTask
	 * 		The number of the task in this work post's assembly procedure.
	 * @throws IllegalArgumentException
	 * 		intTask is smaller than 0, or intTask is equal to or greater than
	 * 		the number of tasks in this work post's assembly procedure.
	 * @throws IllegalArgumentException
	 * 		intTask refers to a task that has a type different from this
	 * 		work post's type.
	 */
	public void completeTask(int intTask) throws IllegalArgumentException {
		this.activeAssembly.completeTask(intTask, this.getTaskType());
	}

	/**
	 * Gets a view of the order encapsulated in this work post's assembly procedure.
	 */
	public OrderContainer getOrderContainer() {
		return this.getAssemblyProcedure().getOrderContainer();
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public int getWorkPostNumber() {
		return this.workPostNum;
	}

	@Override
	public TaskType getWorkPostType() {
		return this.workPostType;
	}
}