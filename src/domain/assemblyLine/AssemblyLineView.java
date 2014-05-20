package domain.assemblyLine;

import java.util.List;

import domain.order.OrderContainer;

/**
 * A view on an AssemblyLineFacade. Allows the system to pass a facade as an
 * immutable object that can be inspected.
 * 
 * @author Frederik Goovaerts
 */
public interface AssemblyLineView {

	/**
	 * Indicate whether the given Order has been put on this AssemblyLine
	 * 
	 * @param order
	 * 		The order to look for
	 * @return This AssemblyLine has the given Order
	 */
	public boolean contains(OrderContainer order);
	
	/**
	 * Get a list of pending {@link OrderContainer}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	public List<OrderContainer> getActiveOrderContainers();
	
	/**
	 * Get views those AssemblyTasks that are of the specified WorkPost's type.
	 * 
	 * @param workPostNum
	 * 		The WorkPost of interest
	 * @return Views of the AssemblyTasks that are of the specified WorkPost's type
	 * @throws IllegalArgumentException
	 * 		workPostNum refers to a WorkPost that does not exist
	 */
	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNum)
		throws IllegalArgumentException;
	
	/**
	 * Get the WorkPosts composing the assembly line, as immutable {@link WorkPostContainer}s
	 * 
	 * @return A list of immutable containers for all respective WorkPosts in their order.
	 */
	public List<WorkPostContainer> getWorkPostContainers();
	
	/**
	 * Return if this AssemblyLine is currently empty. 
	 * 
	 * @return True if empty, otherwise false.
	 */
	public boolean isEmpty();
}
