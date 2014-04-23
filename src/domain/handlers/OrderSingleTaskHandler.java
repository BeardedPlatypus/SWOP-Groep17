package domain.handlers;

import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.Option;
import domain.OptionCategory;
import domain.SingleOrderSession;
import domain.order.Order;
import domain.order.OrderContainer;
import exceptions.OrderDoesNotExistException;

/**
 * Coordinates with the outside world in order to place a new single task order.
 * 
 * @author Simon Slangen
 */
public class OrderSingleTaskHandler {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialize this OrderSingleTaskHandler with the specified Manufacturer.
	 * 
	 * @param manufacturer
	 */
	public OrderSingleTaskHandler(Manufacturer manufacturer){
		this.manufacturer = manufacturer;
	}

	//--------------------------------------------------------------------------
	// Attributes (& private getters)
	//--------------------------------------------------------------------------
	private Manufacturer manufacturer;
	private SingleOrderSession currentOrderSession = null;
	
	/**
	 * Returns the manufacturer coupled with this OrderSingleTaskHandler object.
	 */
	private Manufacturer getManufacturer(){
		return manufacturer;
	}
	
	/**
	 * Returns the active SingleOrderSession object if it exists. Null otherwise.
	 */
	private SingleOrderSession getCurrentOrderSession(){
		return currentOrderSession;
	}
	
	//--------------------------------------------------------------------------
	// Order methods.
	//--------------------------------------------------------------------------
	/**
	 * Starts a new order session.
	 */
	public void startNewOrderSession() {
		currentOrderSession = getManufacturer().startNewSingleTaskOrderSession();
	}

	/**
	 * Returns a list of option categories, containing the possible tasks to be ordered
	 * in a single task order.
	 * 
	 * @return	List of option categories.
	 * @throws 	IllegalStateException
	 *			If there is no active new order session.
	 */
	public List<OptionCategory> getPossibleTasks() throws IllegalStateException {
		if(isRunningOrderSession()){
			return getCurrentOrderSession().getPossibleTasks();
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Selects an option to be ordered as a single task.
	 * 
	 * @param 	option
	 * 			The option to be ordered in this single task.
	 * @throws IllegalStateException
	 *			If there is no active new order session.
	 */
	public void selectOption(Option option) throws IllegalStateException {
		if(isRunningOrderSession()){
			getCurrentOrderSession().selectOption(option);
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Sets the deadline for this single task order to be within the given number of days, hours and minutes.
	 * 
	 * @param	days
	 * 			The number of days before the deadline.
	 * @param	hours
	 * 			The number of hours before the deadline modulo days.
	 * @param	minutes
	 * 			The number of minutes before the deadline modulo hours.
	 * @throws 	IllegalStateException
	 *			If there is no active new order session.
	 */
	public void specifyDeadline(int days, int hours, int minutes) throws IllegalStateException {
		if(isRunningOrderSession()){
			getCurrentOrderSession().specifyDeadline(days, hours, minutes);
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Submits the single task order that's been composed in the active order session and schedules it for completion
	 * Returns the order that's been scheduled.
	 * 
	 * @post	isRunningOrderSession() == false
	 * @return	The submitted order.
	 * @throws 	IllegalStateException
	 *			If there is no active new order session.
	 */
	public OrderContainer submitSingleTaskOrder() throws IllegalStateException {
		if(isRunningOrderSession()){
			OrderContainer submittedOrder = getCurrentOrderSession().submitSingleTaskOrder();
			currentOrderSession = null;
			return submittedOrder;
		} else {
			throw new IllegalStateException("No active order session.");
		}
	}

	/**
	 * Returns the estimated completion time for the given order.
	 * 
	 * @param 	order
	 * 			The order of which to poll the estimated completion time.
	 * @return	The estimated completion time for the given order.
	 * @throws  OrderDoesNotExistException 
	 * 			If the order does not exist.
	 */
	public DateTime getEstimatedCompletionTime(OrderContainer order) throws  OrderDoesNotExistException {
		return getManufacturer().getEstimatedCompletionTime(order);
	}
	
	/**
	 * Checks whether there is an active single order session.
	 * 
	 * @return	true if there is an active single order session
	 * 			false otherwise
	 */
	public boolean isRunningOrderSession(){
		if(getCurrentOrderSession() == null){
			return false;
		} else {
			return true;
		}
	}
}