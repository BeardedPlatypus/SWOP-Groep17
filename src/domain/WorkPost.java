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
	 * Initialise this new WorkPost with the specified TaskType and number. 
	 * 
	 * @param workPostType
	 * 		The type of this new work post.
	 * 
	 * @post | (new this).getTaskType() == workPostType
	 * @post | (new this).getPostNum() == workPostNum
	 * @post | (new this).isEmpty()
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
		if (this.isEmpty())
			throw new IllegalStateException();
		
		return this.activeAssembly;
	}
	
	/**
	 * Add the specified AssemblyProcedure as new active AssemblyProcedure to 
	 * this WorkPost. 
	 * 
	 * @param assemblyProcedure
	 * 		The new active AssemblyProcedure of this WorkPost.
	 * 
	 * @postcondition | (new this).getAssemblyProcedure() == assemblyProcedure
	 * @postcondition | !(new this).isEmpty()
	 * 
	 * @throws IllegalStateException
	 * 		| !this.isEmpty()
	 * @throws IllegalArgumentException
	 * 		| assemblyProcedure == null
	 */
	void addActiveAssemblyProcedure(AssemblyProcedure assemblyProcedure) {
		if (!this.isEmpty())
			throw new IllegalStateException("This workpost is not empty.");
		if (assemblyProcedure == null)
			throw new IllegalArgumentException("AssemblyProcedure may not be null");
		
		this.setAssemblyProcedure(assemblyProcedure);
	}
	
	/**
	 * Set the AssemblyProcedure on this WorkPost to the specified AssemblyProcedure.
	 * 
	 * @param assemblyProcedure
	 * 		The AssemblyProcedure to be added to this WorkPost
	 * 
	 * @postcondition | (new this).getAssemblyProcedure() == assemblyProcedure
	 */
	protected void setAssemblyProcedure(AssemblyProcedure assemblyProcedure) {
		this.activeAssembly = assemblyProcedure;
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
