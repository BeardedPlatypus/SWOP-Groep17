package domain;

import java.util.List;

/** 
 * The WorkPostContainer provides an interface that allows external programs 
 * to access the information of a WorkPost in the domain, without accidental 
 * (and uncontrolled) state changes. 
 * It provides methods for accessing its name, workpostnumber, TaskType, and 
 * AssemblyProcedure.
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public interface WorkPostContainer {
	/**
	 * Get the name of this WorkPostContainer.
	 * 
	 * @return The name of this WorkPostContainer.
	 */
	public String getName();
	
	/**
	 * Get the number of this WorkPostContainer.
	 * 
	 * @return The number of this WorkPostContainer.
	 */
	public int getWorkPostNum();
	
	/**
	 * Get the TaskType of this WorkPost.
	 * 
	 * @return The TaskType of this WorkPost.
	 */
	public TaskType getTaskType();
	
	/**
	 * Get the tasks from the active AssemblyProcedure that match this WorkPost TaskType.
	 * 
	 * @return A list of assembly tasks from the assembly procedure with the same type as this WorkPost
	 * 
	 * @throws IllegalStateException
	 * 		| this.isEmpty()
	 */
	public List<AssemblyTaskContainer> getMatchingAssemblyTasks();
	
	/**
	 * Check if this WorkPost is currently empty (has no active AssemblyProcedure).
	 * 
	 * @return True if no AssemblyProcedure, False otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Get the active AssemblyProcedureContainer of this WorkPost. 
	 * 
	 * @return The active AssemblyProcedureContainer of this WorkPost. 
	 */
	public AssemblyProcedureContainer getAssemblyProcedureContainer();
}