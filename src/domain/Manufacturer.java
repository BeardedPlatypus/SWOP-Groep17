package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import domain.Order;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.OptionRestrictionException;

//TODO everything

/**
 * A class which represents the book-keeping body of the system.
 * This class keeps the completed orders and hands the UI information about which models can be ordered.
 * It passes information for a new order to the productionSchedule, which instantiates new orders.
 * 
 * @author Martinus Wilhelmus Tegelaers, Frederik Goovaerts
 */
public class Manufacturer {
	private AlgorithmStrategyFactory algorithmStrategyFactory;
	private SingleTaskCatalog singleTaskCatalog;

	public void submitSingleTaskOrder(Option option, DateTime deadline) {
		throw new UnsupportedOperationException();
	}

	public void getEstimatedCompletionTime(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public void getTaskContainersAtWorkPost(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public void completeTask(Object parameter, Object parameter2, Object parameter3) {
		throw new UnsupportedOperationException();
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
	public List<OrderContainer> getPendingOrderContainers() {
		List<OrderContainer> pending = this.getAssemblingPendingOrderContainers();
		pending.addAll(this.getSchedulePendingOrderContainers());
		return pending;
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
	private OptionRestrictionManager optionRestrictionManager;

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
		if(!model.checkOptionsValdity(options))
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
		return this.getProductionSchedule().submitStandardOrder(model, options);
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

	public Order popNextOrderFromSchedule() {
		return this.getProductionSchedule().popNextOrderFromSchedule();
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

	// TODO: completion time stamp must be set for the order
	public void addToCompleteOrders(Order order) {
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
	public String getStatisticsReport() {
		return this.getAssemblyLine().getStatisticsReport();
	}


}