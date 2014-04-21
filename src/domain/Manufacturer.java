package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import domain.Order;
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
	private ModelCatalog modelCatalog;
	private AlgorithmStrategyFactory algorithmStrategyFactory;
	private OptionRestrictionManager optionRestrictionManager;
	private SingleTaskCatalog singleTaskCatalog;
	

	

	public void getCompletedOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public void getCarModels() {
		throw new UnsupportedOperationException();
	}

	public OrderSession getNewOrderSession() {
		throw new UnsupportedOperationException();
	}

	public void checkOrderValidity(Model model, Option options) {
		throw new UnsupportedOperationException();
	}

	public void submitStandardOrder(Model model, Option options) {
		throw new UnsupportedOperationException();
	}

	public void startNewOrderSession() {
		throw new UnsupportedOperationException();
	}

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
	
	public void getPendingOrderContainers() {
		throw new UnsupportedOperationException();
	}
	
	public Order popNextOrderFromSchedule() {
		return this.getProductionSchedule().popNextOrderFromSchedule();
	}
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
