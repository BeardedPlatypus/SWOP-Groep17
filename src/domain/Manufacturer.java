package domain;

import java.util.ArrayList;
import domain.Order;

/**
 * A class which represents the book-keeping body of the system.
 * This class keeps the completed orders and hands the UI information about which models can be ordered.
 * It passes information for a new order to the productionSchedule, which instantiates new orders.
 * 
 * @author Martinus Wilhelmus Tegelaers, Frederik Goovaerts
 */
public class Manufacturer {
	public ProductionSchedule productionSchedule;
	private ArrayList<Order> attribute2 = new ArrayList<Order>();
	private ModelCatalog modelCatalog;
	public AssemblyLine assemblyLine;
	public CompleteOrderCatalog completeOrderCatalog;
	public AlgorithmStrategyFactory algorithmStrategyFactory;
	public OptionRestrictionManager unnamed_OptionRestrictionManager_;
	public SingleTaskCatalog singleTaskCatalog;

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

	public void getAlgorithmFactory() {
		throw new UnsupportedOperationException();
	}

	public void setNewSchedulingAlgorithm(Comparator comparator) {
		throw new UnsupportedOperationException();
	}

	public void setProductionSchedule(ProductionSchedule productionSchedule) {
		this.productionSchedule = productionSchedule;
	}
}