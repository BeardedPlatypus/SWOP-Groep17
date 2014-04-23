package domain.productionSchedule;

import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.Model;
import domain.Option;
import domain.order.OrderContainer;

/** 
 * The ProductionScheduleFacade provides an interface to the ProductionSchedule
 * subsystem. It provides the basic methods the rest of the domain can use to 
 * interact with this ProductionSchedule.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class ProductionScheduleFacade {
	//--------------------------------------------------------------------------
	// Manufacturer
	//--------------------------------------------------------------------------
	//TODO initialisation stuff: set manufacturer properly
	/** 
	 * Get the Manufacturer that owns this ProductionScheduleFacade. 
	 * 
	 * @return The Manufacturer that owns this ProductionScheduleFacade.
	 */
	private Manufacturer getManufacturer() {
		return this.manufacturer;
	}
	
	/** The manufacturer that owns this ProductionScheduleFacade. */
	private Manufacturer manufacturer; 
	
	//--------------------------------------------------------------------------
	// Get methods.
	//--------------------------------------------------------------------------
	/**
	 * Get the pending orders of this ProductionSchedule. 
	 * 
	 * @return The pending orders of this ProductionSchedule.
	 * 	       | ALL o: o == OrderContainer && o in this 
	 */
	public List<OrderContainer> getPendingOrdersContainers() {
		throw new UnsupportedOperationException();
	}

	
	public void submitStandardOrder(Model model, Option options) {
		throw new UnsupportedOperationException();
	}

	/** 
	 * Get the estimated completion time of the specified order in this ProductionSchedule.
	 * 
	 * @param order
	 * 		The specified order of which the estimated completion time is requested.
	 * 
	 * @return The estimated completion time of the specified order.
	 * 
	 * @throws IllegalArgumentException
	 * 		| order == null || !this.contains(order)
	 */
	public DateTime getEstimatedCompletionTime(OrderContainer order) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Check if the specified order is an element of this ProductionSchedule.
	 * 
	 * @param order
	 * 		The Specified order that should be checked.
	 * 
	 * @return | order in this
	 * 
	 * @throws IllegalArgumentException
	 * 		| order == null
	 */
	public boolean contains(OrderContainer order) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}