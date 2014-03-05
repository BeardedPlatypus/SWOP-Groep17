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
	
	public List<OrderContainer> getCompletedOrders() {
		throw new UnsupportedOperationException();
	}

	public List<OrderContainer> getPendingOrders() {
		return this.manufacturer.getPendingOrderContainers();
	}

	public void startNewOrder() {
		throw new UnsupportedOperationException();
	}

	public void chooseModelAndOptions(Object model, Object specifications) {
		throw new UnsupportedOperationException();
	}
}