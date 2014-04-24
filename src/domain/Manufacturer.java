package domain;

import java.util.Comparator;
import java.util.List;

import domain.restrictions.OptionRestrictionManager;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;
import domain.assemblyLine.AssemblyLine;
import domain.assemblyLine.AssemblyTaskContainer;
import domain.assemblyLine.SchedulerIntermediate;
import domain.assemblyLine.WorkPostContainer;
import domain.car.Model;
import domain.car.ModelCatalog;
import domain.car.Option;
import domain.car.Specification;
import domain.order.CompletedOrderCatalog;
import domain.order.Order;
import domain.order.OrderContainer;
import domain.order.OrderFactory;
import domain.order.OrderSession;
import domain.order.SingleOrderSession;
import domain.order.SingleTaskCatalog;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.productionSchedule.ProductionScheduleFacade;
import domain.productionSchedule.strategy.AlgorithmStrategyFactory;
import domain.productionSchedule.strategy.SchedulingStrategy;
import domain.productionSchedule.strategy.SchedulingStrategyView;

/**
 * A class which represents the book-keeping body of the system.
 * This class keeps the completed orders and hands the UI information about which models can be ordered.
 * It passes information for a new order to the productionSchedule, which instantiates new orders.
 * 
 * @author Martinus Wilhelmus Tegelaers, Frederik Goovaerts
 */
public class Manufacturer {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new manufacturer object with all given parameters as subsystems
	 * to interface with.
	 * 
	 * @param stratFact
	 * 		The {@link AlgorithmStrategyFactory} for the new {@link Manufacturer}
	 * @param singleCat
	 * 		The {@link SingleTaskCatalog} for the new {@link Manufacturer}
	 * @param completedCat
	 * 		The {@link CompletedOrderCatalog} for the new {@link Manufacturer}
	 * @param modelCat
	 * 		The {@link ModelCatalog} for the new {@link Manufacturer}
	 * @param optionRestMan
	 * 		The {@link OptionRestrictionManager} for the new {@link Manufacturer}
	 * @param assemblyLine
	 * 		The {@link AssemblyLine} for the new {@link Manufacturer}
	 * @param prodSched
	 * 		The {@link ProductionScheduleFacade} for the new {@link Manufacturer}
	 * @throws IllegalArgumentException
	 * 		If any of the parameters is null
	 */
	public Manufacturer(AlgorithmStrategyFactory stratFact,
						SingleTaskCatalog singleCat,
						CompletedOrderCatalog completedCat,
						ModelCatalog modelCat,
						OptionRestrictionManager optionRestMan,
						ProductionScheduleFacade prodSched,
						OrderFactory orderFactory)
						throws IllegalArgumentException
	{
		if(stratFact == null)
			throw new IllegalArgumentException("AlgorithmStrategyFactory should not be null.");
		if(singleCat == null)
			throw new IllegalArgumentException("SingleTaskCatalog should not be null.");
		if(completedCat == null)
			throw new IllegalArgumentException("CompletedOrderCatalog should not be null.");
		if(modelCat == null)
			throw new IllegalArgumentException("ModelCatalog should not be null.");
		if(optionRestMan == null)
			throw new IllegalArgumentException("OptionRestrictionManager should not be null.");
		if(prodSched == null)
			throw new IllegalArgumentException("ProductionScheduleFacade should not be null.");
		
		this.algorithmStrategyFactory = stratFact;
		this.singleTaskCatalog = singleCat;
		this.completedOrderCatalog = completedCat;
		this.modelCatalog = modelCat;
		this.optionRestrictionManager = optionRestMan;
		this.productionScheduleFacade = prodSched;
		this.orderFactory = orderFactory;
	}
	
