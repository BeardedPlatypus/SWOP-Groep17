package domain.handlers;

import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.Model;
import domain.Option;
import domain.OptionCategory;
import domain.OrderSession;
import domain.order.OrderContainer;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;

/**
 * Coordinates with the outside world in order to place a new order.
 * 
 * @author Simon Slangen, Frederik Goovaerts
 */
public class NewOrderSessionHandler {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Create a new NewOrderSessionHandler with given manufacturer, and no legal
	 * active order session.
	 * 
	 * @param man
	 * 		The manufacturer this Handler will interface with
	 * 
	 * @throws IllegalArgumentException
	 * 		If the given manufacturer is null
	 */
	public NewOrderSessionHandler(Manufacturer man) throws IllegalArgumentException {
		if (man == null)
			throw new IllegalArgumentException("Cannot initialise new order "
					+ "session handler with non-existent manufacturer.");
		this.manufacturer = man;
		this.currentOrderSession = null;
	}
	
	//--------------------------------------------------------------------------
	// Attributes (& private getters)
	//--------------------------------------------------------------------------
	
	/**
	 * Get the Handler's manufacturer for internal use
	 * 
	 * @return the manufacturer of this class
	 */
	private Manufacturer getManufacturer(){
		return manufacturer;
	}
	
	/**
	 * Get the Handler's active order session for internal use
	 * 
	 * @return the active order session of this class
	 */
	private OrderSession getCurrentOrderSession(){
		return currentOrderSession;
	}
	
	/**	The manufacturer this Handler interfaces with to make new orders */
	private final Manufacturer manufacturer;
	
	/**
	 * The orderSession object this class interfaces with to assemble the necessary
	 * components for a new order
	 */
	private OrderSession currentOrderSession;
	
	//--------------------------------------------------------------------------
	// Getters
	//--------------------------------------------------------------------------
	/**
	 * Get the system's pending orders. This includes the orders in the production
	 * schedule, queueing to be assembled, as well as the orders which are active
	 * on the assembly line.
	 * 
	 * @return the list of pending orders in the system
	 */
	public List<OrderContainer> getPendingOrders() {
		return this.getManufacturer().getPendingOrderContainers();
	}

	/**
	 * Get the system's completed orders. This encompasses the orders which were
	 * previously assembled and are now marked as complete.
	 * 
	 * @return the list of completed orders in the system
	 */
	public List<OrderContainer> getCompletedOrders() {
		return this.getManufacturer().getCompletedOrderContainers();
	}

	//--------------------------------------------------------------------------
	// New order session methods
	//--------------------------------------------------------------------------
	/**
	 * Start a new order session.
	 * 
	 * @post this.getCurrentOrderSession() == getManufacturer().startNewOrderSession()
	 */
	public void startNewOrderSession() {
		currentOrderSession = getManufacturer().getNewOrderSession();
	}
	
	/**
	 * Return a list of available car models.
	 * 
	 * @return List of available car models.
	 * 
	 * @throws IllegalStateException
	 *		If there is no active new order session.
	 */
	public List<Model> getCarModels() throws IllegalStateException {
		if(!isRunningNewOrderSession())
			throw new IllegalStateException("No active order session.");
		return getCurrentOrderSession().getCarModels();
	}
	/**
	 * Check to see if we're currently running a new order session.
	 * Initially false, this becomes true after the user has started
	 * a new order.
	 * 
	 * @return	true if a new order has been started, false otherwise
	 */
	public boolean isRunningNewOrderSession() {
		if(getCurrentOrderSession() == null){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Select the model passed as the argument in the active new order session.
	 * 
	 * @param model
	 * 		The car model to create an order for.
	 * @throws IllegalStateException
	 *		If there is no active new order session.
	  @throws IllegalArgumentException
	 * 		When the given model is not a model of the system or null
	 * @throws IllegalStateException
	 * 		When a model was already chosen for the current OrderSession
	 */
	public void chooseModel(Model model) throws IllegalStateException {
		if(!isRunningNewOrderSession())
			throw new IllegalStateException("No active order session.");
		getCurrentOrderSession().chooseModel(model);
	}

	/**
	 * Return the next option category to the caller.
	 * 
	 * @return  The next option category.
	 * 
	 * @throws IllegalStateException
	 *		If there is no active new order session.
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no optionCategories are unfilled anymore
	 */
	public OptionCategory getNextOptionCategory()
			throws IllegalStateException,
			NoOptionCategoriesRemainingException
	{
		if(!isRunningNewOrderSession())
			throw new IllegalStateException("No active order session.");
		return getCurrentOrderSession().getNextOptionCategory();
	}

	/**
	 * Select the option passed as the argument in the active new order session.
	 * 
	 * @param option
	 * 		An option to add to the order.
	 * 
	 * @throws IllegalStateException
	 *		If there is no active new order session.
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no optionCategories are unfulfilled anymore
	 */
	public void selectOption(Option option) throws IllegalStateException, NoOptionCategoriesRemainingException{
		if(!isRunningNewOrderSession())
			throw new IllegalStateException("No active order session.");
		getCurrentOrderSession().addOption(option);
	}

	/**
	 * Submit the order composed by the active new order session, and return
	 * the resulting order as a container.
	 * 
	 * @throws IllegalStateException
	 *		If there is no active new order session.
	 * @throws IllegalStateException
	 * 		If no model has been chosen
	 * @throws IllegalStateException
	 * 		If there are unfulfilled OptionCategories
	 * @throws IllegalCarOptionCombinationException 
	 * 		When the chosen options are not valid with given model
	 * @throws OptionRestrictionException
	 * 		When the set of options does not meet the system's restrictions
	 * @throws IllegalStateException
	 * 		When the order was already submitted
	 */
	public void submitOrder() throws IllegalStateException, IllegalArgumentException, IllegalCarOptionCombinationException, OptionRestrictionException {
		if(!isRunningNewOrderSession())
			throw new IllegalStateException("No active order session.");
		getCurrentOrderSession().submitOrder();
	}

	/**
	 * Check whether or not the current orderSession has unfilled options.
	 * 
	 * @return whether or not the current orderSession has unfilled options.
	 * @throws IllegalStateException
	 * 		When no orderSession is active
	 */
	public boolean hasUnfilledOptions() throws IllegalStateException {
		if(!isRunningNewOrderSession())
			throw new IllegalStateException("No active order session.");
		return getCurrentOrderSession().hasUnfilledOptions();
	}

	/**
	 * When a new order has been constructed with a session, this allows the user
	 * to query the system for the ETA of the new order.
	 * 
	 * @return the ETA of the order made with the current session
	 * 
	 * @throws IllegalStateException
	 * 		If the handler has no active session or the session has not created
	 * 		an order in the system.
	 * @throws OrderDoesNotExistException
	 * 		If the order of the session is not recognized in the system
	 */
	public DateTime getNewOrderETA() throws IllegalStateException, OrderDoesNotExistException{
		if(!this.isRunningNewOrderSession())
			throw new IllegalStateException("No orderSession is running.");
		return this.getCurrentOrderSession().getETA();
	}
}