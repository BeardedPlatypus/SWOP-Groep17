package domain;

import java.util.List;

import domain.restrictions.OptionRestrictionManager;
import exceptions.IllegalVehicleOptionCombinationException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;
import domain.assembly_line.AssemblyFloor;
import domain.assembly_line.AssemblyLine;
import domain.assembly_line.AssemblyLineState;
import domain.assembly_line.AssemblyLineStateView;
import domain.assembly_line.AssemblyLineView;
import domain.assembly_line.AssemblyTaskView;
import domain.assembly_line.SchedulerIntermediate;
import domain.assembly_line.WorkPostView;
import domain.car.ModelCatalog;
import domain.car.Option;
import domain.car.Specification;
import domain.car.Model;
import domain.clock.Clock;
import domain.order.CompletedOrderCatalog;
import domain.order.Order;
import domain.order.OrderView;
import domain.order.OrderFactory;
import domain.order.OrderSession;
import domain.order.SingleOrderSession;
import domain.order.SingleTaskCatalog;
import domain.order.SingleTaskOrder;
import domain.order.StandardOrder;
import domain.productionSchedule.SchedulerContext;
import domain.productionSchedule.strategy.AlgorithmStrategyFactory;
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
	 * @param floor
	 * 		The {@link AssemblyLine} for the new {@link Manufacturer}
	 * @param clock
	 * 		The {@link SchedulerIntermediate} for the new {@link Manufacturer}
	 * @param schedule 
	 * @param prodSched
	 * 		The {@link ProductionScheduleFacade} for the new {@link Manufacturer}
	 * @param inter 
	 * @param line 
	 * @throws IllegalArgumentException
	 * 		If any of the parameters is null
	 */
	public Manufacturer(AlgorithmStrategyFactory stratFact,
						SingleTaskCatalog singleCat,
						CompletedOrderCatalog completedCat,
						ModelCatalog modelCat,
						OptionRestrictionManager optionRestMan,
						OrderFactory orderFactory,
						AssemblyFloor floor,
						Clock clock,
						SchedulerContext schedule)
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
		if(orderFactory == null)
			throw new IllegalArgumentException("OptionRestrictionManager should not be null.");
		if(floor == null)
			throw new IllegalArgumentException("OptionRestrictionManager should not be null.");
		if(clock == null)
			throw new IllegalArgumentException("OptionRestrictionManager should not be null.");
		if(schedule == null)
			throw new IllegalArgumentException("OptionRestrictionManager should not be null.");
		
		this.algorithmStrategyFactory = stratFact;
		this.singleTaskCatalog = singleCat;
		this.completedOrderCatalog = completedCat;
		this.modelCatalog = modelCat;
		this.optionRestrictionManager = optionRestMan;
		this.orderFactory = orderFactory;
		this.assemblyFloor = floor;
		this.clock = clock;
		this.schedulerContext = schedule;


		this.orderFactory.setManufacturer(this);

	}
	

	
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
	public List<OrderView> getPendingOrderContainers() {
		List<OrderView> pending = this.getAssemblingPendingOrderContainers();
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
	public DateTime getEstimatedCompletionTime(OrderView order) throws OrderDoesNotExistException{
		//TODO: DIVERT THIS TO THE STATISTICS
		// http://content.artofmanliness.com/uploads//2010/10/snidely-whiplash.jpg
		
//		if(this.getProductionSchedule().contains(order))
//			return this.getProductionSchedule().getEstimatedCompletionTime(order);
//		if(this.getAssemblyFloor().contains(order))
//			return this.getAssemblyFloor().getEstimatedCompletionTime(order);
//		if(this.getCompletedOrderCatalog().contains(order))
//			return this.getCompletedOrderCatalog().getCompletionTime(order);
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
	
//	/**
//	 * Set a new SchedulingStrategy of the ProductionSchedule subsystem.
//	 * 
//	 * @param strat
//	 * 		The new SchedulingStrategy of this Manufacturer's ProductionSchedule subsystem.
//	 */
//	public void setNewSchedulingAlgorithm(SchedulingStrategy strat) {
//		this.getProductionSchedule().setNewSchedulingAlgorithm(strat);
//	}
	
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
		return this.getProductionSchedule().getCurrentSchedulingStrategy();
	}
	
	/**
	 * Set the currently used SchedulingAlgorithm to the FIFO algorithm
	 */
	public void setFifoAlgorithm() {
		this.getProductionSchedule().setSchedulingStrategy(
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
		this.getProductionSchedule().setSchedulingStrategy(
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
	 * Get the {@link SingleTaskCatalog} of this {@link Manufacturer}.
	 * 
	 * @return the {@link SingleTaskCatalog}.
	 */
	public SingleTaskCatalog getSingleTaskCatalog(){
		return this.singleTaskCatalog;
	}

	/** The SingleTaskCatalog of this Manufacturer. */
	private final SingleTaskCatalog singleTaskCatalog;
	
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
	public OrderView submitSingleTaskOrder(Option option, DateTime deadline) {
		SingleTaskOrder order = this.getOrderFactory().makeNewSingleTaskOrder(deadline, option);
		this.getProductionSchedule().addNewSingleTaskOrder(order);
		
		//TODO Fix the unidling in the floor
		
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
	public List<Model> getVehicleModels() {
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
	 * @throws IllegalVehicleOptionCombinationException 
	 * 		When the list of options is not valid with given model
	 */
	private boolean checkOrderRestrictionValidity(Model model, List<Option> options)
			throws IllegalArgumentException, IllegalVehicleOptionCombinationException
	{
		if(model == null)
			throw new IllegalArgumentException("Model schould not be null");
		if(options == null)
			throw new IllegalArgumentException("Options list should not be null.");
		if(options.contains(null))
			throw new IllegalArgumentException("Options list should not contain null.");
		if(!model.checkOptionsValidity(options))
			throw new IllegalVehicleOptionCombinationException("These options do not match given model.");
		if(!this.getOptionRestrictionManager().checkValidity(model,options))
			return false;
		return true;
	}
	

	//--------------------------------------------------------------------------
	// ProductionSchedule related variables and methods. 
	//--------------------------------------------------------------------------
	/**
	 * Get the ProductionSchedule of this Manufacturer.
	 * 
	 * @return The ProductionSchedule of this Manufacturer. 
	 */
	public SchedulerContext getProductionSchedule() {
		return this.schedulerContext;
	}
	
	/** Interface into production schedule functionality. */
	private final SchedulerContext schedulerContext;

	//--------------------------------------------------------------------------


	/**
	 * Get a list of pending {@link OrderView}s in the productionSchedule.
	 * 
	 * @return List of pending order containers in the productionSchedule.
	 */
	private List<OrderView> getSchedulePendingOrderContainers() {
		return this.getProductionSchedule().getPendingStandardOrders();
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
	 * @throws IllegalVehicleOptionCombinationException 
	 * 		When the list of options is not valid with given model
	 * @throws IllegalArgumentException
	 * 		When either of the parameters is or contains null
	 * @throws OptionRestrictionException
	 * 		When the set of options does not meet the system's restrictions
	 */
	public OrderView submitStandardOrder(Model model, List<Option> options)
			throws IllegalArgumentException,
			IllegalVehicleOptionCombinationException,
			OptionRestrictionException
	{
		if(model == null)
			throw new IllegalArgumentException("Model schould not be null");
		if(options == null)
			throw new IllegalArgumentException("Options list should not be null.");
		if(options.contains(null))
			throw new IllegalArgumentException("Options list should not contain null.");
		if(!checkOrderRestrictionValidity(model, options))
			throw new OptionRestrictionException("Options do not meet Restriction criteria.");
		Specification orderSpecs = model.makeSpecification(options);
		StandardOrder newOrder = this.getOrderFactory().makeNewStandardOrder(model, orderSpecs);
		this.getProductionSchedule().addNewStandardOrder(newOrder);
		
		//TODO fix unidling in the floor
		
		return newOrder;
	}

	
	//--------------------------------------------------------------------------
	// AssemblyFloor and AssemblyLine-related variables and methods
	//--------------------------------------------------------------------------
	/** This Manufacturer's AssemblyFloor */
	private final AssemblyFloor assemblyFloor;

	/**
	 * Get the AssemblyFloor of this Manufacturer.
	 * 
	 * @return The AssemblyFloor
	 */
	private AssemblyFloor getAssemblyFloor() {
		return this.assemblyFloor;
	}

	/**
	 * Ask the AssemblyLine for its WorkPostContainers.
	 * 
	 * @return The WorkPostContainers
	 */
	public List<AssemblyLineView> getAssemblyLineViews() {
		//TODO set this up as required
		return this.getAssemblyFloor().getLineViews();
	}
	


	public List<WorkPostView> getWorkPostsAt(int lineNb) {
		// TODO Auto-generated method stub
		return this.getAssemblyFloor().getWorkPostViewsAt(lineNb);
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
	public List<AssemblyTaskView> getAssemblyTasksAtPost(int lineNum, int postNum) throws IllegalArgumentException {
		//TODO also add layer of indirection
		return this.getAssemblyFloor().getAssemblyTasksAtPost(lineNum, postNum);
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
	public void completeWorkpostTask(int lineNumber, int workPostNumber, int taskNumber, int minutes) throws IllegalArgumentException,
	IllegalStateException {
		this.getAssemblyFloor().completeWorkpostTask(lineNumber, workPostNumber, taskNumber, minutes);
	}
	
	/**
	 * Get a list of pending {@link OrderView}s on the assembly line. 
	 * 
	 * @return List of pending order containers on the assembly line.
	 */
	private List<OrderView> getAssemblingPendingOrderContainers() {
		//TODO add layer of indirection
		return this.getAssemblyFloor().getActiveOrderContainers();
	}
	

	//--------- Assembly States ---------//

	public List<AssemblyLineStateView> getAvailableStates() {
		// TODO Auto-generated method stub
		return null;
	}



	public AssemblyLineState getStateInstance(int stateNum) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<AssemblyLineStateView> getCurrentLineStates() {
		// TODO Auto-generated method stub
		return null;
	}

	//----- end of Assembly States -----//

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
	public List<OrderView> getCompletedOrderContainers() {
		return this.getCompletedOrderCatalog().getCompletedOrderContainers();
	}
	
	//--------------------------------------------------------------------------
	// Clock and Time-related methods
	//--------------------------------------------------------------------------

	/** The manufacturer's clock */
	private final Clock clock;
	
	/**
	 * Increments the time with the specified DateTime
	 * 
	 * @param dt
	 * 		The time to increment with
	 */
	public void incrementTime(DateTime dt) {
		
		//TODO how does this impact the clock? Put this away for good?
		//this.getProductionSchedule().incrementTime(dt);
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
		return this.getAssemblyFloor().getStatisticsReport();
	}


}
