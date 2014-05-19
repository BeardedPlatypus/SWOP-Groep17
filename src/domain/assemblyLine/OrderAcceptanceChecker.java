package domain.assemblyLine;

import java.util.Arrays;
import java.util.List;

import domain.car.Model;
import domain.order.Order;

/**
 * Helper class for the production schedule to determine whether a scheduled
 * order can be placed on a specific AssemblyLine.
 * 
 * @author Thomas Vochten
 *
 */
public class OrderAcceptanceChecker {
	
	/**
	 * Initialise a new OrderSelector.
	 * 
	 * @param models
	 * 		The models the new OrderSelector must accept.
	 */
	public OrderAcceptanceChecker(List<Model> models) {
		this.allowedModels = models;
	}
	
	/**
	 * Initialise a new OrderSelector
	 * 
	 * @param models
	 * 		The models the new OrderSelector must accept.
	 */
	public OrderAcceptanceChecker(Model... models) {
		this(Arrays.asList(models));
	}
	
	/** The models that this OrderSelector accepts */
	private List<Model> allowedModels;
	
	/**
	 * Get the models that this OrderSelector accepts
	 * 
	 * @return The models
	 */
	private List<Model> getAllowedModels() {
		return this.allowedModels;
	}
	
	/**
	 * Determine whether this OrderSelector accepts the specified Order
	 * 
	 * @param order
	 * 		The order of interest.
	 * @return The order is accepted
	 */
	public boolean accepts(Order order) {
		return this.getAllowedModels().contains(order.getModel());
	}
}
