package domain;

import java.util.List;

import com.sun.media.sound.ModelConnectionBlock;

public class NewOrderSessionHandler {
	/* -------------------------------------------------------------------------
	 * Constructors
	 * -----------------------------------------------------------------------*/
	/**
	 * 
	 * @param manufacturer 
	 * 		The manufacturer of this NewOrderSessionHandler. 
	 * 
	 * @precondition
	 * 		| manufacturer != null
	 * 
	 * @postcondition
	 * 		| (new this).manufacturer == manufacturer
	 */
	public NewOrderSessionHandler(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	/**
	 * The manufacturer of this NewOrderSessionHandler
	 */
	private Manufacturer manufacturer;
	
	/**
	 * A getter for the manufacturer for internal use
	 * 
	 * @return
	 * 		the manufacturer in this class
	 */
	private Manufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * Gets a list of completed orders in the system from the manufacturer
	 * 
	 * @return
	 * 		a list of completed orders in the system
	 * 		
	 */
	public List<OrderContainer> getCompletedOrders() {
		return getManufacturer().getCompletedOrderContainers();
	}

	/**
	 * Gets a list of pending orders in the system from the manufacturer
	 * 
	 * @return
	 * 		a list of pending orders in the system
	 * 		
	 */
	public List<OrderContainer> getPendingOrders() {
		return getManufacturer().getPendingOrderContainers();
	}

	/**
	 * Indicates the user wants to order a car, so returns a list with the possible models the
	 * manufacturer is able to make, and their respective options (embedded).
	 * These models are immutable objects.
	 * 
	 * @return
	 * 		The list of possible models to choose from, available in the system
	 */
	public List<Model> getNewOrderModels() {
		return this.getManufacturer().getModels();
	}

	/**
	 * Sends the chosen model and specifications from the UI to the manufacturer for registering a new order in the system
	 * 
	 * @param model
	 * 		The chosen model for the new order
	 * @param specifications
	 * 		The chosen model specifications for the new order
	 */
	public void chooseModelAndSpecifications(Model model, Specification specifications) {
		this.getManufacturer().createOrder(model, specifications);
	}
}