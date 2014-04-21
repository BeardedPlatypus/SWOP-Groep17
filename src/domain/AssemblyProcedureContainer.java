package domain;

import java.util.List;

import domain.order.OrderContainer;

/**
 * AssemblyProcedureContainer is a view object that provides an immutable interface
 * for accessing AssemblyProcedures. 
 *
 * @author Martinus Wilhelmus Tegelaers, Thomas Vochten
 */
public interface AssemblyProcedureContainer {
	/**
	 * Get the order fulfilled by this assembly procedure.
	 * 
	 * @return The OrderView of this AssemblyProcedure.
	 */
	public OrderContainer getOrder();
	
	/**
	 * Get the AssemblyTaskContainers of this AssemblyProcedure.
	 * 
	 * @return The AssemblyTaskContainers of this AssemblyProcedure.
	 */
	public List<AssemblyTaskContainer> getAssemblyTasks();
	
	/**
	 * Get the AssemblyTaskContainers of this AssemblyProcedure that match the specified TaskType. 
	 * 
	 * @param taskType
	 * 		The TaskType used to filter the AssemblyTasks. 
	 * 
	 * @return A list of AssemblyTaskContainers that match the specified TaskType
	 */
	public List<AssemblyTaskContainer> getAssemblyTasks(TaskType taskType);
}