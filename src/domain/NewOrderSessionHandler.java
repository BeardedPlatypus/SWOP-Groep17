package domain;

import java.util.List;

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
	
	/** The manufacturer of this NewOrderSessionHandler */
	private Manufacturer manufacturer;
	
	private Manufacturer getManufacturer() {
		return manufacturer;
	}

	public List<OrderContainer> getCompletedOrders() {
		return getManufacturer().getCompletedOrderContainers();
	}

	public List<OrderContainer> getPendingOrders() {
		return getManufacturer().getPendingOrderContainers();
	}

	public void startNewOrder() {
		throw new UnsupportedOperationException();
	}

	public void chooseModelAndOptions(Object model, Object specifications) {
		throw new UnsupportedOperationException();
	}
}