package domain;

import java.util.List;

import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;

/**
 * Facade on all handlers, so the UI only needs one object to interface with.
 * 
 * @author Frederik Goovaerts
 */
public class DomainFacade {
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Construct a domainfacade with all given handlers as fields.
	 * 
	 * @param performHandler
	 * 		The new PerformAssemblyTaskHandler for this facade.
	 * @param newOrderHandler
	 * 		The new NewOrderSessionHandler for this facade.
	 * @param singleTaskHandler
	 * 		The new OrderSingleTaskHandler for this facade.
	 * @param orderDetailsHandler
	 * 		The new OrderDetailsHandler for this facade.
	 * @param algorithmHandler
	 * 		The new SchedulingAlgorithmHandler for this facade.
	 * @throws IllegalArgumentException
	 * 		If any of the given handlers is null.
	 */
	public DomainFacade(PerformAssemblyTaskHandler performHandler,
					    NewOrderSessionHandler newOrderHandler,
					    OrderSingleTaskHandler singleTaskHandler,
					    OrderDetailsHandler orderDetailsHandler,
					    SchedulingAlgorithmHandler algorithmHandler)
	{
		if(performHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(newOrderHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(singleTaskHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(orderDetailsHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		if(algorithmHandler == null)
			throw new IllegalArgumentException("Handler should not be null!");
		this.performAssemblyTaskHandler = performHandler;
		this.newOrderSessionHandler = newOrderHandler;
		this.orderSingleTaskHandler = singleTaskHandler;
		this.orderDetailsHandler = orderDetailsHandler;
		this.schedulingAlgorithmHandler = algorithmHandler;
	}
	
	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------
	
	/**
	 * Get the PerformAssemblyTaskHandler for internal use.
	 * 
	 * @return the performAssemblyTaskHandler
	 */
	private PerformAssemblyTaskHandler getPerformAssemblyTaskHandler() {
		return performAssemblyTaskHandler;
	}

	/**
	 * Get the {@link NewOrderSessionHandler} for internal use.
	 * 
	 * @return the newOrderSessionHandler
	 */
	private NewOrderSessionHandler getNewOrderSessionHandler() {
		return newOrderSessionHandler;
	}

	/**
	 * Get the OrderSingleTaskHandler for internal use.
	 * 
	 * @return the orderSingleTaskHandler
	 */
	private OrderSingleTaskHandler getOrderSingleTaskHandler() {
		return orderSingleTaskHandler;
	}

	/**
	 * Get the OrderDetailsHandler for internal use.
	 * 
	 * @return the orderDetailsHandler
	 */
	private OrderDetailsHandler getOrderDetailsHandler() {
		return orderDetailsHandler;
	}

	/**
	 * Get the SchedulingAlgorithmHandler for internal use.
	 * 
	 * @return the schedulingAlgorithmHandler
	 */
	private SchedulingAlgorithmHandler getSchedulingAlgorithmHandler() {
		return schedulingAlgorithmHandler;
	}	
	
	/**	The facade's PerformAssemblyTaskHandler */
	private final PerformAssemblyTaskHandler performAssemblyTaskHandler;

	/**	The facade's NewOrderSessionHandler */
	private final NewOrderSessionHandler newOrderSessionHandler;

	/**	The facade's OrderSingleTaskHandler */
	private final OrderSingleTaskHandler orderSingleTaskHandler;

	/**	The facade's OrderDetailsHandler */
	private final OrderDetailsHandler orderDetailsHandler;

	/**	The facade's SchedulingAlgorithmHandler */
	private final SchedulingAlgorithmHandler schedulingAlgorithmHandler;
	
	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------

	
	public void getAlgorithms() {
		throw new UnsupportedOperationException();
	}

	public void getCurrentScheduleAlgorithm() {
		throw new UnsupportedOperationException();
	}

	public void setFifoAlgorithm() {
		throw new UnsupportedOperationException();
	}
	
	 
	//-------------------------------------------------------------------------
	// Order New Car Methods

	/**
	 * Get the system's pending orders. This includes the orders in the production
	 * schedule, queueing to be assembled, as well as the orders which are active
	 * on the assembly line.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @return the list of pending orders in the system
	 */
	public List<OrderContainer> getPendingOrders(){
		return this.getNewOrderSessionHandler().getPendingOrders();
	}
	
	/**
	 * Get the system's completed orders. This encompasses the orders which were
	 * previously assembled and are now marked as complete.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @return the list of completed orders in the system
	 */
	public List<OrderContainer> getCompletedOrders(){
		return this.getNewOrderSessionHandler().getCompletedOrders();
	}
	
	/**
	 * Start a new orderSession in the newOrderSessionHandler, so a user can
	 * put together an order from scratch. This resets all previously chosen
	 * model and options in the Handler.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @effect this.getNewOrderSessionHandler().startNewOrderSession()
	 * 
	 */
	public void startNewOrderSession(){
		this.getNewOrderSessionHandler().startNewOrderSession();
	}
	
	/**
	 * Get a list of all possible Car Models for construction by the system.
	 * 
	 * @pre this.getNewOrderSessionHandler() != null
	 * 
	 * @return a list of all possible Car Models for the system
	 */
	public List<Model> getCarModels(){
		return this.getNewOrderSessionHandler().getCarModels();
	}
	
	/**
	 * Choose the model for the current OrderSession active in the handler.
	 * If a model was previously chosen, and a new session hasn't been started
	 * yet, an exception will occur. If the model is not a model of the system,
	 * an exception will occur.
	 * 
	 * @param model
	 * 		The model the user wants to set for the OrderSession
	 * @throws IllegalArgumentException
	 * 		When the given model is not a model of the system or null
	 * @throws IllegalStateException
	 * 		When a model was already chosen for the current OrderSession
	 * @throws IllegalStateException
	 * 		When there is no active OrderSession
	 */
	public void chooseModel(Model model) throws IllegalArgumentException, IllegalStateException{
		if(model == null)
			throw new IllegalArgumentException("Model can not be null.");
		this.getNewOrderSessionHandler().chooseModel(model);
	}
	
	/**
	 * Check whether or not all choices have been made for the order specification
	 * 
	 * @return whether or not all choices have been made
	 */
	public boolean orderHasUnfilledOptions(){
		return this.getNewOrderSessionHandler().hasUnfilledOptions();
	}
	
	/**
	 * Get the next OptionCategory of which no option has been chosen yet.
	 * If no options remain, throw an exception.
	 * 
	 * @return the next unchosen OptionCategory
	 * 
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no optionCategories are unfilled anymore
	 * @throws IllegalStateException
	 * 		If there is no active OrderSession
	 */
	public OptionCategory getNextOptionCategory()
			throws NoOptionCategoriesRemainingException,
			IllegalStateException
	{
		return this.getNewOrderSessionHandler().getNextOptionCategory();
	}
	
	/**
	 * Add the given option to the current OrderSession specification
	 * 
	 * @param option
	 * 		The option to add to the specification
	 * @throws IllegalArgumentException
	 * 		If the option is null or not an option of the given unfilled category
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no optionCategories are unfulfilled anymore
	 */
	public void selectOption(Option option) throws IllegalArgumentException, NoOptionCategoriesRemainingException{
		if(option == null)
			throw new IllegalArgumentException("Option can not be null.");
		this.getNewOrderSessionHandler().selectOption(option);
	}
	
	/**
	 * Submit the order composed by the active OrderSession, and return
	 * the resulting order as a container.
	 * 
	 * @return the object of the new order
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
	public void submitOrder() throws IllegalStateException, IllegalArgumentException, IllegalCarOptionCombinationException, OptionRestrictionException{
		this.getNewOrderSessionHandler().submitOrder();
		
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
	 */
	public DateTime getNewOrderETA() throws IllegalStateException{
		return this.getNewOrderSessionHandler().getNewOrderETA();
	}

	//-------------------------------------------------------------------------

}
