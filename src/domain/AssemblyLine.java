package domain;

import java.util.ArrayList;
import java.util.List;

import domain.WorkPost;

/**
 * A class depicting an AssemblyLine in the system. An AssemblyLine is composed 
 * of a number of {@link WorkPost}s.
 * It is associated with a {@link Manufacturer} that it uses to communicate with the rest
 * of the system.
 * It provides methods for advancing the assembly line and requesting new {@link Order}s.
 * 
 * @author Thomas Vochten, Frederik Goovaerts, Martinus Wilhelmus Tegelaers
 */
public class AssemblyLine implements WorkPostObserver {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	//TODO: Raw method, add annotation
	//TODO: Add additional throws of exceptions. 	
	/**
	 * Instantiate a new AssemblyLine with the specified {@link Manufacturer}.
	 * 
	 * @param manufacturer
	 * 		manufacturer that owns this AssemblyLine
	 * @throws IllegalArgumentException
	 * 		manufacturer == null
	 */
	public AssemblyLine(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException();
		}
		
		this.manufacturer = manufacturer;
		manufacturer.setAssemblyLine(this);
		
		int workPostNum = 0;
		for (TaskType type : TaskType.values()) {
			WorkPost newPost = new WorkPost(type, workPostNum);
			workPostNum++;
			this.workPosts.add(newPost);
			newPost.register(this);
		}
		