	/** The SingleTaskCatalog of this Manufacturer. */
	private final SingleTaskCatalog singleTaskCatalog;

	
	//--------------------------------------------------------------------------
	// Methods concerning multiple subsystems
	//--------------------------------------------------------------------------
	/**
	 * Get the system's pending orders. This includes the orders in the production
	 * schedule, queuing to be assembled, as well as the orders which are active
	 * on the assembly line.
	 * 
	 * @return the list of pending orders in the system
	 */
	public List<OrderContainer> getPendingOrderContainers() {
		List<OrderContainer> pending = this.getAssemblingPendingOrderContainers();
		pending.addAll(this.getSchedulePendingOrderContainers());
		return pending;
	}
	
	
	/**
	 * Query the system for estimated completion time of given order.
	 * Checks sequentially if the order is found in the ProductionSchedule, the
	 * AssemblyLine and the CompletedOrdersCatalog.
	 * If the order is found, it queries the respective component for the ECT.
	 * If the order is not present in the system, the system throws an
	 * OrderDoesNotExistException.
	 * 
	 * @param order
	 * 		The order to find in the system and return the ECT for
	 * 
	 * @return the ECT of given order
	 * 
	 * @throws OrderDoesNotExistException
	 * 		When the order is not found in the system.
	 */
	//TODO Aan elkaar knopen
	//FIXME FIX EVERYTHING
	public DateTime getEstimatedCompletionTime(OrderContainer order) throws OrderDoesNotExistException{
		if(this.getProductionSchedule().contains(order))
			return this.getProductionSchedule().getEstimatedCompletionTime(order);
		if(this.getAssemblyLine().contains(order))
			return this.getSchedulerIntermediate().getEstimatedCompletionTime(order);
		if(this.getCompletedOrderCatalog().contains(order))
			return this.getCompletedOrderCatalog().getCompletionTime(order);
		throw new OrderDoesNotExistException("Order was not found in the system.");
	}
	
	
	//--------------------------------------------------------------------------
	// AlgorithStrategyFactory methods.
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	// Algorithm methods.
	//--------------------------------------------------------------------------
	/**
	 * Get the AlgorithmFactory of this Manufacturer
	 * 
	 * @return the AlogorithmFactory of this Manufacturer. 
	 */
	public AlgorithmStrategyFactory getAlgorithmFactory() {
		return this.algorithmStrategyFactory;
	}
	
	/**
	 * Set a new SchedulingStrategy of the ProductionSchedule subsystem.
	 * 
	 * @param strat
	 * 		The new SchedulingStrategy of this Manufacturer's ProductionSchedule subsystem.
	 */
	public void setNewSchedulingAlgorithm(SchedulingStrategy strat) {
		this.getProductionSchedule().setNewSchedulingAlgorithm(strat);
	}
	
	/**
	 * Get a list of SchedulingStrategyViews of all available SchedulingAlgorithms
	 * 
	 * @return The SchedulingStrategyViews
	 */
	public List<SchedulingStrategyView> getAlgorithms() {
		return this.getAlgorithmFactory().getAlgorithmViews();
	}
	
	/**
	 * Get a view of the currently used SchedulingAlgorithm
	 * 
	 * @return The SchedulingAlgorithm
	 */
	public SchedulingStrategyView getCurrentAlgorithm() {
		return this.getProductionSchedule().getCurrentSchedulingAlgorithm();
	}
	
	/**
	 * Set the currently used SchedulingAlgorithm to the FIFO algorithm
	 */
	public void setFifoAlgorithm() {
		this.getProductionSchedule().setNewSchedulingAlgorithm(
				this.getAlgorithmFactory().getFifoStrategy());
	}
	
	/**
	 * Get the batches that are currently eligible for use in batch strategies.
	 * 
	 * @return The batches
	 */
	public List<Specification> getCurrentBatches() {
		return this.getProductionSchedule().getEligibleBatches();
	}
	
	/**
	 * Set the currently used SchedulingAlgorithm to a batch strategy that
	 * uses the specified Specification.
	 * 
	 * @param batch
	 * 		The batch used to compare Orders
	 * @throws IllegalArgumentException
	 * 		batch is null
	 */
	public void setBatchAlgorithm(Specification batch) throws IllegalArgumentException {
		this.getProductionSchedule().setNewSchedulingAlgorithm(
				this.getAlgorithmFactory().getBatchStrategy(batch));
	}

	
	/** The AlgorithmStrategyFactory of this Manufacturer. */
	private final AlgorithmStrategyFactory algorithmStrategyFactory;


	//--------------------------------------------------------------------------
	// OrderFactory
	//--------------------------------------------------------------------------
	
	/**
	 * Get the OrderFactory of this Manufacturer.
	 * 
	 * @return the OrderFactory of this Manufacturer.
	 */
	public OrderFactory getOrderFactory() {
		return this.orderFactory;
	}
	
	/** The OrderFactory of this Manufacturer. */
	private final OrderFactory orderFactory;
	
	/**
	 * Create and return a new order session.
	 * 
	 * @return the new order session
	 * 
	 * @post result.getManufacturer = this
	 */
	public OrderSession getNewOrderSession() {
		return new OrderSession(this);
	}
	
	/**
	 * Create and return a new single task order session.
	 * 
	 * @return	the new single task order session
	 */
	public SingleOrderSession startNewSingleTaskOrderSession() {
		return new SingleOrderSession(this, this.singleTaskCatalog);
	}
	
