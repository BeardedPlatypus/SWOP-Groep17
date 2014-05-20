package domain.handlers;

import java.util.List;

import domain.Manufacturer;
import domain.assemblyLine.AssemblyTaskView;
import domain.assemblyLine.WorkPostView;

/**
 * Coordinates with the outside world in order to complete assembly tasks of a single WorkPost.
 */
public class PerformAssemblyTaskHandler {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Instantiate a new PerformAssemblyTaskHandler with the specified Manufacturer.
	 * 
	 * @param manufacturer
	 * 		The assembly line that the new handler will operate on.
	 * 
	 * @throws IllegalArgumentException
	 * 		| Manufacturer == null.
	 */
	public PerformAssemblyTaskHandler(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException("Cannot initialise perform assembly task handler with non-existent manufacturer.");
		}
		
		this.manufacturer = manufacturer;
	}
	
	//--------------------------------------------------------------------------
	// Attributes 
	//--------------------------------------------------------------------------
	/**
	 * Get the Manufacturer of this PerformAssemblyTaskHandler.
	 * 
	 * @return The Manufacturer of this PerformAssemblyTaskHandler.
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The Manufacturer that is used by this PerformAssemblyTaskHandler. */
	private final Manufacturer manufacturer;

	/**
	 * Get the WorkPostContainrs AssemblyLine's WorkPosts.
	 * 
	 * @return the WorkPostContainers of the ASsemblyLine's WorkPosts
	 */
	public List<WorkPostView> getWorkPosts() {
		return this.getManufacturer().getWorkPostContainers();
	}

	/**
	 * Get the AssemblyTaskContainers of the specified work post.
	 * 
	 * @param workPostNumber
	 * 		The work post of interest.
	 * 
	 * @return The AssemblyTaskContainers at the specified work post.
	 * 
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a work post that does not exist.
	 */
	public List<AssemblyTaskView> getAssemblyTasksAtWorkPost(int workPostNumber) throws IllegalArgumentException {
		return this.getManufacturer().getAssemblyTasksAtPost(workPostNumber);
	}

	/**
	 * Perform the specified taskNumber at the specified WorkPost.
	 * 
	 * @param workPostNumber
	 * 		The number of the work post.
	 * @param taskNumber
	 * 		The number of the task.
	 * @param minutes
	 * 		The amount of minutes it took to complete the task.
	 * 
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a work post that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task with a type incompatible with the given
	 * 		work post.
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException {
		this.getManufacturer().completeWorkpostTask(workPostNumber, taskNumber, minutes);
	}
}
