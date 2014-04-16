package domain;

import java.util.List;

/**
 * Coordinates with the outside world in order to place a new order.
 * 
 * @author Simon Slangen
 */
public class NewOrderSessionHandler {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	public NewOrderSessionHandler(Manufacturer man) throws IllegalArgumentException {
		if (manufacturer == null) {
			throw new IllegalArgumentException("Cannot initialise new order session handler with non-existent manufacturer.");
		}
		this.manufacturer = man;
		this.currentOrderSession = null;
	}
	
	//--------------------------------------------------------------------------
	// Attributes (& private getters)
	//--------------------------------------------------------------------------
	private Manufacturer manufacturer;
	private OrderSession currentOrderSession;
	
	private Manufacturer getManufacturer(){
		return manufacturer;
	}
	
	private OrderSession getCurrentOrderSession(){
		return currentOrderSession;
	}
	
	//--------------------------------------------------------------------------
	// Getters
	//--------------------------------------------------------------------------
	/**
	 * Returns a list of pending orders.
	 * 
	 * @return List of pending orders.
	 */
	public List<OrderContainer> getPendingOrders() {
		return getManufacturer().getPendingOrderContainers();
	}

	/**
	 * Returns a list of completed orders.
	 * 
	 * @return List of completed orders.
	 */
	public void getCompletedOrders() {
		return getManufacturer().getCompletedOrderContainers();
	}

	//--------------------------------------------------------------------------
	// New order session methods
	//--------------------------------------------------------------------------
	/**
	 * Returns a list of available car models.
	 * 
	 * @return List of available car models.
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public List<Model> getCarModels() throws IllegalStateException {
		if(!isRunningNewOrderSession()){
			throw new IllegalStateException("No active order session.");
		} else {
			return getCurrentOrderSession().getCarModels();
		}
	}

	/**
	 * Starts a new order session.
	 */
	public void startNewOrderSession() {
		currentOrderSession = getManufacturer().startNewOrderSession();
	}
	
	/**
	 * Checks to see if we're currently running a new order session.
	 * Initially false, this becomes true after the user has started
	 * a new order.
	 * 
	 * @return	true if a new order has been started.
	 * 			false otherwise
	 */
	public boolean isRunningNewOrderSession() {
		if(getCurrentOrderSession() == null){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Selects the model passed as the argument in the active new order session.
	 * 
	 * @param 	model
	 * 			The car model to create an order for.
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public void chooseModel(Model model) throws IllegalStateException {
		if(isRunningNewOrderSession()){
			getCurrentOrderSession().chooseModel(model);
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Returns the next option category to the caller.
	 * 
	 * @return  The next option category.
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public OptionCategory getNextOptionCategory() throws IllegalStateException {
		if(isRunningNewOrderSession()){
			return getCurrentOrderSession().getNextOptionCategory();
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Selects the option passed as the argument in the active new order session.
	 * 
	 * @param option
	 * 			An option to add to the order.
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public void selectOption(Option option) throws IllegalStateException {
		if(isRunningNewOrderSession()){
			getCurrentOrderSession().selectOption(option);
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Submits the order composed by the active new order session.
	 * 
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public void submitOrder() throws IllegalStateException {
		if(isRunningNewOrderSession()){
			getCurrentOrderSession().submitOrder();
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}
}