	/**
	 * Submit the parameters for a single task order to the production schedule.
	 * We expect the schedule to make an order and schedule it. The schedule
	 * will also return the order, which is further passed on.
	 * 
	 * @param option
	 * 		The option for the single task order
	 * @param deadline
	 * 		The deadline for the single task order
	 * 
	 * @return
	 * 		The generated order
	 * 
	 * @throws IllegalArgumentException
	 * 		If the given option is not an option of the singleTaskCatalog
	 * @throws IllegalArgumentException
	 * 		If either of the arguments is null
	 */
	public OrderContainer submitSingleTaskOrder(Option option, DateTime deadline) {
		SingleTaskOrder order = this.getOrderFactory().makeNewSingleTaskOrder(deadline, option);
		this.getProductionSchedule().submitSingleTaskOrder(order);
		return order;
	}
	
	/**
	 * Check whether or not given option is present in the singleTaskCatalog
	 * 
	 * @param option
	 * 		The option to check if it is present in the singleTaskCatalog
	 * 
	 * @return whether or not given option is present in the singleTaskCatalog
	 * 
	 * @throws IllegalArgumentException
	 * 		| option == null
	 */
	public boolean singleTaskCatalogContains(Option option) throws IllegalArgumentException{
		return this.singleTaskCatalog.contains(option);
	}
	
	//--------------------------------------------------------------------------
	// ModelCatalog
	//--------------------------------------------------------------------------
		
	/**
	 * Get the modelCatalog for internal use
	 * 
	 * @return the modelCatalog of this class
	 */
	private ModelCatalog getModelCatalog(){
		return this.modelCatalog;
	}
	
	/** The modelCatalog of this system, kept by the manufacturer */
	private ModelCatalog modelCatalog;
	
	/**
	 * Get a list of the available Models in the system.
	 * 
	 * @return a list of the available Models in the system
	 */
	public List<Model> getCarModels() {
		return this.getModelCatalog().getModels();
	}
	
	/** 
	 * Get the model of a SingleTaskOrder.
	 * 
	 * @return the model of a SingleTaskOrder
	 */
	public Model getSingleTaskModel() {
		return this.getModelCatalog().getSingleTaskModel();
	}

	/**
	 * Check whether or not given model is a model of this system.
	 * 
	 * @param model
	 * 		The model to check for.
	 * 
	 * @return whether or not given model is part of the system
	 */
	public boolean isValidModel(Model model){
		if(model == null)
			throw new IllegalArgumentException("Null is not a valid model.");
		return this.getModelCatalog().contains(model);
	}

	/**
	 * Check whether the modelcatalog contains given model
	 * 
	 * @param model
	 * 		The model to check for
	 * @return whether or not the catalog contains the model
	 */
	public boolean modelCatalogContains(Model model) {
		return this.getModelCatalog().contains(model);
	}
	
	//--------------------------------------------------------------------------
	// Restrictions Manager
	//--------------------------------------------------------------------------

	/**
	 * Check whether given model and given specs match all restrictions
	 * 
	 * @param model
	 * 		The model to check
	 * @param specification
	 * 		The specification to check
	 * 
	 * @return whether given model and given specs match all restrictions
	 */
	public boolean checkSpecificationRestrictions(Model model,
			Specification specification) {
		return this.getOptionRestrictionManager().checkValidity(model, specification.getOptions());
	}
	
	/**
	 * Get the optionRestrictionManager for internal use
	 * 
	 * @return the optionRestrictionManager of this class
	 */
	private OptionRestrictionManager getOptionRestrictionManager(){
		return this.optionRestrictionManager;
	}
	
	/** The restrictionManager this manufacturer keeps, to check given restriction on new orders */
	private final OptionRestrictionManager optionRestrictionManager;

