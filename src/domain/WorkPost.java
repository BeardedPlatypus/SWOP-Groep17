package domain;

import java.util.List;

/**
 * A class for workposts that are part of an assembly line.
 * A workpost has a type, and a name, derived from its type.
 * It also has a number, which depicts it's relative order in the assemblyline.
 */
public class WorkPost implements WorkPostContainer {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialise this new WorkPost with the specified TaskTyp and number. 
	 * 
	 * @param workPostType
	 * 		The type of this new work post.
	 * 
	 * @post | (new this).getTaskType() == workPostType
	 * @post | (new this).getPostNum() == workPostNum
	 * 
	 * @throws IllegalArgumentException
	 * 		| workPostType == null
	 */
	public WorkPost(TaskType workPostType, int workPostNum) throws IllegalArgumentException {
		if (workPostType == null) {
			throw new IllegalArgumentException("Cannot initialise a work post with a non-existent work post type.");
		}
		
		this.workPostType = workPostType;
		this.name = workPostType.toString();
		this.workPostNum = workPostNum;
	}

	//--------------------------------------------------------------------------
	// Methods related to WorkPostContainer.
	//--------------------------------------------------------------------------
	@Override
	public String getName() {
		return this.name;
	}
	
	/** The name of this WorkPost. */
	private final String name;

	//--------------------------------------------------------------------------
	@Override
	public int getWorkPostNum() {
		return this.workPostNum;
	}
	
	/** The number of this WorkPost. */
	private int workPostNum;

	//--------------------------------------------------------------------------
	@Override
	public TaskType getTaskType() {
		return this.workPostType;
	}
	
	/** The type of this WorkPost. */
	private final TaskType workPostType;

	//--------------------------------------------------------------------------
	@Override
	public boolean isEmpty() {
		return this.getAssemblyProcedure() == null;
	}
	
	@Override
	public List<AssemblyTaskContainer> getMatchingAssemblyTasks() {
		if (this.isEmpty()) {
			throw new IllegalStateException();
		}
		return this.activeAssembly.getAssemblyTasks(this.getTaskType());
	}

	@Override
	public AssemblyProcedureContainer getAssemblyProcedureContainer() {
		return this.activeAssembly;
	}
	
	/**
	 * Get the AssemblyProcedure of this WorkPost. 
	 * 
	 * @return the ASsemblyProcedure of this WorkPost, null if it does not exist.
	 */
	protected AssemblyProcedure getAssemblyProcedure(){
		return activeAssembly;
	}
	
	/** The assembly procedure the work post is currently working on. */
	private AssemblyProcedure activeAssembly;

	//--------------------------------------------------------------------------
	// Methods related to completing a task.
	//--------------------------------------------------------------------------
	//TODO new functionality
	public void completeTask(Object parameter, Object parameter2) {
		throw new UnsupportedOperationException();
	}

	//TODO new functionality
	public void incrementTime(Object parameter) {
		throw new UnsupportedOperationException();
	}	
}