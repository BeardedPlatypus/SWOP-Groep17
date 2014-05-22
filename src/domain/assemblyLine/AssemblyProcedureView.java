package domain.assemblyLine;

import java.util.List;

import domain.order.OrderView;

/**
 * AssemblyProcedureView is a view object that provides an immutable interface
 * for accessing AssemblyProcedures. 
 *
 * @author Martinus Wilhelmus Tegelaers, Thomas Vochten
 */
public interface AssemblyProcedureView {
	/**
	 * Get the order fulfilled by this assembly procedure.
	 * 
	 * @return The OrderView of this AssemblyProcedure.
	 */
	public OrderView getOrderView();
	
	/**
	 * Get the AssemblyTaskViews of this AssemblyProcedure.
	 * 
	 * @return The AssemblyTaskViews of this AssemblyProcedure.
	 */
	public List<AssemblyTaskView> getAssemblyTasks();
	
	/**
	 * Get the AssemblyTaskContainers of this AssemblyProcedure that match the specified TaskType. 
	 * 
	 * @param taskType
	 * 		The TaskType used to filter the AssemblyTasks. 
	 * 
	 * @return A list of AssemblyTaskContainers that match the specified TaskType
	 */
	public List<AssemblyTaskView> getAssemblyTasks(TaskType taskType);
}