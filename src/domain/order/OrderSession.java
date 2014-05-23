package domain.order;

import java.util.ArrayList;
import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Model;
import domain.car.Option;
import domain.car.OptionCategory;
import exceptions.IllegalVehicleOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;


/**
 * Class which represents an ordering session for the system. The session is tied
 * to a manufacturer, with which it interfaces to choose a model and options, and
 * eventually submit the specifications for the order to the system.
 * 
 * @author Frederik Goovaerts
 *
 */
public class OrderSession {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	
	/**
	 * Create a new OrderSession with given manufacturer as the manufacturer.
	 * 
	 * @param man
	 * 		The manufacturer for the new OrderSession
	 * @throws IllegalArgumentException
	 * 		If the given manufacturer is null
	 */
	public OrderSession(Manufacturer man) throws IllegalArgumentException{
		if(man == null)
			throw new IllegalArgumentException("Manufacturer can not be null.");
		this.manufacturer = man;
		this.model = null;
		this.resultingOrder = null;
		this.options = new ArrayList<Option>();
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Option methods
	
	/**
	 * Get the list of options of this class for internal use.
	 * 
	 * @return the list of options of this class
	 */
	private List<Option> getOptions(){
		return this.options;
	}
	
	/** A list of options forming the temporary specification of this order session */
	private ArrayList<Option> options;
	
	/**
	 * Add given option to the current specification in this OrderSession
	 * 
	 * @param option
	 * 		The option to add to this OrderSession's set of chosen options
	 * @throws IllegalArgumentException
	 * 		If the given option is null
	 * 
	 */
	public void addOption(Option option) throws IllegalArgumentException{
		if(option == null)
			throw new IllegalArgumentException("Option should not be null.");
		this.getOptions().add(option);
	}

	/**
	 * Check whether the model has unfilled OptionCategories with the given Options
	 * in this class.
	 * 
	 * @return whether the model has unfilled options
	 * 
	 * @throws IllegalStateException
	 * 		When the session does not have a model yet.
	 */
	public boolean hasUnfilledOptions() throws IllegalStateException{
		if(!this.modelIsChosen())
			throw new IllegalStateException("Model is not yet chosen.");
		return this.getModel().hasUnfilledOptions(new ArrayList<Option>(this.getOptions()));
	}
	
	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Manufacturer methods
	
	/**
	 * Get the Manufacturer of this class for internal use.
	 * 
	 * @return the manufacturer of this class
	 */
	private Manufacturer getManufacturer(){
		return this.manufacturer;
	}
	
	/** The manufacturer this session interfaces with to build an order */
	private final Manufacturer manufacturer;
	
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// Model methods
	
	/**
	 * Set the model of this OrderSession to given model. The model has to be a
	 * model of the system, and not null.
	 * This option can be called only once per session, when the model has not
	 * been set yet, else an exception is thrown.
	 * 
	 * @param model
	 * 		The model to set for this session.
	 * @throws IllegalArgumentException
	 * 		If the given model is null or not a model of the system
	 * @throws IllegalStateException
	 * 		If the model has already been set for this Session
	 */
	public void chooseModel(Model model) throws IllegalArgumentException, IllegalStateException{
		if(model == null)
			throw new IllegalArgumentException("Chosen model can not be null.");
		if(!this.getManufacturer().isValidModel(model))
			throw new IllegalArgumentException("Not a valid model for the system.");
		if(modelIsChosen())
			throw new IllegalStateException("There already is a chosen model for this session.");
		this.setModel(model);
	}
	
	
	/**
	 * Check whether or not there is already a chosen model for this Session.
	 * 
	 * @return whether or not there already is a chosen model
	 */
	private boolean modelIsChosen(){
		if(this.getModel() == null)
			return false;
		return true;
	}
	
	/**
	 * Get the model of this class for internal use
	 * 
	 * @return the model
	 */
	private Model getModel() {
		return model;
	}

	/**
	 * Set the model of this class to given model
	 * 
	 * @param model
	 * 		the model to set
	 */
	private void setModel(Model model) {
		this.model = model;
	}

	/** The model of this session. Starts empty, supposed to be set only once */
	private Model model;

	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Order methods
	
	
	/**
	 * Check whether or not an order is made with this session.
	 * 
	 * @return whether or not an order is made with this session
	 */
	private boolean orderIsMade(){
		return !(this.getOrder() == null);
	}
	
	/**
	 * Get the Order of this class, for internal use
	 * 
	 * @return the order of this class
	 */
	private OrderView getOrder(){
		return this.resultingOrder;
	}
	
	/**
	 * Set the Order of this class to given order, if it is a valid order, and
	 * an order hasn't been set yet.
	 * 
	 * @post this.resultingOrder = order
	 * 
	 * @throws IllegalArgumentException
	 * 		When the given order is not valid
	 * @throws IllegalStateException
	 * 		When an order was previously set
	 */
	private void setOrder(OrderView generatedOrder) throws IllegalArgumentException, IllegalStateException{
		if(generatedOrder == null)
			throw new IllegalArgumentException("Order is not valid.");
		if(orderIsMade())
			throw new IllegalStateException("An order has already been set.");
		this.resultingOrder = generatedOrder;
	}
	
	/** The order this  */
	private OrderView resultingOrder;

	//--------------------------------------------------------------------------

	
	//--------------------------------------------------------------------------
	// Class Methods
	//--------------------------------------------------------------------------

	/**
	 * Get the list of models in the system
	 * 
	 * @return	the list of models in the system
	 * 
	 * @pre
	 * 		this.getManufacturer != null
	 */
	public List<Model> getVehicleModels(){
		return this.getManufacturer().getVehicleModels();
	}

	/**
	 * Get the next unfilled optionCategory from set model with the options
	 * currently chosen in this session. OptionCategories with one option are
	 * skipped, and the option is added when the order is submitted. It is viewed
	 * as a required or default option.
	 * 
	 * @return the next unfilled optionCategory
	 * 
	 * @throws IllegalStateException
	 * 		When no model has been set
	 * @throws NoOptionCategoriesRemainingException 
	 * 		When no more options are available
	 */
	public OptionCategory getNextOptionCategory()
			throws IllegalStateException,
			NoOptionCategoriesRemainingException
	{
		if(!this.modelIsChosen())
			throw new IllegalStateException("No model has been chosen yet.");
		return this.getModel().getNextOptionCategory(options);
	}

	/**
	 * Submit the current state of this session to the system, to form an order.
	 * The order is then saved in this session, so the ETA can be calculated.
	 * 
	 * @throws IllegalVehicleOptionCombinationException 
	 * 		When the chosen options are not valid with given model
	 * @throws OptionRestrictionException
	 * 		When the set of options does not meet the system's restrictions
	 * @throws IllegalStateException
	 * 		When the model or options are null
	 * @throws IllegalStateException
	 * 		When the order was already submitted
	 */
	public void submitOrder() throws IllegalArgumentException,
									 IllegalVehicleOptionCombinationException,
									 OptionRestrictionException,
									 IllegalStateException
	{
		if(this.orderIsMade())
			throw new IllegalStateException("An order has already been made from this Session.");
		if(!this.modelIsChosen())
			throw new IllegalStateException("No model specified in this session.");
		try{
			List<Option> allDesiredOptions = this.getModel().getSolitaryOptions();
			allDesiredOptions.addAll(this.getOptions());
			OrderView generatedOrder = this.getManufacturer().
					submitStandardOrder(this.getModel(),
							allDesiredOptions);
			this.setOrder(generatedOrder);
			System.out.println("Order Made, estimated completion time is:");
			System.out.println(this.getETA());
		} catch (IllegalArgumentException e){
			throw new IllegalStateException("Session is not valid (yet).");
		}
	}

	/**
	 * When a new order has been constructed with this session, this method allows
	 * the user to query the system for the ETA of the new order.
	 * 
	 * @return the ETA of the order made with this session
	 * 
	 * @throws IllegalStateException
	 * 		If the session has not created an order in the system.
	 * @throws OrderDoesNotExistException 
	 * 		Is the order of this OrderSession is not recognised by the system
	 */
	public DateTime getETA() throws OrderDoesNotExistException {
		if(!this.orderIsMade())
			throw new IllegalStateException("No order has been made with this session yet!");
		return this.getManufacturer().getEstimatedCompletionTime(this.getOrder());
	}
}