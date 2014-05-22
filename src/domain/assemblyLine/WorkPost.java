package domain.assemblyLine;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import domain.order.Order;
import domain.order.OrderView;

/**
 * A class for WorkPosts that are part of an assembly line.
 * A WorkPost has a type, and a name, derived from its type.
 * It also has a number, which depicts it's relative order in the AssemblyLine.
 */
public class WorkPost implements WorkPostView {
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
		this.minutesOfWork = 0;
		this.observers = new ArrayList<WorkPostObserver>();
		this.activeAssembly = Optional.absent();
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
		return ! this.getAssemblyProcedure().isPresent();
	}
	
	@Override
	public List<AssemblyTaskView> getMatchingAssemblyTasks() {
		if (this.isEmpty()) {
			return new ArrayList<>();
		}
		return this.activeAssembly.get().getAssemblyTasks(this.getTaskType());
	}

	@Override
	public AssemblyProcedureView getAssemblyProcedureView() {
		if (this.isEmpty())
			throw new IllegalStateException();
		
		return this.activeAssembly.get();
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
	 * @throws IllegalArgumentException
	 * 		| assemblyprocedure.absent()
	 */
	void addActiveAssemblyProcedure(Optional<AssemblyProcedure> assemblyProcedure) {
		if (!this.isEmpty())
			throw new IllegalStateException("This workpost is not empty.");
		if (assemblyProcedure == null)
			throw new IllegalArgumentException("AssemblyProcedure may not be null");
		if (! assemblyProcedure.isPresent()) {
			throw new IllegalArgumentException("AssemblyProcedure may not be null");
		}
		
		this.setAssemblyProcedure(assemblyProcedure);
	}
	
	/**
	 * Set the AssemblyProcedure on this WorkPost to the specified AssemblyProcedure.
	 * Additionally, resets the minute counter for this WorkPost.
	 * 
	 * @param assemblyProcedure
	 * 		The AssemblyProcedure to be added to this WorkPost
	 * 
	 * @postcondition | (new this).getAssemblyProcedure() == assemblyProcedure
	 */
	protected void setAssemblyProcedure(Optional<AssemblyProcedure> assemblyProcedure) {
		if (assemblyProcedure == null) {
			this.activeAssembly = Optional.absent();
		}
		else {
			this.activeAssembly = assemblyProcedure;
		}
		this.setMinutesOfWork(0);
	}
	
	/**
	 * Get the AssemblyProcedure of this WorkPost. 
	 * 
	 * @return the AssemblyProcedure of this WorkPost, null if it does not exist.
	 */
	protected Optional<AssemblyProcedure> getAssemblyProcedure(){
		return activeAssembly;
	}
	
	
	/**
	 * Get the amount of minutes that the specified AssemblyProcedure is expected
	 * to spend at this WorkPost
	 * 
	 * @param procedure
	 * 		The procedure of interest
	 * @return The amount of minutes
	 * @throws IllegalArgumentException
	 * 		procedure is null
	 */
	public int getTimeOnWorkPost(AssemblyProcedure procedure) throws IllegalArgumentException {
		if (procedure == null) {
			throw new IllegalArgumentException("procedure is null");
		}
		if (procedure.getAssemblyTasks(this.getTaskType()).isEmpty()) {
			return 0;
		}
		return procedure.getExpectedMinutes();
	}
	
	/**
	 * Add the specified amount of minutes to this WorkPost's AssemblyProcedure's
	 * elapsed amount of minutes
	 * 
	 * @param minutes
	 * 		The elapsed amount of minutes to add
	 * @throws IllegalArgumentException
	 * 		See {@link AssemblyProcedure#addToElapsedMinutes(int) addToElapsedMinutes(int minutes)}
	 */
	public void addToElapsedMinutes(int minutes) throws IllegalArgumentException {
		if (! this.isEmpty()) {
			this.getAssemblyProcedure().get().addToElapsedMinutes(minutes);	
		}
	}
	
	/** The assembly procedure the work post is currently working on. */
	private Optional<AssemblyProcedure> activeAssembly;
	
	//--------------------------------------------------------------------------
	// Order-related methods
	//--------------------------------------------------------------------------
	/**
	 * Get the OrderContainer of this WorkPost's AssemblyProcedure
	 * 
	 * @return The OrderContainer
	 */
	public OrderView getOrderView() {
		return this.getAssemblyProcedure().get().getOrder();
	}
	
	/**
	 * Check whether or not this workpost contains given order at the moment
	 * 
	 * @param order
	 * 		Given order to check for
	 * @return whether or not this workpost contains given order
	 */
	public boolean contains(OrderView order) {
		if (! this.getAssemblyProcedure().isPresent()) {
			return false;
		}
		return this.getAssemblyProcedure().get().contains(order);
	}

	/**
	 * Get the estimated time given order will spend on this workpost
	 * 
	 * @param order
	 * 		Given order
	 * @return
	 * 		estimated time given order will spend on this workpost
	 */
	public int getExpectedTimeOnPost(Order order) {
		return order.getMinutesOnPostOfType(this.getTaskType());
	}
	
	/**
	 * Get the Order of this WorkPost if it exists.
	 *   
	 * @return The Order of this WorkPost if it exists.
	 */
	Optional<Order> getOrder() {
		if (this.getAssemblyProcedure().isPresent()) {
			return Optional.of(this.getAssemblyProcedure().get().getOrder());
		} else {
			return Optional.<Order> absent();
		}
	}
	//--------------------------------------------------------------------------
	// State management
	//--------------------------------------------------------------------------
	/**
	 * Takes the other WorkPost's AssemblyProcedure and leaves the other WorkPost
	 * with an empty AssemblyProcedure.
	 * 
	 * @param other
	 * 		WorkPost whose AssemblyProcedure will be taken
	 * @throws IllegalArgumentException
	 * 		This WorkPost does not follow other
	 */
	public void takeAssemblyProcedureFrom(WorkPost other) throws IllegalArgumentException {
		if (! this.follows(other)) {
			throw new IllegalArgumentException("WorkPost must follow the other WorkPost");
		}
		this.setAssemblyProcedure(other.getAssemblyProcedure());
		other.setAssemblyProcedure(null);
		this.setMinutesOfWork(0);
	}
	
	@Override
	public boolean isFinished() throws IllegalStateException {
		if (this.isEmpty()) {
			throw new IllegalStateException("The WorkPost has no AssemblyProcedure");
		}
		return this.getAssemblyProcedure().get().isFinished(this.getTaskType());
	}
	
	/**
	 * Indicate whether this WorkPost follows the specified WorkPost
	 * 
	 * @param other
	 * 		The other WorkPost
	 * @return This WorkPost follows other
	 */
	private boolean follows(WorkPost other) {
		return this.getWorkPostNum() - other.getWorkPostNum() == 1;
	}

	//--------------------------------------------------------------------------
	// Methods related to completing a task.
	//--------------------------------------------------------------------------
	/**
	 * Complete the task in this WorkPost's AssemblyProcedure with the specified
	 * task number and increments the time this WorkPost has been working on
	 * the AssemblyProcedure with the specified amount of minutes. If this
	 * WorkPost has already finished its work or the specified
	 * task has already been completed, this method has no effect.
	 * @param taskNum
	 * 		The number of the task to be completed
	 * @param minutes
	 * 		Amount of minutes to increment the time spent working with
	 * @throws IllegalArgumentException
	 * 		taskNum refers to a task that does not exist, or taskNum
	 * 		refers to a task that is of a differing type from this WorkPost.
	 * @throws IllegalArgumentException
	 * 		minutes is negative
	 * @throws IllegalStateException
	 * 		The WorkPost has no active AssemblyProcedure.
	 */
	public void completeTask(int taskNum, int minutes) throws IllegalArgumentException,
													IllegalStateException {
		if (this.isEmpty()) {
			throw new IllegalStateException("Cannot complete a task if"
					+ "WorkPost is empty");
		}
		if (minutes < 0) {
			throw new IllegalArgumentException("Cannot complete a task in a"
					+ "negative amount of time");
		}
		if (this.isFinished() || this.getAssemblyProcedure().get().taskIsFinished(taskNum)) {
			return;
		}
		try {
			this.getAssemblyProcedure().get().completeTask(taskNum, this.getTaskType());
			this.incrementTime(minutes);
			if (this.isFinished()) {
				this.notifyWorkComplete();
			}
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
	
	/** The running total of minutes this WorkPost has been working
	 * on the current AssemblyProcedure */
	private int minutesOfWork;
	
	/**
	 * Get the amount of minutes that this WorkPost has been working on
	 * the current AssemblyProcedure.
	 * 
	 * @return The amount of minutes
	 */
	private int getMinutesOfWork() {
		return this.minutesOfWork;
	}
	
	/**
	 * Set the amount of minutes that this WorkPost has been working on
	 * the current AssemblyProcedure to the specified amount of minutes.
	 * 
	 * @param minutes
	 * 		Amount of minutes that the running amount of minutes will be set to
	 */
	private void setMinutesOfWork(int minutes) {
		this.minutesOfWork = minutes;
	}

	/**
	 * Increment the amount of minutes that this WorkPost has been working on
	 * the current AssemblyProcedure with the specified amount of minutes.
	 * 
	 * @param minutes
	 * 		Amount of minutes to increment the running amount of minutes with
	 */
	private void incrementTime(int minutes) {
		this.setMinutesOfWork(this.getMinutesOfWork() + minutes);
	}	
	
	//--------------------------------------------------------------------------
	// Observer-related variables and methods
	//--------------------------------------------------------------------------
	private List<WorkPostObserver> observers;
	
	/**
	 * Get this WorkPost's WorkPostObserver objects.
	 * 
	 * @return
	 * 		The WorkPostObservers
	 */
	private List<WorkPostObserver> getObservers() {
		return this.observers;
	}
	
	/**
	 * Register the specified WorkPostObserver with this WorkPost.
	 * 
	 * @param observer
	 * 		The observer to be registered
	 */
	public void register(WorkPostObserver observer) {
		this.getObservers().add(observer);
	}
	
	/**
	 * Indicate to all observers that this WorkPost has finished its work.
	 */
	private void notifyWorkComplete() {
		for (WorkPostObserver observer : this.getObservers()) {
			observer.notifyWorkComplete(this.getMinutesOfWork());
		}
	}

}
