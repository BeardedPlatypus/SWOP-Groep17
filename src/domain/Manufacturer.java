package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which represents the book-keeping body of the system.
 * This class keeps the completed orders and hands the UI information about which models can be ordered.
 * It passes information for a new order to the productionSchedule, which instantiates new orders.
 * 
 * @author Martinus Wilhelmus Tegelaers, Frederik Goovaerts
 *
 */
public class Manufacturer {
	
	/**
	 * The ProductionSchedule this manufacturer uses for scheduling orders.
	 */
	private final ProductionSchedule productionSchedule;
	
	/**
	 * A list this class keeps of orders the assembly system has completed in the past.
	 */
	private List<Order> completedOrders = new ArrayList<Order>();

	/**
	 * A ModelCatalog which contains the models this manufacturer is able to produce.
	 */
	private final ModelCatalog modelCatalog;

	/**
	 * Constructor
	 * 
	 * Instantiates an object of the class with a given productionSchedule and modelCatalog.
	 * 
	 * @param productionSchedule
	 * 		The productionSchedule this manufacturer will use to schedule its orders for assembly
	 * @param modelCatalog
	 * 		The modelCatalog of the manufacturer which contains the models this manufacturer can assemble.
	 * @throws IllegalArgumentException
	 * 		When either of the arguments is null.
	 */
	public Manufacturer(ProductionSchedule productionSchedule, ModelCatalog modelCatalog) throws IllegalArgumentException{
		if(productionSchedule == null)
			throw new IllegalArgumentException("productionSchedule is not allowed to be null when creating a new Manufacturer.");
		if(modelCatalog == null)
			throw new IllegalArgumentException("modelCatalog is not allowed to be null when creating a new Manufacturer.");
		this.productionSchedule = productionSchedule;
		this.modelCatalog = modelCatalog;
	}
	
	/**
	 * A getter for the productionSchedule for internal use
	 * 
	 * @return
	 * 		the productionSchedule of this class
	 */
	private ProductionSchedule getProductionSchedule() {
		return this.productionSchedule;
	}
	
	/**
	 * A getter for the modelCatalog for internal use
	 * 
	 * @return
	 * 		the modelCatalog of this class
	 */
	private ModelCatalog getModelCatalog() {
		return modelCatalog;
	}
	
	/**
	 * A getter for the list of completed orders for internal use
	 * 
	 * @return
	 * 		the list of completed orders
	 */
	private List<Order> getCompletedOrders() {
		return completedOrders;
	}

	/**
	 * Returns a list of containers for the completed orders for use by the UI.
	 * 
	 * @return
	 * 		The list of completed orders as containers
	 */
	public List<OrderContainer> getCompletedOrderContainers() {
		return new ArrayList<OrderContainer>(completedOrders);
	}

	/**
	 * Returns the containers of the pending orders from the productionSchedule
	 * 
	 * @return
	 * 		A list with containers for the pending orders in the schedule
	 */
	public List<OrderContainer> getPendingOrderContainers() {
		return this.getProductionSchedule().getPendingOrderContainers();
	}

	/**
	 * Gets the available models from the modelCatalog for use by the UI
	 * 
	 * @return
	 * 		A list of the available models in the modelCatalog
	 */
	public List<Model> getModels() {
		return this.getModelCatalog().getModels();
	}

	/**
	 * Passes the information for a new order from the UI to the production schedule where an order will be created
	 * 
	 * @param model
	 * 		The chosen model for the new order
	 * @param specifications
	 * 		The chosen specifications for the model for the new order
	 * @throws IllegalArgumentException
	 * 		When either of the arguments is null.
	 */
	public void createOrder(Model model, Specification specifications) throws IllegalArgumentException{
		if(model == null)
			throw new IllegalArgumentException("model is not allowed to be null when creating a new Order.");
		if(specifications == null)
			throw new IllegalArgumentException("modelCatalog is not allowed to be null when creating a new Order.");
		this.getProductionSchedule().addNewOrder(model, specifications);
	}

	/**
	 * Adds an order to the list of completed orders.
	 * This method expects the order to already have been set as completed with a completion date.
	 * 
	 * @param order
	 * 		The order to add to the completed orders list
	 * @throws IllegalStateException
	 * 		If the order is not yet set as completed
	 */
	public void addCompleteOrder(Order order) throws IllegalStateException{
		if(!order.isCompleted())
			throw new IllegalStateException("Order is not yet completed, can not add it to completed orders.");
		this.getCompletedOrders().add(order);
	}
}
