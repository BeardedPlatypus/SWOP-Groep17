package domain;

import java.util.ArrayList;
import java.util.List;

public class WorkPost implements WorkPostContainer {
	
	private String name;
	private int workPostNum;
	public TaskType workPostType;
	public AssemblyProcedure activeAssembly;
	
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

	public List<AssemblyTaskContainer> getAssemblyTasks() {
		if (this.getAssemblyProcedure() == null) {
			return new ArrayList<AssemblyTaskContainer>();
		}
		return this.activeAssembly.getAssemblyTasks(this.getTaskType());
	}
	
	public AssemblyProcedure getAssemblyProcedure() {
		return this.activeAssembly;
	}
	
	public void setAssemblyProcedure(AssemblyProcedure assemblyProcedure) {
		this.activeAssembly = assemblyProcedure;
	}

	public void completeTask(int intTask) {
		this.activeAssembly.completeTask(intTask, this.getTaskType());
	}

	public OrderContainer getOrderContainer() {
		throw new UnsupportedOperationException();
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getWorkPostNumber() {
		return this.workPostNum;
	}

	@Override
	public TaskType getWorkPostType() {
		return this.workPostType;
	}
}