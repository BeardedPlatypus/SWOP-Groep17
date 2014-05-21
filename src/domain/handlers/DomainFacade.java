package domain.handlers;

import java.util.List;

import domain.DateTime;
import domain.assemblyLine.AssemblyTaskView;
import domain.assemblyLine.WorkPostView;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import domain.car.Specification;
import domain.order.OrderView;
import domain.productionSchedule.strategy.SchedulingStrategyView;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;

/**
 * Facade on all handlers, so the UI only needs one object to interface with.
 * 
 * @author Frederik Goovaerts
 */
public class DomainFacade {

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Construct a domainfacade with all given handlers as fields.
	 * 
	 * @param performHandler
	 * 		The new PerformAssemblyTaskHandler for this facade.
	 * @param newOrderHandler
	 * 		The new NewOrderSessionHandler for this facade.
	 * @param singleTaskHandler
	 * 		The new OrderSingleTaskHandler for this facade.
	 * @param orderDetailsHandler
	 * 		The new OrderDetailsHandler for this facade.
	 * @param algorithmHandler
	 * 		The new SchedulingAlgorithmHandler for this facade.
	 * @throws IllegalArgumentException
	 * 		If any of the given handlers is null.
	 */
	public DomainFacade(AdaptSchedulingAlgorithmHandler algorithmHandler,
			AssemblyLineStatusHandler assemblyLineStatusHandler,
			CheckOrderDetailsHandler orderDetailsHandler,
			CheckProductionStatisticsHandler prodStatHandler,
			NewOrderSessionHandler newOrderHandler,
			OrderSingleTaskHandler singleTaskHandler,
			PerformAssemblyTaskHandler performHandler)
					throws IllegalArgumentException
					{
		if(algorithmHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(assemblyLineStatusHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(orderDetailsHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(prodStatHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(newOrderHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(singleTaskHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(performHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		this.schedulingAlgorithmHandler = algorithmHandler;
		this.assemblyLineStatusHandler = assemblyLineStatusHandler;
		this.orderDetailsHandler = orderDetailsHandler;
		this.productionStatisticsHandler = prodStatHandler;
		this.newOrderSessionHandler = newOrderHandler;
		this.orderSingleTaskHandler = singleTaskHandler;
		this.performAssemblyTaskHandler = performHandler;
	}

	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------

	/**
	 * Get the PerformAssemblyTaskHandler.
	 * This is both for internal use, and in the case a developer wants to write
	 * a UI based on the handlers themselves, for better specialization of the
	 * different parts of the UI.
	 * 
	 * @return the performAssemblyTaskHandler
	 */
	public PerformAssemblyTaskHandler getPerformAssemblyTaskHandler() {
		return this.performAssemblyTaskHandler;
	}

	/**
	 * Get the {@link NewOrderSessionHandler} for internal use.
	 * 
	 * @return the newOrderSessionHandler
	 */
	public NewOrderSessionHandler getNewOrderSessionHandler() {
		return this.newOrderSessionHandler;
	}

	/**
	 * Get the OrderSingleTaskHandler.
	 * This is both for internal use, and in the case a developer wants to write
	 * a UI based on the handlers themselves, for better specialization of the
	 * different parts of the UI.
	 * 
	 * @return the orderSingleTaskHandler
	 */
	public OrderSingleTaskHandler getOrderSingleTaskHandler() {
		return this.orderSingleTaskHandler;
	}

	/**
	 * Get the OrderDetailsHandler.
	 * This is both for internal use, and in the case a developer wants to write
	 * a UI based on the handlers themselves, for better specialization of the
	 * different parts of the UI.
	 * 
	 * @return the orderDetailsHandler
	 */
	public CheckOrderDetailsHandler getCheckOrderDetailsHandler() {
		return this.orderDetailsHandler;
	}

	/**
	 * Get the SchedulingAlgorithmHandler.
	 * This is both for internal use, and in the case a developer wants to write
	 * a UI based on the handlers themselves, for better specialization of the
	 * different parts of the UI.
	 * 
	 * @return the schedulingAlgorithmHandler
	 */
	public AdaptSchedulingAlgorithmHandler getAdaptSchedulingAlgorithmHandler() {
		return this.schedulingAlgorithmHandler;
	}	
	
	/**
	 * Get the CheckProductionStatisticsHandler.
	 * This is both for internal use, and in the case a developer wants to write
	 * a UI based on the handlers themselves, for better specialization of the
	 * different parts of the UI.
	 * 
	 * @return the CheckProductionStatisticsHandler
	 */
	public CheckProductionStatisticsHandler getCheckProductionStatisticsHandler() {
		return this.productionStatisticsHandler;
	}
	
	/**
	 * Get the AssemblyLineStatusHandler.
	 * This is both for internal use, and in the case a developer wants to write
	 * a UI based on the handlers themselves, for better specialization of the
	 * different parts of the UI.
	 * 
	 * @return the AssemblyLineStatusHandlerHandler
	 */
	public AssemblyLineStatusHandler getAssemblyLineStatusHandler() {
		return this.assemblyLineStatusHandler;
	}

	/**	The facade's PerformAssemblyTaskHandler */
	private final PerformAssemblyTaskHandler performAssemblyTaskHandler;

	/**	The facade's NewOrderSessionHandler */
	private final NewOrderSessionHandler newOrderSessionHandler;

	/**	The facade's OrderSingleTaskHandler */
	private final OrderSingleTaskHandler orderSingleTaskHandler;

	/**	The facade's OrderDetailsHandler */
	private final CheckOrderDetailsHandler orderDetailsHandler;

	/**	The facade's SchedulingAlgorithmHandler */
	private final AdaptSchedulingAlgorithmHandler schedulingAlgorithmHandler;

	/**	The facade's CheckProductionStatisticsHandler */
	private final CheckProductionStatisticsHandler productionStatisticsHandler;

	/**	The facade's AssemblyLineStatusHandler */
	private final AssemblyLineStatusHandler assemblyLineStatusHandler;

	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------


	//--------------------------------------------------------------------------
	// Algorithm adaption methods

	// All flows
	public List<SchedulingStrategyView> getAlgorithms() {
		return this.getAdaptSchedulingAlgorithmHandler().getAlgorithms();
	}

	public SchedulingStrategyView getCurrentAlgorithm() {
		return this.getAdaptSchedulingAlgorithmHandler().getCurrentAlgorithm();
	}

	//--------------------------------------------------------------------------
	// Fifo flow
	public void setFifoAlgorithm() {
		this.getAdaptSchedulingAlgorithmHandler().setFifoAlgorithm();
	}

	//--------------------------------------------------------------------------
	// Batch flow
	public List<Specification> getCurrentBatches() {
		return this.getAdaptSchedulingAlgorithmHandler().getCurrentBatches();
	}

	public void setBatchAlgorithm(Specification batch) throws IllegalArgumentException {
		this.getAdaptSchedulingAlgorithmHandler().setBatchAlgorithm(batch);
	}

	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Check Order Details methods
	
	/**
	 * Get a copy of the completed orders snapshot list for the user.
	 * If the snapshots are deprecated, these are refreshed first.
	 * 
	 * @return a copy of the completed orders snapshot list.
	 */
	public List<OrderView> getCompletedOrdersContainers(){
		return this.getCheckOrderDetailsHandler().getCompletedOrdersContainers();
	}
	
	/**
	 * Get a copy of the pending orders snapshot list for the user.
	 * If the snapshots are deprecated, these are refreshed first.
	 * 
	 * @return a copy of the pending orders snapshot list.
	 */
	public List<OrderView> getPendingOrdersContainers(){
		return this.getCheckOrderDetailsHandler().getPendingOrdersContainers();
	}
	
	/**
	 * Select order with given index from the Completed Orders snapshot.
	 * Both snapshots are then deprecated and can not be used anymore.
	 * This method can not be used until the snapshots are refreshed.
	 * 
	 * @param orderIndex
	 * 		The index of the wanted order in the list of completed orders
	 * 
	 * @throws IllegalArgumentException
	 * 		When the given index is out of the bounds of the list
	 * @throws IllegalStateException
	 * 		When this method is called with deprecated snapshots
	 */
	public void selectCompletedOrder(int orderIndex)
			throws IllegalArgumentException,
			IllegalStateException
	{
		this.getCheckOrderDetailsHandler().selectCompletedOrder(orderIndex);
	}
	
	/**
	 * Select order with given index from the Pending Orders snapshot.
	 * Both snapshots are then deprecated and can not be used anymore.
	 * This method can not be used until the snapshots are refreshed.
	 * 
	 * @param orderIndex
	 * 		The index of the wanted order in the list of completed orders
	 * 
	 * @throws IllegalArgumentException
	 * 		When the given index is out of the bounds of the list
	 * @throws IllegalStateException
	 * 		When this method is called with deprecated snapshots
	 */
	public void selectPendingOrder(int orderIndex)
			throws IllegalArgumentException,
			IllegalStateException
	{
		this.getCheckOrderDetailsHandler().selectPendingOrder(orderIndex);
	}
	
	/**
	 * Get the Specification of the currently observed Order, if there is one.
	 * 
	 * @return the specification of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 */
	public Specification getCurrentOrderSpecification(){
		return this.getCheckOrderDetailsHandler().getCurrentOrderSpecification();
	}

	/**
	 * Get the submission time of the currently observed Order, if there is one.
	 * 
	 * @return the submission time of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 */
	public DateTime getCurrentOrderSubmissionTime(){
		return this.getCheckOrderDetailsHandler().getCurrentOrderSubmissionTime();
	}

	/**
	 * Get the completion state of the currently observed Order, if there is one.
	 * 
	 * @return the completion state of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 */
	public boolean currentOrderIsComplete(){
		return this.getCheckOrderDetailsHandler().currentOrderIsComplete();
	}

	/**
	 * Get the Completion Time of the currently observed Order, if there is one.
	 * 
	 * @return the Completion Time of the order
	 * 
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 * @throws IllegalStateException
	 * 		If the order has not been completed yet
	 */
	public DateTime getCurrentOrderCompletionTime(){
		return this.getCheckOrderDetailsHandler().getCurrentOrderCompletionTime();
	}

	/**
	 * Get the estimated Completion Time of the currently observed Order, if there is one.
	 * 
	 * @return the estimated Completion Time of the order
	 * 
	 * @throws OrderDoesNotExistException 
	 * 		If the order is not recognised by the system
	 * @throws IllegalStateException
	 * 		If this method is called when no order is set
	 * @throws IllegalStateException
	 * 		If the order is already completed
	 */
	public DateTime getCurrentOrderEstimatedCompletionTime() throws OrderDoesNotExistException{
		return this.getCheckOrderDetailsHandler().getCurrentOrderEstimatedCompletionTime();
	}

	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Check Production Statistics Handler
	
	/**
	 * Query the gathered statistics. The end user is responsible for
	 * meaningful analysis of the report.
	 * 
	 * @return A report in the form of a String
	 */
	public String getStatisticsReport() {
		return this.getCheckProductionStatisticsHandler().getStatisticsReport();
	}
	

	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Order New Car Methods

	/**
	 * Get the system's pending orders. This includes the orders in the production
	 * schedule, queueing to be assembled, as well as the orders which are active
	 * on the assembly line.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @return the list of pending orders in the system
	 */
	public List<OrderView> getPendingOrders(){
		return this.getNewOrderSessionHandler().getPendingOrders();
	}

	/**
	 * Get the system's completed orders. This encompasses the orders which were
	 * previously assembled and are now marked as complete.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @return the list of completed orders in the system
	 */
	public List<OrderView> getCompletedOrders(){
		return this.getNewOrderSessionHandler().getCompletedOrders();
	}
	
	

	/**
	 * Start a new orderSession in the newOrderSessionHandler, so a user can
	 * put together an order from scratch. This resets all previously chosen
	 * model and options in the Handler.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @effect this.getNewOrderSessionHandler().startNewOrderSession()
	 * 
	 */
	public void startNewOrderSession(){
		this.getNewOrderSessionHandler().startNewOrderSession();
	}

	/**
	 * Get a list of all possible Car Models for construction by the system.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @return a list of all possible Car Models for the system
	 */
	public List<Model> getCarModels(){
		return this.getNewOrderSessionHandler().getCarModels();
	}

	/**
	 * Choose the model for the current OrderSession active in the handler.
	 * If a model was previously chosen, and a new session hasn't been started
	 * yet, an exception will occur. If the model is not a model of the system,
	 * an exception will occur.
	 * 
	 * @param model
	 * 		The model the user wants to set for the OrderSession
	 * @throws IllegalArgumentException
	 * 		When the given model is not a model of the system or null
	 * @throws IllegalStateException
	 * 		When a model was already chosen for the current OrderSession
	 * @throws IllegalStateException
	 * 		When there is no active OrderSession
	 */
	public void chooseModel(Model model) throws IllegalArgumentException, IllegalStateException{
		if(model == null)
			throw new IllegalArgumentException("Model can not be null.");
		this.getNewOrderSessionHandler().chooseModel(model);
	}

	/**
	 * Check whether or not all choices have been made for the order specification
	 * 
	 * @return whether or not all choices have been made
	 */
	public boolean orderHasUnfilledOptions(){
		return this.getNewOrderSessionHandler().hasUnfilledOptions();
	}

	/**
	 * Get the next OptionCategory of which no option has been chosen yet.
	 * If no options remain, throw an exception.
	 * 
	 * @return the next unchosen OptionCategory
	 * 
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no optionCategories are unfilled anymore
	 * @throws IllegalStateException
	 * 		If there is no active OrderSession
	 */
	public OptionCategory getNextOptionCategory()
			throws NoOptionCategoriesRemainingException,
			IllegalStateException
			{
		return this.getNewOrderSessionHandler().getNextOptionCategory();
			}

	/**
	 * Add the given option to the current OrderSession specification
	 * 
	 * @param option
	 * 		The option to add to the specification
	 * @throws IllegalArgumentException
	 * 		If the option is null or not an option of the given unfilled category
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no optionCategories are unfulfilled anymore
	 */
	public void selectOption(Option option) throws IllegalArgumentException, NoOptionCategoriesRemainingException{
		if(option == null)
			throw new IllegalArgumentException("Option can not be null.");
		this.getNewOrderSessionHandler().selectOption(option);
	}
	
	/**
	 * Check whether given model and options match, and the options pass the
	 * system's restriction checks.
	 * 
	 * @param model
	 * 		The model to check for
	 * @param options
	 * 		The options to check for
	 * 
	 * @return whether given model and options match, and the options pass the
	 * 		system's restriction checks
	 * 
	 * @throws IllegalArgumentException
	 * 		When either of the arguments is or contains null
	 */
	public boolean isFullyValidOptionSet(Model model, List<Option> options)
			throws IllegalArgumentException{
		return this.getNewOrderSessionHandler().isFullyValidOptionSet(model, options);
	}

	/**
	 * Submit the order composed by the active OrderSession, and return
	 * the resulting order as a container.
	 * 
	 * @return the object of the new order
	 * 
	 * @throws IllegalStateException
	 *		If there is no active new order session.
	 * @throws IllegalStateException
	 * 		If no model has been chosen
	 * @throws IllegalStateException
	 * 		If there are unfulfilled OptionCategories
	 * @throws IllegalCarOptionCombinationException 
	 * 		When the chosen options are not valid with given model
	 * @throws OptionRestrictionException
	 * 		When the set of options does not meet the system's restrictions
	 * @throws IllegalStateException
	 * 		When the order was already submitted
	 */
	public void submitOrder() throws IllegalStateException, IllegalArgumentException, IllegalCarOptionCombinationException, OptionRestrictionException{
		this.getNewOrderSessionHandler().submitOrder();

	}

	/**
	 * When a new order has been constructed with a session, this allows the user
	 * to query the system for the ETA of the new order.
	 * 
	 * @return the ETA of the order made with the current session
	 * 
	 * @throws IllegalStateException
	 * 		If the handler has no active session or the session has not created
	 * 		an order in the system.
	 * @throws OrderDoesNotExistException 
	 * 		If the order of the session is not recognized by the system
	 */
	public DateTime getNewOrderETA() throws IllegalStateException, OrderDoesNotExistException{
		return this.getNewOrderSessionHandler().getNewOrderETA();
	}

	//-------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Single Task Order Methods
	
	/**
	 * Starts a new order session.
	 */
	public void startNewSingleTaskOrderSession() {
		this.getOrderSingleTaskHandler().startNewOrderSession();
	}

	/**
	 * Returns a list of option categories, containing the possible tasks to be ordered
	 * in a single task order.
	 * 
	 * @return	List of option categories.
	 * @throws 	IllegalStateException
	 *			If there is no active new order session.
	 */
	public List<OptionCategory> getPossibleTasks() throws IllegalStateException {
		return this.getOrderSingleTaskHandler().getPossibleTasks();
	}

	/**
	 * Selects an option to be ordered as a single task.
	 * 
	 * @param 	option
	 * 			The option to be ordered in this single task.
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public void selectSingleTaskOption(Option option) throws IllegalStateException {
		this.getOrderSingleTaskHandler().selectOption(option);
	}

	/**
	 * Sets the deadline for this single task order to be within the given number of days, hours and minutes.
	 * 
	 * @param	days
	 * 			The number of days before the deadline.
	 * @param	hours
	 * 			The number of hours before the deadline modulo days.
	 * @param	minutes
	 * 			The number of minutes before the deadline modulo hours.
	 * @throws 	IllegalStateException
	 *			If there is no active new order session.
	 */
	public void specifyDeadline(int days, int hours, int minutes) throws IllegalStateException {
		this.getOrderSingleTaskHandler().specifyDeadline(days, hours, minutes);
	}

	/**
	 * Submits the single task order that's been composed in the active order session and schedules it for completion
	 * Returns the order that's been scheduled.
	 * 
	 * @post	isRunningOrderSession() == false
	 * @return	The submitted order.
	 * @throws 	IllegalStateException
	 *			If there is no active new order session.
	 */
	public OrderView submitSingleTaskOrder() throws IllegalStateException {
		return this.getOrderSingleTaskHandler().submitSingleTaskOrder();
	}

	/**
	 * Returns the estimated completion time for the given order.
	 * 
	 * @param 	order
	 * 			The order of which to poll the estimated completion time.
	 * @return	The estimated completion time for the given order.
	 * @throws  OrderDoesNotExistException 
	 * 			If the order does not exist.
	 */
	public DateTime getEstimatedCompletionTime(OrderView order)
			throws OrderDoesNotExistException {
		return this.getOrderSingleTaskHandler().getEstimatedCompletionTime(order);
	}
	
	/**
	 * Checks whether there is an active single order session.
	 * 
	 * @return	true if there is an active single order session
	 * 			false otherwise
	 */
	public boolean isRunningOrderSession(){
		return this.getOrderSingleTaskHandler().isRunningOrderSession();
	}

	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Perform AssemblyTask Methods
	
	/**
	 * Get the WorkPostContainrs AssemblyLine's WorkPosts.
	 * 
	 * @return the WorkPostContainers of the ASsemblyLine's WorkPosts
	 */
	public List<WorkPostView> getWorkPosts() {
		return this.getPerformAssemblyTaskHandler().getWorkPosts();
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
		return this.getPerformAssemblyTaskHandler().getAssemblyTasksAtWorkPost(workPostNumber);
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
		this.getPerformAssemblyTaskHandler().completeWorkpostTask(workPostNumber, taskNumber, minutes);
	}

	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// AsemblyLine Status methods
	
	/**
	 * Returns a list of work post containers, containing information about the assembly line status.
	 * 
	 * @return List of work post containers.
	 */
	public List<WorkPostView> getStatusWorkPosts() {
		return this.getAssemblyLineStatusHandler().getWorkPosts();
	}
	
	/**
	 * Returns the amount of different work posts on the assembly line.
	 * 
	 * @return Amount of work posts.
	 */
	public int getAmountOfWorkPosts() {
		return this.getAssemblyLineStatusHandler().getAmountOfWorkPosts();
	}
	
	/**
	 * Retrieves a single work post from the list of work posts using the given index.
	 * 
	 * @param 	workPostNumber
	 * 			The index of the work post.
	 * @pre		workPostNumber >= 0 && workPostNumber < getAmountOfWorkPosts()
	 * @return	The work post situated at the given index.
	 * @throws	IllegalArgumentException
	 * 			If the given index does not satisfy the preconditions.
	 */
	public WorkPostView getWorkPost(int workPostNumber) throws IllegalArgumentException {
		return this.getAssemblyLineStatusHandler().getWorkPost(workPostNumber);
	}
	
	/**
	 * Retrieves a list of tasks at the work post identified by the given index.
	 * 
	 * @param 	workPostNumber
	 * 			The index of the work post.
	 * @pre		workPostNumber >= 0 && workPostNumber < getAmountOfWorkPosts()
	 * @return	List of tasks at that work post.
	 * @throws	IllegalArgumentException
	 * 			If the given index does not satisfy the preconditions.
	 */
	public List<AssemblyTaskView> getTasksAtWorkPost(int workPostNumber) throws IllegalArgumentException {
		return this.getAssemblyLineStatusHandler().getTasksAtWorkPost(workPostNumber);
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
		return this.getAssemblyLineStatusHandler().getAmountOfTasksAtWorkPost(workPostNumber);
	}
	

	//--------------------------------------------------------------------------

}
