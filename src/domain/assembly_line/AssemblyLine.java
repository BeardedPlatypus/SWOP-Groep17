package domain.assembly_line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;

import domain.DateTime;
import domain.Manufacturer;
import domain.assembly_line.virtual.VirtualAssemblyLine;
import domain.car.Model;
import domain.car.Specification;
import domain.clock.EventConsumer;
import domain.order.CompletedOrderEvent;
import domain.order.CompletedOrderObserver;
import domain.order.CompletedOrderSubject;
import domain.order.Order;
import domain.order.OrderView;
import domain.statistics.ProcedureStatistics;
import exceptions.OrdersNotEmptyWhenAdvanceException;

/**
 * A class depicting an AssemblyLine in the system. An AssemblyLine is composed 
 * of a number of {@link WorkPost}s.
 * It is associated with a {@link Manufacturer} that it uses to communicate with the rest
 * of the system.
 * It provides methods for advancing the assembly line and requesting new {@link Order}s.
 * 
 * @author Thomas Vochten, Frederik Goovaerts, Martinus Wilhelmus Tegelaers
 */
public class AssemblyLine implements WorkPostObserver, CompletedOrderSubject {
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
	 * 		models is null or models is empty
	 * @throws IllegalArgumentException
	 * 		eventConsumer == null
	 */
	public AssemblyLine(List<WorkPost> workPosts, List<Model> models, EventConsumer eventConsumer)
		throws IllegalArgumentException {
		if (workPosts == null || workPosts.isEmpty()) {
			throw new IllegalArgumentException("Cannot initialise an AssemblyLine"
					+ "without any WorkPosts");
		}
		if (models == null || models.isEmpty()) {
			throw new IllegalArgumentException("Cannot initialise an AssemblyLine"
					+ "with empty model list");
		}
		if (eventConsumer == null) {
			throw new IllegalArgumentException("eventConsumer == null");
		}
//		if (schedulerIntermediate == null) {
//			throw new IllegalArgumentException("Cannot initialise an AssemblyLine"
//					+ "with null SchedulerIntermediate");
//		}
		
		this.workPosts = workPosts;
		for (WorkPost workPost : workPosts) {
			workPost.register(this);
		}
		
		this.acceptedModels = Collections.unmodifiableList(models);
		this.elapsedTime = new DateTime(0, 0, 0);
		
//		this.schedulerIntermediate = schedulerIntermediate;
//		schedulerIntermediate.setAssemblyLine(this);
		
		this.observers = new ArrayList<CompletedOrderObserver>();
		this.initialiseState();
		
		this.eventConsumer = eventConsumer;
	}	
	
	//--------------------------------------------------------------------------
	// AssemblyLineController
	//--------------------------------------------------------------------------
	/**
	 * Get the AssemblyLineController of this AssemblyLineControllerController.
	 * 
	 * @return The AssemblyLineController of AssemblyLine.
	 */
	protected AssemblyLineController getAssemblyLineController() {
		return this.assemblyLineController;
	}
	
	/**
	 * Set the AssemblyLineController of this AssemblyLine to newAssemblyLineController.
	 * 
	 * @param newAssemblyLineController
	 * 		The new AssemblyLine of this AssemblyLine.
	 * 
	 * @postcondition | (new this).getAssemblyLineController == newAssemblyLineController
	 * 
	 * @throws IllegalArgumentException
	 * 		| newAssemblyLineController == null
	 */
	void setAssemblyLine(AssemblyLineController newAssemblyLineController) throws IllegalArgumentException {
		if (newAssemblyLineController == null) {
			throw new IllegalArgumentException("potato cannot be null.");
		}
		this.assemblyLineController = newAssemblyLineController;
	}
	
