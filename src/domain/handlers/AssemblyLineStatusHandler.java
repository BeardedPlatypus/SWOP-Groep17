package domain.handlers;

import java.util.List;

import domain.Manufacturer;
import domain.assemblyLine.AssemblyTaskContainer;
import domain.assemblyLine.WorkPostContainer;

/**
 * Coordinates with the outside world in order to view the assembly line status.
 * 
 * @author Simon Slangen
 */
public class AssemblyLineStatusHandler {
	
	private final Manufacturer manufacturer;
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------	
	/**
	 * Instantiate a new AssemblyLineStatusHandler with the specified {@link Manufacturer}.
	 * 
	 * @param 	manufacturer
	 * 			manufacturer that has the assembly line.
	 * @throws 	IllegalArgumentException
	 * 			manufacturer == null
	 */
	public AssemblyLineStatusHandler(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException();
		}
		
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Returns the manufacturer.
	 * 
	 * @return	The manufacturer.
	 */
	private Manufacturer getManufacturer() {
		return manufacturer;
	}
	
	/**
	 * Returns a list of work post containers, containing information about the assembly line status.
	 * 
	 * @return List of work post containers.
	 */
	public List<WorkPostContainer> getWorkPosts() {
		return getManufacturer().getWorkPostContainers();
	}
	
	/**
	 * Returns the amount of different work posts on the assembly line.
	 * 
	 * @return Amount of work posts.
	 */
	public int getAmountOfWorkPosts() {
		return getWorkPosts().size();
	}
	
	/**
	 * Checks if the given index is a valid work post number.
	 * 
	 * @param 	workPostNumber
	 * 			The work post index to verify.
	 * @return	true if larger than zero and smaller than the number of workposts
	 * 			false otherwise
	 */
	private boolean isValidWorkPostNumber(int workPostNumber){
		return workPostNumber >= 0 && workPostNumber < getAmountOfWorkPosts();
	}
	
	/**
	 * Retrieves a single work post from the list of work posts using the given index.
	 * 
	 * @param 	workPostNumber
	 * 			The index of the work post.
	 * @return	The work post situated at the given index.
	 * @throws	IllegalArgumentException
	 * 			| workPostNumber >= 0 && workPostNumber < getAmountOfWorkPosts()
	 */
	public WorkPostContainer getWorkPost(int workPostNumber) throws IllegalArgumentException {
		if(!isValidWorkPostNumber(workPostNumber)){
			throw new IllegalArgumentException("The passed index is invalid. Needs to be in between 0 and "
						+ (getAmountOfWorkPosts()-1) + ". " + workPostNumber + " was passed.");
		} else {
			return getWorkPosts().get(workPostNumber);
		}
	}
	
	/**
	 * Retrieves a list of tasks at the work post identified by the given index.
	 * 
	 * @param 	workPostNumber
	 * 			The index of the work post.
	 * @return	List of tasks at that work post.
	 * @throws	IllegalArgumentException
	 * 			| workPostNumber >= 0 && workPostNumber < getAmountOfWorkPosts()
	 */
	public List<AssemblyTaskContainer> getTasksAtWorkPost(int workPostNumber) throws IllegalArgumentException {
		if(!isValidWorkPostNumber(workPostNumber)){
			throw new IllegalArgumentException("The passed index is invalid. Needs to be in between 0 and "
						+ (getAmountOfWorkPosts()-1) + ". " + workPostNumber + " was passed.");
		} else {
			return getWorkPost(workPostNumber).getMatchingAssemblyTasks();
		}
	}
	
	/**
	 * Returns the amount of different tasks at the work post identified by the given index.
	 * 
	 * @param 	workPostNumber
	 * 			The index of the work post.
	 * @return	Amount of tasks at that work post.
	 * @throws	IllegalArgumentException
	 * 			If the given index does not satisfy the preconditions.
	 */
	public int getAmountOfTasksAtWorkPost(int workPostNumber) throws IllegalArgumentException {
		if(!isValidWorkPostNumber(workPostNumber)){
			throw new IllegalArgumentException("The passed index is invalid. Needs to be in between 0 and "
						+ (getAmountOfWorkPosts()-1) + ". " + workPostNumber + " was passed.");
		} else {
			return getTasksAtWorkPost(workPostNumber).size();
		}
	}
	
	
}