		this.elapsedTime = new DateTime(0, 0, 0);
	}	
	
	//--------------------------------------------------------------------------
	// Manufacturer.
	//--------------------------------------------------------------------------
	/** 
	 * Get the manufacturer that owns this AssemblyLine. 
	 * 
	 * @return this manufacturer that owns this AssemblyLine.
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The manufacturer that owns this AssemblyLIne. */
	private final Manufacturer manufacturer;

	//--------------------------------------------------------------------------
	// WorkPost-related methods and variables.
	//--------------------------------------------------------------------------
	/**
	 * Asks the given {@link WorkPost} to complete {@link AssemblyTask} with given number on its current {@link AssemblyProcedure}
	 * 
	 * @param workPostNumber
	 * 		The number of the {@link WorkPost} we want to address
	 * @param taskNumber
	 * 		The number of the {@link AssemblyTask} that should be marked as completed at given {@link WorkPost}
	 * @throws IllegalArgumentException
	 * 		When the workPostNumber is not a valid index
	 * @throws IllegalArgumentException
	 * 		See {@link WorkPost#completeTask(int, int) completeTask(int, int)}
	 * @throws IllegalStateException
	 * 		See {@link WorkPost#completeTask(int, int) completeTask(int, int)}
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException,
			IllegalStateException{
		if(!this.isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		this.getWorkPost(workPostNumber).completeTask(taskNumber, minutes);
	}
	
	//--------------------------------------------------------------------------
	// OrderContainer methods.

	/**
	 * Get a list of pending {@link OrderContainer}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	public List<OrderContainer> getActiveOrderContainers() {
		ArrayList<OrderContainer> activeOrders = new ArrayList<>();
		
		for (WorkPost post : this.getWorkPosts()) {
			if (!post.isEmpty()) {
				activeOrders.add(post.getOrderContainer());
			}
		}
		return activeOrders;
	}
	
	//--------------------------------------------------------------------------
	// Querying AssemblyProcedure objects
	//--------------------------------------------------------------------------
	/**
	 * Get views of the AssemblyProcedures currently active on each WorkPost
	 * 
	 * @return The views of the AssemblyProcedures
	 */
	public List<AssemblyProcedureContainer> getAssemblyOnEachWorkPost() {
		List<AssemblyProcedureContainer> toReturn = new ArrayList<AssemblyProcedureContainer>();
		for (WorkPost workPost : this.getWorkPosts()) {
			toReturn.add(workPost.getAssemblyProcedureContainer());
		}
		return toReturn;
	}
	
	//--------------------------------------------------------------------------
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
		throws IllegalArgumentException {
		if (! this.isValidWorkPostNum(workPostNum)) {
			throw new IllegalArgumentException("workPostNum refers to an"
					+ "invalid WorkPost");
		}
		return this.getWorkPost(workPostNum).getMatchingAssemblyTasks();
	}
	
	/**
	 * Indicate whether the specified WorkPost number is valid.
	 * 
	 * @param workPostNum
	 * 		The WorkPost of interest
	 * @return workPostNum is valid
	 */
	private boolean isValidWorkPostNum(int workPostNum) {
		return workPostNum >= 0 && workPostNum < this.getAssemblyLineSize();
	}

	//--------------------------------------------------------------------------
	// WorkPost-related variables and methods
	//--------------------------------------------------------------------------
	/**
	 * Get the WorkPosts composing the assembly line, as immutable {@link WorkPostContainer}s
	 * 
	 * @return A list of immutable containers for all respective WorkPosts in their order.
	 */
	public List<WorkPostContainer> getWorkPostContainers() {
		return new ArrayList<WorkPostContainer>(workPosts);
	}

	/** 
	 * Get the size of this AssemblyLine.
	 * 
	 * @return the size of this AssemblyLine.
	 */
	public int getAssemblyLineSize() {
		return this.workPosts.size();
	}
	

	/**
	 * Get the amount of WorkPosts who currently have an AssemblyProcedure, also
	 * counting those with all tasks finished.
	 * 
	 * @return the amount of WorkPosts who currently have an AssemblyProcedure
	 */
	private int getNbOfActiveWorkPosts() {
		int count = 0;
		for(WorkPost p : this.getWorkPosts()) {
			if (!p.isEmpty()) {
				count++;
			}
		}
		return count;
	}
	
	/** The amount of time that has passed since last advancing the AssemblyLine */
	private DateTime elapsedTime;
	
	/**
	 * Get the elapsed time since the last advancement of the AssemblyLine.
	 * 
	 * @return the elapsed time since the last advancement of the AssemblyLine
	 */
	private DateTime getElapsedTime() throws IllegalStateException{
		return this.elapsedTime;
	}
	
	/**
	 * Set the elapsed time since the last advancement of the AssemblyLine
	 * to the specified value.
	 * 
	 * @param elapsedTime
	 * 		The value the elapsed time will be set to.
	 */
	private void setElapsedTime(DateTime elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * Return if this AssemblyLine is currently empty. 
	 * 
	 * @return True if empty, otherwise false.
	 */
	public boolean isEmpty() {
		for(WorkPost p : this.getWorkPosts()) {
			if (!p.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Check if the specified workPostNumber correpsonds to a valid WorkPost 
	 * in this AssemblyLine.
	 * 
	 * @param workPostNumber 
	 * 		The workPostNumber of the workPost to be checked. 
	 * @return workPostNumber corresponds with a valid workPost. 
	 */
	public boolean isValidWorkPost(int workPostNumber) {
		return workPostNumber >= 0 && workPostNumber < this.getAssemblyLineSize(); 
	}
	
	/**
	 * Get the {@link WorkPost} at the specified position in the assembly line.
	 * 
	 * @param workPostNumber
	 * 		The index of the wanted WorkPost. 
	 * 
	 * @return The requested WorkPost
	 * 
	 * @throws IllegalArgumentException 
	 * 		| workPostNumber < 0 || workPostNumber >= this.getAssemblyLineSize() 
	 */
	protected WorkPost getWorkPost(int workPostNumber) throws IllegalArgumentException{
		if(!isValidWorkPost(workPostNumber))
			throw new IllegalArgumentException("Argument is not an existing workpost.");
		return this.getWorkPosts().get(workPostNumber);
	}
	
	/**
	 * Get the {@link WorkPost}s of this AssemblyLine.
	 * 
	 * @return The WorkPosts of this AssLine
	 */
	private List<WorkPost> getWorkPosts() {
		return new ArrayList<WorkPost>(this.workPosts);
	}
		
	/** The {@link WorkPost}s of this assembly line, ordered by the assembly line's layout */
	private final List<WorkPost> workPosts = new ArrayList<WorkPost>();
	
	//--------------------------------------------------------------------------
	// WorkPostObserver logic
	//--------------------------------------------------------------------------
	@Override
	public void notifyWorkComplete(int minutes) {
		DateTime prospectiveElapsedTime = new DateTime(0, 0, minutes);
		if (this.getElapsedTime().compareTo(prospectiveElapsedTime) < 0) {
			this.setElapsedTime(prospectiveElapsedTime);
		}
		this.incrementFinishedAssemblyCounter();
	}

	/**
	 * Increment the assemblyCounter, indicating an additional workPost has
	 * finished the AssemblyTasks it can perform on its current AssemblyProcedure.
	 * Notify the assemblyLine itself of the fact that a workPost has finished,
	 * so it can check if it has to advance the Line.
	 */
	private void incrementFinishedAssemblyCounter() {
		this.finishedAssemblyCounter++;
		this.checkAdvance();
	}
	
	/**
	 * Reset the finished assembly counter
	 */
	private void resetFinishedAssemblyCounter() {
		this.finishedAssemblyCounter = 0;
	}

	/**
	 * Counter that keeps track of how many WorkPosts have finished all their
	 * respective tasks at this stage of production
	 */
	private int finishedAssemblyCounter = 0;
	
	//--------------------------------------------------------------------------
	// AssemblyLine Advancement methods. 
	//--------------------------------------------------------------------------
	
	/**
	 * Check whether all WorkPosts with an AssemblyProcedure have finished their
	 * work, and if this is the case, start advancing the AssemblyLine.
	 */
	private void checkAdvance() {
		if((this.finishedAssemblyCounter >= this.getNbOfActiveWorkPosts()) &&
				!this.isEmpty()){
			DateTime elapsedTime = this.getElapsedTime();
			this.tryAdvance(elapsedTime);
		}
		
	}

	/**
	 * Advance this AssemblyLine by one {@link WorkPost} and update the {@link DateTime}
	 * with the elapsed time since the previous advancement. 
	 * Request a new order from the {@link Manufacturer} for the first WorkPost. 
	 * 
	 * @param elapsedTime
	 * 		The time that has elapsed since the last advancement, in minutes. 
	 * @throws IllegalStateException
	 * 		When there is still a finished assembly ready to be collected before
	 * 		the shifting of the WorkPosts.
	 */
	private void tryAdvance(DateTime elapsedTime) throws IllegalStateException{
		this.shiftWorkPosts();
		this.putNextOrderOnAssemblyLine();
		this.setElapsedTime(new DateTime(0, 0, 0));
		this.resetFinishedAssemblyCounter();
	}

	/**
	 * Advance this AssemblyLine by one WorkPost. All AssemblyProcedures
	 * are shifted forward by one WorkPost. Concerning the last AssemblyProcedure:
	 * its order is retrieved and information about it is reported to the StatisticsLogger
	 * 
	 * @throws IllegalStateException
	 * 		If the completedOrder field is not empty, the line cannot be advanced.
	 */
	private void shiftWorkPosts() throws IllegalStateException{
		AssemblyProcedure finishedProcedure = this.getWorkPosts()
				.get(this.getWorkPosts().size() - 1).getAssemblyProcedure();
		
		this.getWorkPost(this.getAssemblyLineSize() - 1)
			.addToElapsedMinutes((int) this.getElapsedTime().getInMinutes());
		for(int i = this.getAssemblyLineSize() - 1; i > 0 ; i--){
			this.getWorkPost(i - 1).addToElapsedMinutes((int) this.getElapsedTime().getInMinutes());
			this.getWorkPost(i).takeAssemblyProcedureFrom(this.getWorkPost(i - 1));
		}
		
		this.handleFinishedAssemblyProcedure(finishedProcedure);
	}

	//TODO
	/**
	 * Request a new Order from the Manufacturer, convert this to an AssemblyProcedure
	 * and add this AssemblyProcedure to the first WorkPost. 
	 * 
	 * @throws IllegalStateException
	 * 		| !this.getWorkPost(0).isEmpty()
	 */
	private void putNextOrderOnAssemblyLine() throws IllegalStateException{
		Order order = this.getManufacturer().popNextOrderFromSchedule();
		if (order == null) {
			return;
		}
		AssemblyProcedure procedure = this.makeAssemblyProcedure(order);
		this.getWorkPost(0).setAssemblyProcedure(procedure);
	}
	
	/**
	 * Call this method when advancing to handle the AssemblyProcedure that
	 * is rolling off this AssemblyLine, if any. Concretely, submit the
	 * AssemblyProcedure's Order to the Manufacturer as a completed Order, record
	 * statistical information and give that information to the StatisticsLogger.
	 */
	private void handleFinishedAssemblyProcedure(AssemblyProcedure finishedProcedure) {
		if (finishedProcedure == null) {
			return;
		}
		
		ProcedureStatistics stats = finishedProcedure.makeStatisticsEvent();
		this.addStatistics(stats);
		
		this.getManufacturer().addToCompleteOrders(finishedProcedure.getOrder());
	}

	//--------------------------------------------------------------------------
	// AssemblyProcedure Factory Methods. 
	//--------------------------------------------------------------------------
	public AssemblyProcedure makeAssemblyProcedure(Order order) {
		List<AssemblyTask> tasks = this.generateTasksFrom(order);
		int expectedMinutes = this.calculateExpectedTimeOnLine(order);
		return new AssemblyProcedure(order, tasks, expectedMinutes);
	}
	
	/**
	 * Generate a list of AssemblyTasks from the specified order.
	 * 
	 * @param order
	 * 		The order from which the list of AssemblyTasks is created.
	 * 
	 * @precondition
	 * 		order != null
	 *   
	 * @return A list of assembly tasks based on the specified order.
	 * @throws IllegalStateException
	 * 		! order.getModel().isValidSpecification(order.getSpecifications())
	 */
	protected List<AssemblyTask> generateTasksFrom(OrderContainer order) {
		Specification orderSpecs = order.getSpecifications();
		
		List<AssemblyTask> toReturn = new ArrayList<AssemblyTask>();
		
		for (int i = 0; i < orderSpecs.getOptions().size(); i++) {
			toReturn.add(new AssemblyTask(orderSpecs.getOptions().get(i), i));
		}
		
		return toReturn;
	}
	
	/**
	 * Calculate the time in minutes that the order is expected to spend
	 * on this AssemblyLine
	 * 
	 * @param order
	 * @return The expected amount of minutes
	 */
	private int calculateExpectedTimeOnLine(Order order) {
		return order.getMinutesPerPost() * this.getAssemblyLineSize();
	}
	
	//--------------------------------------------------------------------------
	// Statistics logger and related methods
	//--------------------------------------------------------------------------
	/** The statistics logger of this assembly line. */
	private StatisticsLogger statisticsLogger;
	
	/**
	 * Get this AssemblyLine's StatisticsLogger
	 * 
	 * @return The StatisticsLogger
	 */
	private StatisticsLogger getStatisticsLogger() {
		return this.statisticsLogger;
	}
	
	/**
	 * Set this AssemblyLine's StatisticsLogger to the specified StatisticsLogger
	 * 
	 * @param logger
	 * 		The StatisticsLogger of interest
	 * @throws IllegalArgumentException
	 * 		logger is null
	 */
	public void setStatisticsLogger(StatisticsLogger logger) throws IllegalArgumentException {
		if (logger == null) {
			throw new IllegalArgumentException("Cannot set logger to null.");
		}
		this.statisticsLogger = logger;
	}
	
	/**
	 * Get a report on the statistical variables watched by this AssemblyLine
	 * 
	 * @return A report in the form of a String.
	 */
	public String getStatisticsReport() {
		if (this.getStatisticsLogger() == null) {
			return "Not currently recording statistics";
		}
		return this.getStatisticsLogger().getReport();
	}
	
	/**
	 * Pass the specified ProcedureStatistics onto the StatisticsLogger
	 * 
	 * @param stats
	 * 		A new statistical event
	 */
	private void addStatistics(ProcedureStatistics stats) {
		if (this.getStatisticsLogger() == null) {
			return;
		}
		this.getStatisticsLogger().addStatistics(stats);
	}
}
