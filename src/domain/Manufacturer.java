package domain;

import java.util.Comparator;
import java.util.List;

import domain.restrictions.OptionRestrictionManager;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;
import domain.order.CompletedOrderCatalog;
import domain.order.Order;
import domain.order.OrderContainer;
import domain.productionSchedule.ProductionScheduleFacade;

//TODO everything

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
						ProductionScheduleFacade prodSched)
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
	}
	
	/** The AlgorithmStrategyFactory of this Manufacturer. */
	private final AlgorithmStrategyFactory algorithmStrategyFactory;
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
			return this.getAssemblyLine().getEstimatedCompletionTime(order);
		if(this.getCompletedOrderCatalog().contains(order))
			return this.getCompletedOrderCatalog().getCompletionTime(order);
		throw new OrderDoesNotExistException("Order was not found in the system.");
	}
	
	//--------------------------------------------------------------------------
	// Model and new order related methods and variables
	//--------------------------------------------------------------------------
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
		if(option == null)
			throw new IllegalArgumentException("Option should not be null.");
		if(deadline == null)
			throw new IllegalArgumentException("Option should not be null.");
		if(!this.singleTaskCatalogContains(option))
			throw new IllegalArgumentException("Option is not a singleTaskOption.");
		this.getProductionSchedule().submitSingleTaskOrder(option, deadline);
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
	 * 		If given option is not present in the singelTaskCatalog
	 */
	private boolean singleTaskCatalogContains(Option option) throws IllegalArgumentException{
		if(option == null)
			throw new IllegalArgumentException("Option can not be null.");
		return this.singleTaskCatalog.contains(option);
	}
	
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
	//TODO Is dit systeem van booleans en exceptions acceptabel?
	// Exceptions voor abnormale cases: Null, en illegale model-option-combo
	// Boolean return voor restrictions al dan niet ok.
	private boolean checkOrderValidity(Model model, List<Option> options)
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
	public Order submitStandardOrder(Model model, List<Option> options)
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
		return this.getProductionSchedule().submitStandardOrder(model, orderSpecs);
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
	// ProductionScheduleFacade related variables and methods. 
	//--------------------------------------------------------------------------
	/** Interface into production schedule functionality. */
	private ProductionScheduleFacade productionScheduleFacade;

	/**
	 * Get the ProductionSchedule of this Manufacturer.
	 * 
	 * @return The ProductionSchedule of this Manufacturer. 
	 */
	ProductionScheduleFacade getProductionSchedule() {
		return this.productionScheduleFacade;
	}

	/**
	 * Set the association with the ProductionSchedule. 
	 * 
	 * @param productionSchedule
	 * 		The new ProductionSchedule of this Manufacturer. 
	 * 
	 * @post | (new this).getProductionSchedule == productionSchedule
	 */
	void setProductionSchedule(ProductionScheduleFacade productionScheduleFacade) {
		this.productionScheduleFacade = productionScheduleFacade;
	}

	/**
	 * Remove an Order from this Manufacturer's ProductionSchedule and
	 * pass it along.
	 * 
	 * @return The removed order
	 * @throws IllegalStateException
	 * 		See {@link ProductionScheduleFacade#orderAvailable() orderAvailable()}
	 */
	public Order popNextOrderFromSchedule() throws IllegalStateException {
		return this.getProductionSchedule().popNextOrderFromSchedule();
	}
	
	/**
	 * Ask this Manufacturer's ProductionSchedule if an order is available
	 * 
	 * @return Whether an order is available
	 */
	public boolean orderAvailable() {
		return this.getProductionSchedule().orderAvailable();
	}

	/**
	 * Get a list of pending {@link OrderContainer}s in the productionSchedule.
	 * 
	 * @return List of pending order containers in the productionSchedule.
	 */
	private List<OrderContainer> getSchedulePendingOrderContainers() {
		return this.getProductionSchedule().getPendingOrdersContainers();
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
	/**
	 * Get the AlgorithmFactory of this Manufacturer
	 * 
	 * @return the AlogorithmFactory of this Manufacturer. 
	 */
	public AlgorithmStrategyFactory getAlgorithmFactory() {
		throw new UnsupportedOperationException();
	}

	public void setNewSchedulingAlgorithm(Comparator<Order> comparator) {
		throw new UnsupportedOperationException();
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