	/** The AssemblyLine of this AssemblyLineController. */
	private AssemblyLineController assemblyLineController;
	
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
	public List<WorkPostView> getWorkPostViews() {
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
	 * @return The TaskTypes of this AssemblyLine's WorkPosts
	 */
	public List<TaskType> getTaskTypes() {
		List<TaskType> toReturn = new ArrayList<TaskType>();
		for (WorkPost workPost : this.getWorkPosts()) {
			toReturn.add(workPost.getTaskType());
		}
		
		return toReturn;
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
	 * @param orders
	 * 		Orders to put on the AssemblyLine 
	 * @throws IllegalStateException
	 * 		When there is still a finished assembly ready to be collected before
	 * 		the shifting of the WorkPosts.
	 */
	private void tryAdvance(DateTime elapsedTime, List<Order> orders) throws IllegalStateException{
		this.getCurrentState().advanceAssemblyLine(orders);
		this.setElapsedTime(new DateTime(0, 0, 0));
	}
	
	/**
	 * Explicitly tell this AssemblyLine to advance, thereby putting the
	 * specified Orders on the line.
	 * 
	 * @throws IllegalStateException
	 * 		canAdvance() == false
	 * @throws IllegalArgumentException
	 * 		orders is null or contains null
	 * @throws OrdersNotEmptyWhenAdvanceException
	 * 		The list of orders was not empty when advance is finished
	 */
	public void advance(List<Order> orders) throws IllegalStateException,
		IllegalArgumentException, OrdersNotEmptyWhenAdvanceException {
		if (! this.canAdvance()) {
			throw new IllegalStateException("Cannot advance AssemblyLine");
		}
		this.tryAdvance(this.getElapsedTime(), orders);
		if (! orders.isEmpty()) {
			throw new OrdersNotEmptyWhenAdvanceException("Fatal error: "
					+ "list of Orders was not empty after advance was finished");
		}
		this.resetFinishedAssemblyCounter();
	}
	
//	/** Source of new Orders. */
//	private SchedulerIntermediate schedulerIntermediate;
//	
//	/**
//	 * @return the SchedulerIntermediate
//	 */
//	private SchedulerIntermediate getSchedulerIntermediate() {
//		return this.schedulerIntermediate;
//	}
//	
//	/**
//	 * Set the SchedulerIntermediate to the specified SchedulerIntermediate
//	 * 
//	 * @param schedulerIntermediate
//	 * 		The new SchedulerIntermediate
//	 * @throws IllegalArgumentException
//	 * 		schedulerIntermediate is null
//	 */
//	public void setSchedulerIntermediate(SchedulerIntermediate schedulerIntermediate)
//		throws IllegalArgumentException {
//		if (schedulerIntermediate == null) {
//			throw new IllegalArgumentException("Cannot set null SchedulerIntermediate"
//					+ "in AssemblyLine");
//		}
//		this.schedulerIntermediate = schedulerIntermediate;
//	}
	
//	/**
//	 * Ask the SchedulerIntermediate for the next Order
//	 * 
//	 * @return The next Order
//	 */
//	Optional<Order> popNextOrderFromSchedule() {
//		//TODO ask the AssemblyLineController
//		return this.getSchedulerIntermediate().popNextOrderFromSchedule();
//	}
	
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
		CompletedOrderEvent event = new CompletedOrderEvent(finishedProcedure.get().getOrder(),
				stats);
		this.notifyOrderComplete(event);
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
	
	/** The Models that this AssemblyLine can accept */
	private final List<Model> acceptedModels;
	
	/**
	 * @return The Models that this AssemblyLine can accept
	 */
	public List<Model> getAcceptedModels() {
		return this.acceptedModels;
	}
	
	/**
	 * Indicate whether this AssemblyLine can accept Orders with the specified
	 * Model.
	 * 
	 * @param model
	 * 		The Model to check for
	 * @return AssemblyLine can handle Orders with model
	 */
	public boolean hasModel(Model model) {
		return this.getAcceptedModels().contains(model);
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
	// Rolling AssemblyProcedure off the line
	//--------------------------------------------------------------------------
	/** The CompletedOrderObservers */
	private List<CompletedOrderObserver> observers;
	
	/**
	 * @return The CompletedOrderObservers
	 */
	private List<CompletedOrderObserver> getObservers() {
		return this.observers;
	}

	@Override
	public void attachObserver(CompletedOrderObserver observer)
			throws IllegalArgumentException {
		if (observer == null) {
			throw new IllegalArgumentException("Cannot attach null observer");
		}
		this.getObservers().add(observer);
	}

	@Override
	public void detachObserver(CompletedOrderObserver observer) {
		this.getObservers().remove(observer);
	}

	@Override
	public void notifyOrderComplete(CompletedOrderEvent event)
			throws IllegalArgumentException {
		for (CompletedOrderObserver obs : this.getObservers()) {
			obs.updateCompletedOrder(event);
		}
	}
	
	//--------------------------------------------------------------------------
	// Virtual
	//--------------------------------------------------------------------------
	/**
	 * Construct a new VirtualAssemblyLine that represents the current state of 
	 * this AssemblyLine
	 * 
	 * @return A new VirtualAssemblyLine representing the current state of this
	 * 		   Assemblyline.
	 */
	public VirtualAssemblyLine newVirtualAssemblyLine() {
		TaskType[] t = new TaskType[this.getAssemblyLineSize()];
		return new VirtualAssemblyLine(this.getTaskTypes().toArray(t), this.getOrdersPerWorkStation());
	}
	
	/**
	 * Get a list of the length of the number of the workposts specifying the 
	 * order if they have one. 
	 * 
	 * @return A list of the length of the number of the workposts with the corresponding order if it exists.
	 */
	public List<Optional<Order>> getOrdersPerWorkStation() {
		List<Optional<Order>> result = new ArrayList<>();
		for (WorkPost wp : this.getWorkPosts()) {
			result.add(wp.getOrder());
		}
		return result;
	}

	/**
	 * Calculate the remaining time this order will spend on this line.
	 * 
	 * @param order
	 * 		The order to check for
	 * @return the estimated remaining time
	 * @throws IllegalArgumentException
	 * 		If the order is not present on this line
	 */
	public DateTime getEstimatedCompletionTime(OrderView order) {
		if(!this.contains(order))
			throw new IllegalArgumentException("Order not present on this line");
		boolean found = false;
		DateTime time = new DateTime(0, 0, 0);
		for(int i=0; i<this.getAssemblyLineSize();i++){
			WorkPost currentWorkPost = this.getWorkPost(i);
			if(currentWorkPost.contains(order))
				found = true;
			if(found){
				time = time.addTime(new DateTime(0, 0,
						order.getModel().
						getMinsOnWorkPostOfType(currentWorkPost.getTaskType())));
			}
		}
		return time;
	}

	//--------------------------------------------------------------------------
	// Event Consumer
	//--------------------------------------------------------------------------
	protected EventConsumer getEventConsumer() {
		return this.eventConsumer;
	}
	
	private final EventConsumer eventConsumer;

}
