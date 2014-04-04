package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for workposts that are part of an assembly line.
 * A workpost has a type, and a name, derived from its type.
 * It also has a number, which depicts it's relative order in the assemblyline.
 */

public class WorkPost implements WorkPostContainer {
	
	/**
	 * The work post's name.
	 */
	private final String name;
	
	/**
	 * The work post's number.
	 */
	private int workPostNum;
	
	/**
	 * The work post's type.
	 */
	private final TaskType workPostType;
	
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
	 * 
	 * @return
	 * 		The type of this workpost
	 */
	public TaskType getTaskType() {
		return this.workPostType;
	}

	/**
	 * Queries the Assembly task of this workpost for tasks matching the workpost's type.
	 * 
	 * @return
	 * 		A list of assembly tasks from the assembly procedure with the same type as this workpost
	 */
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
	 * 
	 * @return
	 * 		the active assembly procedure as a container
	 */
	public AssemblyProcedureContainer getAssemblyProcedureContainer() {
		return this.activeAssembly;
	}
	
	/**
	 * Sets the assembly procedure for this work post to work on
	 * to the given assembly procedure.
	 * 
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
		this.getAssemblyProcedure().completeTask(intTask, this.getTaskType());
	}

	/**
	 * Gets a view of the order encapsulated in this work post's assembly procedure.
	 * Returns null if no assembly is active.
	 * 
	 * @return
	 * 		The order in the active assembly as a container, or null if there is no active assembly
	 */
	public OrderContainer getOrderContainer() {
		if(this.getAssemblyProcedure() != null)
			return this.getAssemblyProcedure().getOrderContainer();
		return null;
	}
	
	/**
	 * Returns the name string of the workpost
	 * 
	 * @return
	 * 		The name of the workpost
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the number of the workpost
	 * 
	 * @return
	 * 		The number of the workpost
	 */
	@Override
	public int getWorkPostNumber() {
		return this.workPostNum;
	}

	/**
	 * Returns the type of the workpost
	 * 
	 * @return
	 * 		The type of the workpost
	 */
	@Override
	public TaskType getWorkPostType() {
		return this.workPostType;
	}

	/**
	 * Checks whether or not all Assembly tasks that can be completed at this workpost on the current procedure
	 * have been completed and returns this result.
	 * 
	 * @return
	 * 		Whether or not all matching tasks of the current procedure are finished by the workpost
	 */
	public boolean allMatchingTasksFinished() {
		List<AssemblyTaskContainer> matchingTasks = getMatchingAssemblyTasks();
		for(AssemblyTaskContainer task : matchingTasks){
			if(!task.isCompleted())
				return false;
		}
		return true;
	}

	/**
	 * Determines whether or not this workpost is working on the given order.
	 * 
	 * @param order
	 * 		The order to check against
	 * @return
	 * 		Whether or not this workpost is working on the given order
	 */
	public boolean isWorkingOnOrder(OrderContainer order) {
			return getAssemblyProcedure() != null && getAssemblyProcedure().getOrder().equals(order);
		 }
	
	/**
	 * Check whether this WorkPost is currently empty. 
	 * 
	 * @return True if empty, false otherwise. 
	 */
	public boolean isEmpty() {
		return this.getAssemblyProcedure() == null;
	}
}