package domain.assemblyLine;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Specification;
import domain.order.Order;
import domain.order.OrderView;
import domain.statistics.ProcedureStatistics;
import domain.statistics.StatisticsLogger;

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
	/**
	 * Instantiate a new AssemblyLine with the specified {@link Manufacturer}.
	 * Also links the manufacturer to itself.
	 * 
	 * @param workPosts
	 * 		The WorkPosts managed by the new AssemblyLine
	 * @param orderSelector
	 * 		The OrderSelector of the new AssemblyLine
	 * @param schedulerIntermediate
	 * 		Source of new Orders
	 * @throws IllegalArgumentException
	 * 		workPosts == null || workPosts.isEmpty()
	 * @throws IllegalArgumentException
	 * 		schedulerIntermediate == null
	 * @throws IllegalArgumentException
	 * 		orderSelector == null
	 */
	public AssemblyLine(List<WorkPost> workPosts, OrderAcceptanceChecker orderSelector,
			SchedulerIntermediate schedulerIntermediate)
		throws IllegalArgumentException {
		if (workPosts == null || workPosts.isEmpty()) {
			throw new IllegalArgumentException("Cannot initialise an AssemblyLine"
					+ "without any WorkPosts");
		}
		if (orderSelector == null) {
			throw new IllegalArgumentException("Cannot initialise an AssemblyLine"
					+ "with null order selector");
		}
		if (schedulerIntermediate == null) {
			throw new IllegalArgumentException("Cannot initialise an AssemblyLine"
					+ "with null SchedulerIntermediate");
		}
		
		this.workPosts = workPosts;
		for (WorkPost workPost : workPosts) {
			workPost.register(this);
		}
		
		this.orderSelector = orderSelector;
		this.elapsedTime = new DateTime(0, 0, 0);
		this.schedulerIntermediate = schedulerIntermediate;
		schedulerIntermediate.setAssemblyLine(this);
		this.initialiseState();
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
	private Manufacturer manufacturer;
	
	/**
	 * Set this AssemblyLine's Manufacturer to the specified manufacturer
	 * 
	 * @param manufacturer
	 * 		The new Manufacturer
	 * @throws IllegalArgumentException
	 * 		manufacturer is null
	 */
	public void setManufacturer(Manufacturer manufacturer) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException("Cannot set null Manufacturer"
					+ "in AssemblyLine");
		}
		this.manufacturer = manufacturer;
	}
	
	//--------------------------------------------------------------------------
	// Order-related methods
	//--------------------------------------------------------------------------
	/**
	 * Indicate whether the given Order has been put on this AssemblyLine
	 * 
	 * @param order
	 * 		The order to look for
	 * @return This AssemblyLine has the given Order
	 */
	public boolean contains(OrderView order) {
		for (WorkPost workPost : this.getWorkPosts()) {
			if (workPost.contains(order)) {
				return true;
			}
		}
		return false;
	}

	//--------------------------------------------------------------------------
	// WorkPost-related methods and variables.
	//--------------------------------------------------------------------------
	/** The {@link WorkPost}s of this assembly line, ordered by the assembly line's layout */
	private final List<WorkPost> workPosts;
	
	/**
	 * Ask the given {@link WorkPost} to complete {@link AssemblyTask} with given number on its current {@link AssemblyProcedure}
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
		this.getCurrentState().completeWorkpostTask(workPostNumber, taskNumber, minutes);
	}
	
	//--------------------------------------------------------------------------
	// OrderContainer methods.

	/**
	 * Get a list of pending {@link OrderView}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	public List<OrderView> getActiveOrderContainers() {
		ArrayList<OrderView> activeOrders = new ArrayList<>();
		
		for (WorkPost post : this.getWorkPosts()) {
			if (!post.isEmpty()) {
				activeOrders.add(post.getOrderView());
			}
		}
		return activeOrders;
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
	public List<AssemblyTaskView> getAssemblyTasksAtPost(int workPostNum)
		throws IllegalArgumentException {
		if (! this.isValidWorkPostNum(workPostNum)) {
			throw new IllegalArgumentException("workPostNum refers to an"
					+ "invalid WorkPost");
		}
		return this.getWorkPost(workPostNum).getMatchingAssemblyTasks();
	}
	
	/**
	 * Get a list of the AssemblyProcedures that are on this AssemblyLine's
	 * WorkPosts. It is padded with nulls for those WorkPosts that are not working
	 * on an AssemblyProcedure
	 * 
	 * @return
	 */
	public List<Optional<AssemblyProcedure>> getAssemblyProcedures() {
		List<Optional<AssemblyProcedure>> toReturn = new ArrayList<Optional<AssemblyProcedure>>();
		for (WorkPost workPost : this.getWorkPosts()) {
			toReturn.add(workPost.getAssemblyProcedure());
		}
		return toReturn;
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
	 * Get the WorkPosts composing the assembly line, as immutable {@link WorkPostView}s
	 * 
	 * @return A list of immutable containers for all respective WorkPosts in their order.
	 */
	public List<WorkPostView> getWorkPostContainers() {
		return new ArrayList<WorkPostView>(workPosts);
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
	 * Get the first WorkPost of this AssemblyLine.
	 * 
	 * @return The first WorkPost
	 */
	WorkPost getFirstWorkPost() {
		return this.getWorkPosts().get(0);
	}
	
	/**
	 * Get the last WorkPost of this AssemblyLine.
	 * 
	 * @return The last WorkPost
	 */
	WorkPost getLastWorkPost() {
		return this.getWorkPosts().get(this.getWorkPosts().size() - 1);
	}

	/**
	 * Get the {@link WorkPost}s of this AssemblyLine.
	 * 
	 * @return The WorkPosts of this AssLine
	 */
	List<WorkPost> getWorkPosts() {
		return new ArrayList<WorkPost>(this.workPosts);
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
	 * Calculate the time the specified AssemblyProcedure would spend on the specified
	 * 
	 * @param procedure
	 * @param workPost
	 * @throws IllegalArgumentException
	 * 		workPostNum is not a valid WorkPost number
	 * @return
	 */
	public int getTimeOnWorkPost(AssemblyProcedure procedure, int workPostNum)
		throws IllegalArgumentException{
		if (! this.isValidWorkPostNum(workPostNum)) {
			throw new IllegalArgumentException("workPostNum is not valid");
		}
		if (procedure == null) {
			return 0;
		}
		return this.getWorkPost(workPostNum).getTimeOnWorkPost(procedure);
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
	DateTime getElapsedTime() throws IllegalStateException{
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
	}
	
	/**
	 * Reset the finished assembly counter
	 */
	private void resetFinishedAssemblyCounter() {
		this.finishedAssemblyCounter = 0;
		for (WorkPost workPost : this.getWorkPosts()) {
			if (! workPost.isEmpty() && workPost.isFinished()) {
				this.incrementFinishedAssemblyCounter();
			}
		}
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
	 * work.
	 */
	public boolean canAdvance() {
		return this.finishedAssemblyCounter >= this.getNbOfActiveWorkPosts();
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
		this.getCurrentState().advanceAssemblyLine();
		this.getManufacturer().incrementTime(elapsedTime);
		this.setElapsedTime(new DateTime(0, 0, 0));
	}
	
	/**
	 * Explicitly tell this AssemblyLine to advance.
	 * 
	 * @throws IllegalStateException
	 * 		canAdvance() == false
	 */
	public void advance() throws IllegalStateException {
		if (! this.canAdvance()) {
			throw new IllegalStateException("Cannot advance AssemblyLine");
		}
		this.tryAdvance(this.getElapsedTime());
		this.resetFinishedAssemblyCounter();
	}
	
	/** Source of new Orders. */
	private SchedulerIntermediate schedulerIntermediate;
	
	/**
	 * @return the SchedulerIntermediate
	 */
	private SchedulerIntermediate getSchedulerIntermediate() {
		return this.schedulerIntermediate;
	}
	
	/**
	 * Set the SchedulerIntermediate to the specified SchedulerIntermediate
	 * 
	 * @param schedulerIntermediate
	 * 		The new SchedulerIntermediate
	 * @throws IllegalArgumentException
	 * 		schedulerIntermediate is null
	 */
	public void setSchedulerIntermediate(SchedulerIntermediate schedulerIntermediate)
		throws IllegalArgumentException {
		if (schedulerIntermediate == null) {
			throw new IllegalArgumentException("Cannot set null SchedulerIntermediate"
					+ "in AssemblyLine");
		}
		this.schedulerIntermediate = schedulerIntermediate;
	}
	
	/**
	 * Ask the SchedulerIntermediate for the next Order
	 * 
	 * @return The next Order
	 */
	Optional<Order> popNextOrderFromSchedule() {
		//TODO ask the AssemblyLineController
		return this.getSchedulerIntermediate().popNextOrderFromSchedule();
	}
	
	/**
	 * Call this method when advancing to handle the AssemblyProcedure that
	 * is rolling off this AssemblyLine, if any. Concretely, submit the
	 * AssemblyProcedure's Order to the Manufacturer as a completed Order, record
	 * statistical information and give that information to the StatisticsLogger.
	 */
	void handleFinishedAssemblyProcedure(Optional<AssemblyProcedure> finishedProcedure) {
		if (finishedProcedure == null || ! finishedProcedure.isPresent()) {
			return;
		}
		
		ProcedureStatistics stats = finishedProcedure.get().makeStatisticsEvent();
		this.addStatistics(stats);
		
		this.getManufacturer().addToCompleteOrders(finishedProcedure.get().getOrder());
	}
	
	//--------------------------------------------------------------------------
	// State management
	//--------------------------------------------------------------------------
	/** The current state of this AssemblyLine */
	private AssemblyLineState state;
	
	/**
	 * Get the current state of this AssemblyLine.
	 * 
	 * @return The current state
	 */
	AssemblyLineState getCurrentState() {
		return this.state;
	}
	
	/**
	 * Set the current state of this AssemblyLine to the specified state.
	 * 
	 * @param state
	 * 		The new state
	 */
	void setCurrentState(AssemblyLineState state) {
		this.state = state;
		state.setAssemblyLine(this);
		state.finaliseSetState();
	}
	
	private void initialiseState() {
		AssemblyLineState initialState = new OperationalState();
		initialState.setAssemblyLine(this);
		initialState.setState(initialState);
	}

	//--------------------------------------------------------------------------
	// AssemblyProcedure Factory Methods. 
	//--------------------------------------------------------------------------
	/**
	 * Make an AssemblyProcedure out of the specified Order. The AssemblyProcedure
	 * is built out of tasks 
	 * 
	 * @param order
	 * 		Order to make AssemblyProcedure from
	 * @return A new AssemblyProcedure compatible with this AssemblyLine
	 * @throws IllegalArgumentException
	 * 		order == null || ! order.isPresent()
	 */
	public AssemblyProcedure makeAssemblyProcedure(Optional<Order> order) throws IllegalArgumentException {
		if (order == null || ! order.isPresent()) {
			throw new IllegalArgumentException("Cannot make assembly procedure"
					+ "from null order");
		}
		List<AssemblyTask> tasks = this.generateTasksFrom(order.get());
		int expectedMinutes = this.calculateExpectedTimeOnLine(order.get());
		return new AssemblyProcedure(order.get(), tasks, expectedMinutes);
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
	protected List<AssemblyTask> generateTasksFrom(OrderView order) {
		Specification orderSpecs = order.getSpecifications();
		
		List<AssemblyTask> toReturn = new ArrayList<AssemblyTask>();
		
		for (int i = 0; i < orderSpecs.getOptions().size(); i++) {
			toReturn.add(new AssemblyTask(orderSpecs.getOptions().get(i), i));
		}
		
		return toReturn;
	}
	
	//--------------------------------------------------------------------------
	// OrderSelector variables and methods
	//--------------------------------------------------------------------------
	/** Determines which Orders this AssemblyLine can handle */
	private final OrderAcceptanceChecker orderSelector;
	
	/**
	 * Get this AssemblyLine's OrderSelector,
	 * which determines which Orders this AssemblyLine can handle
	 * 
	 * @return The order selector.
	 */
	public OrderAcceptanceChecker getOrderSelector() {
		return this.orderSelector;
	}
	
	/**
	 * Calculate the time in minutes that the order is expected to spend
	 * on this AssemblyLine
	 * 
	 * @param order
	 * @return The expected amount of minutes
	 */
	private int calculateExpectedTimeOnLine(Order order) {
		int total = 0;
		for(WorkPost p : this.getWorkPosts()){
			total += p.getExpectedTimeOnPost(order);
		}
		return total;
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