	/**
	 * Check whether or not the given model and list of options would make a valid
	 * order. This encompasses checking whether all options are options of the model,
	 * whether there are no options that are both from the same optionCategory,
	 * and whether the set of options matches the restriction imposed on the system.
	 * If the arguments are not valid, either themselves, or in combination with
	 * each other, an exception occurs.
	 * If the options do not pass the restrictions check, the order is not valid.
	 * If everything, including the restrictions, checks out, the order is valid. 
	 * 
	 * @return whether this combination is valid 
	 * @param model
	 * 		The given model to check with the options
	 * @param options
	 * 		The given options to check with the model
	 * @throws IllegalArgumentException
	 * 		When either of the parameters is or contains null
	 * @throws IllegalCarOptionCombinationException 
	 * 		When the list of options is not valid with given model
	 */
	public boolean checkOrderValidity(Model model, List<Option> options)
			throws IllegalArgumentException, IllegalCarOptionCombinationException
	{
		if(model == null)
			throw new IllegalArgumentException("Model schould not be null");
		if(options == null)
			throw new IllegalArgumentException("Options list should not be null.");
		if(options.contains(null))
			throw new IllegalArgumentException("Options list should not contain null.");
		if(!model.checkOptionsValidity(options))
			throw new IllegalCarOptionCombinationException("These options do not match given model.");
		if(!this.getOptionRestrictionManager().checkValidity(model,options))
			return false;
		return true;
	}
	

	//--------------------------------------------------------------------------
	// ProductionScheduleFacade related variables and methods. 
	//--------------------------------------------------------------------------
	/**
	 * Get the ProductionSchedule of this Manufacturer.
	 * 
	 * @return The ProductionSchedule of this Manufacturer. 
	 */
	public ProductionScheduleFacade getProductionSchedule() {
		return this.productionScheduleFacade;
	}
	
	/** Interface into production schedule functionality. */
	private final ProductionScheduleFacade productionScheduleFacade;

	//--------------------------------------------------------------------------


	/**
	 * Get a list of pending {@link OrderContainer}s in the productionSchedule.
	 * 
	 * @return List of pending order containers in the productionSchedule.
	 */
	private List<OrderContainer> getSchedulePendingOrderContainers() {
		return this.getProductionSchedule().getPendingStandardOrderContainers();
	}
	
	/**
	 * Submit given model and list of options to the system to form a new order.
	 * An order is formed if the model and options form a valid model, concerning
	 * compatibility and restrictions; Else, a relevant exception is thrown.
	 * 
	 * @param model
	 * 		The model for the new order
	 * @param options
	 * 		The list of options for the new order
	 * 
	 * @return the new order, made from the arguments
	 * 
	 * @throws IllegalCarOptionCombinationException 
	 * 		When the list of options is not valid with given model
	 * @throws IllegalArgumentException
	 * 		When either of the parameters is or contains null
	 * @throws OptionRestrictionException
	 * 		When the set of options does not meet the system's restrictions
	 */
	public OrderContainer submitStandardOrder(Model model, List<Option> options)
			throws IllegalArgumentException,
			IllegalCarOptionCombinationException,
			OptionRestrictionException
	{
		if(model == null)
			throw new IllegalArgumentException("Model schould not be null");
		if(options == null)
			throw new IllegalArgumentException("Options list should not be null.");
		if(options.contains(null))
			throw new IllegalArgumentException("Options list should not contain null.");
		if(!checkOrderValidity(model, options))
			throw new OptionRestrictionException("Options do not meet Restriction criteria.");
		Specification orderSpecs = model.makeSpecification(options);
		StandardOrder newOrder = this.getOrderFactory().makeNewStandardOrder(model, orderSpecs);
		this.getProductionSchedule().submitStandardOrder(newOrder);
		return newOrder;
	}

	
	//--------------------------------------------------------------------------
	// AssemblyLine-related variables and methods
	//--------------------------------------------------------------------------
	/** This Manufacturer's AssemblyLine */
	private AssemblyLine assemblyLine;

