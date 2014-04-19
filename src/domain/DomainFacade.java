package domain;

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
	
	/**
	 * Start a new orderSession in the newOrderSessionHandler, so a user can
	 * put together an order from scratch. This resets all previously chosen
	 * model and options in the Handler.
	 * 
	 * @pre
	 * 		this.getNewOrderSessionHandler() != null
	 * @effect
	 * 		this.getNewOrderSessionHandler().startNewOrderSession()
	 * 
	 */
	public void startNewOrderSession(){
		this.getNewOrderSessionHandler().startNewOrderSession();
	}
}