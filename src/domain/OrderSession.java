package domain;

import java.util.ArrayList;
import java.util.List;


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
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
	// Option methods
	
	/**
	 * Get the list of options of this class for internal use.
	 * 
	 * @returnthe list of options of this class
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
		if(this.getManufacturer().isValidModel(model))
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
	// Class Methods
	//--------------------------------------------------------------------------
	
	public List<Model> getCarModels() {
		throw new UnsupportedOperationException();
	}

	public OptionCategory getNextOptionCategory() {
		throw new UnsupportedOperationException();
	}

	public void selectOption(Option option) {
		throw new UnsupportedOperationException();
	}

	public OrderContainer submitOrder() {
		throw new UnsupportedOperationException();
	}

	public boolean hasUnfilledOptions() {
		// TODO Auto-generated method stub
		return false;
	}
}