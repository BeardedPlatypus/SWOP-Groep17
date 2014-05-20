package domain.assemblyLine;

import java.util.List;

import com.google.common.base.Optional;

/** 
 * The WorkPostView provides an interface that allows external programs 
 * to access the information of a WorkPost in the domain, without accidental 
 * (and uncontrolled) state changes. 
 * It provides methods for accessing its name, workpostnumber, TaskType, and 
 * AssemblyProcedure.
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public interface WorkPostView {
	/**
	 * Get the name of this WorkPostView.
	 * 
	 * @return The name of this WorkPostView.
	 */
	public String getName();
	
	/**
	 * Get the number of this WorkPostView.
	 * 
	 * @return The number of this WorkPostView.
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
	public List<AssemblyTaskView> getMatchingAssemblyTasks() throws IllegalStateException;
	
	/**
	 * Check if this WorkPost is currently empty (has no active AssemblyProcedure).
	 * 
	 * @return True if no AssemblyProcedure, False otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Indicate whether this WorkPost has finished its work.
	 * @return Whether this WorkPost has finished its work.
	 * @throws This WorkPostView has no active AssemblyProcedure
	 */
	public boolean isFinished() throws IllegalStateException;
	
	/**
	 * Get the active AssemblyProcedureView of this WorkPost. 
	 * 
	 * @return The active AssemblyProcedureView of this WorkPost.
	 * 
	 * @throws IllegalStateException
	 * 		| this.isEmpty()
	 */
	public AssemblyProcedureView getAssemblyProcedureView() throws IllegalStateException;
}