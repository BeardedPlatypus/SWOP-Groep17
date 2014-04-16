package domain;

import java.util.ArrayList;
import java.util.Comparator;

import domain.Order;

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
	private AssemblyLine assemblyLine;
	private CompletedOrderCatalog completeOrderCatalog;
	private AlgorithmStrategyFactory algorithmStrategyFactory;
	private OptionRestrictionManager optionRestrictionManager;
	private SingleTaskCatalog singleTaskCatalog;
	private ProductionScheduleFacade productionScheduleFacade;

	public void getPendingOrderContainers() {
		throw new UnsupportedOperationException();
	}

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

	public void getWorkPostContainers() {
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
	/**
	 * Get the AssemblyLine of this Manufacturer.
	 * 
	 * @return The AssemblyLine
	 */
	private AssemblyLine getAssemblyLine() {
		return this.assemblyLine;
	}
	
	//--------------------------------------------------------------------------
	// ProductionScheduleFacade related variables and methods. 
	//--------------------------------------------------------------------------
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