	/**
	 * Get the AssemblyLine of this Manufacturer.
	 * 
	 * @return The AssemblyLine
	 */
	private AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}

	/**
	 * Set this Manufacturer's AssemblyLine to the specified AssemblyLine
	 * 
	 * @param assemblyLine
	 * 		The AssemblyLine
	 * @throws IllegalArgumentException
	 * 		assemblyLine is null
	 */
	public void setAssemblyLine(AssemblyLine assemblyLine) throws IllegalArgumentException {
		if (assemblyLine == null) {
			throw new IllegalArgumentException("Cannot set AssemblyLine in Manufacturer to null");
		}
		this.assemblyLine = assemblyLine;
	}
	
	/** This Manufacturer's AssemblyLine */
	private SchedulerIntermediate lineIntermediate;

	/**
	 * Get the AssemblyLine of this Manufacturer.
	 * 
	 * @return The AssemblyLine
	 */
	public SchedulerIntermediate getSchedulerIntermediate() {
		return this.lineIntermediate;
	}

	/**
	 * Set this Manufacturer's AssemblyLine to the specified AssemblyLine
	 * 
	 * @param assemblyLine
	 * 		The AssemblyLine
	 * @throws IllegalArgumentException
	 * 		assemblyLine is null
	 */
	public void setSchedulerIntermediate(SchedulerIntermediate schedulerIntermediate)
			throws IllegalArgumentException {
		if (schedulerIntermediate == null) {
			throw new IllegalArgumentException("Cannot set SchedulerIntermediate in Manufacturer to null");
		}
		this.lineIntermediate = schedulerIntermediate;
	}

	/**
	 * Ask the AssemblyLine for its WorkPostContainers.
	 * 
	 * @return The WorkPostContainers
	 */
	public List<WorkPostContainer> getWorkPostContainers() {
		return this.getAssemblyLine().getWorkPostContainers();
	}

	/**
	 * Ask the AssemblyLine for the AssemblyTaskContainers at the specified
	 * WorkPost.
	 * 
	 * @param postNum
	 * 		The WorkPost of interest
	 * @return Those AssemblyTaskContainers that are of the same type as the
	 * 		specified WorkPost
	 * @throws IllegalArgumentException
	 * 		See {@link AssemblyLine#getAssemblyTasksAtPost(int) getAssemblyTasksAtPost(int)}
	 */
	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int postNum) throws IllegalArgumentException {
		return this.getAssemblyLine().getAssemblyTasksAtPost(postNum);
	}

	/**
	 * Ask the AssemblyLine to complete the specified AssemblyTask at the specified
	 * WorkPost in the specified amount of minutes.
	 * 
	 * @param workPostNumber
	 * 		The WorkPost of interest
	 * @param taskNumber
	 * 		The AssemblyTask of interest
	 * @param minutes
	 * 		The amount of minutes the task was completed in
	 * @throws IllegalArgumentException
	 * 		See {@link AssemblyLine#completeWorkpostTask(int, int, int) completeWorkpostTask(int, int, int)}
	 * @throws IllegalStateException
	 * 		See {@link AssemblyLine#completeWorkpostTask(int, int, int) completeWorkpostTask(int, int, int)}
	 */
	public void completeWorkpostTask(int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException,
	IllegalStateException {
		this.getAssemblyLine().completeWorkpostTask(workPostNumber, taskNumber, minutes);
	}
	
	/**
	 * Get a list of pending {@link OrderContainer}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	private List<OrderContainer> getAssemblingPendingOrderContainers() {
		return this.getAssemblyLine().getActiveOrderContainers();
	}

	//--------------------------------------------------------------------------
	// Completed Order Methods
	//--------------------------------------------------------------------------
	/** Registers the Orders that have been completed */
	private CompletedOrderCatalog completedOrderCatalog;

	/**
	 * Get this Manufacturer's CompletedOrderCatalog
	 * 
	 * @return The CompletedOrderCatalog
	 */
	private CompletedOrderCatalog getCompletedOrderCatalog() {
		return this.completedOrderCatalog;
	}

	/**
	 * Add the given order to the completedOrderCatalog.
	 * This method passes the order right through.
	 * 
	 * @param order
	 * 		The order to add to the catalog
	 * @throws IllegalStateException
	 * 		If the order is already completed and/or in the catalog
	 * @throws IllegalArgumentException
	 * 		If given order is null
	 */
	public void addToCompleteOrders(Order order)
			throws IllegalStateException,
			IllegalArgumentException{
		if(order == null)
			throw new IllegalArgumentException("Order Can not be null");
		this.getCompletedOrderCatalog().addCompletedOrder(order);
	}
	
	/**
	 * Get the system's completed orders. This encompasses the orders which were
	 * previously assembled and are now marked as complete.
	 * 
	 * @return the list of completed orders in the system
	 */
	public List<OrderContainer> getCompletedOrderContainers() {
		return this.getCompletedOrderCatalog().getCompletedOrderContainers();
	}
	
	//--------------------------------------------------------------------------
	// Time-related methods
	//--------------------------------------------------------------------------
	/**
	 * Increments the time with the specified DateTime
	 * 
	 * @param dt
	 * 		The time to increment with
	 */
	public void incrementTime(DateTime dt) {
		this.getProductionSchedule().incrementTime(dt);
	}

	//--------------------------------------------------------------------------
	// Querying the statistics
	//--------------------------------------------------------------------------
	/**
	 * Ask this Manufacturer's AssemblyLine for a report of statistics concerning its
	 * production activities.
	 * 
	 * @return The report
	 */
	public String getStatisticsReport() {
		return this.getAssemblyLine().getStatisticsReport();
	}

}
