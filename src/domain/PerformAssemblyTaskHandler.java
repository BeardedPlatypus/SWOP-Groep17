package domain;

import java.util.List;

/**
 * Coordinates with the outside world in order to complete assembly tasks of a single workpost.
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
	Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The Manufacturer that is used by this PerformAssemblyTaskHandler. */
	private final Manufacturer manufacturer;

	//FIXME: old functionality
	/**
	 * Get the WorkPostContainrs AssemblyLine's WorkPosts.
	 * 
	 * @return the WorkPostContainers of the ASsemblyLine's WorkPosts
	 */
	public List<WorkPostContainer> getWorkPosts() {
		throw new UnsupportedOperationException();
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
	public List<AssemblyTaskContainer> getAssemblyTasksAtWorkPost(int workPostNumber) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Perform the specified taskNumber at the specified WorkPost.
	 * 
	 * @param workPostNumber
	 * 		The number of the work post.
	 * @param taskNumber
	 * 		The number of the task.
	 * 
	 * @throws IllegalArgumentException
	 * 		workPostNumber refers to a work post that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task that does not exist.
	 * @throws IllegalArgumentException
	 * 		taskNumber refers to a task with a type incompatible with the given
	 * 		work post.
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}