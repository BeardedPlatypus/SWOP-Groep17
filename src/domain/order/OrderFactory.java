package domain.order;

import java.util.List;

import domain.DateTime;
import domain.Manufacturer;
import domain.car.Option;
import domain.car.Specification;
import domain.car.Model;
import domain.clock.TimeObserver;

/** 
 * The OrderFactory provides a centralised interface for making Orders within 
 * the Domain. It handles the verification of the input parameters.  
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class OrderFactory implements TimeObserver {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------	
	/**
	 * Set the Manufacturer of this OrderFactory to the specified manufacturer. 
	 * 
	 * @param manufacturer
	 * 		The new manufacturer of this OrderFactory. 
	 * 
	 * @postcondition | (new this).getManufacturer() == manufacturer.
	 * 
	 * @throws IllegalArgumentException
	 * 		| manufacturer == null
	 * @throws IllegalStateException 
	 * 		| manufacturer.getOrderFactory() == this 
	 * @throws IllegalStateException
	 * 		manufacturer has already been set.
	 */
	public void setManufacturer(Manufacturer manufacturer) throws IllegalStateException {
		if (manufacturer == null)
			throw new IllegalArgumentException("Manufacturer cannot be null.");
		if (manufacturer.getOrderFactory() != this)
			throw new IllegalStateException("manufacturer.getOrderFactory() does not match this.");
		if (this.manufacturer != null) 
			throw new IllegalStateException("manufacturer has already been set.");
		
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Get the Manufacturer of this OrderFactory. 
	 * 
	 * @return The Manufacturer of this OrderFactory.
	 * 
	 * @throws IllegalArgumentException
	 * 		Manufacturer has not been set.
	 */
	public Manufacturer getManufacturer() {
		if (this.manufacturer == null)
			throw new IllegalStateException();
		
		return this.manufacturer;
	}

	/** The Manufacturer of this OrderFactory. */
	private Manufacturer manufacturer = null;
	//--------------------------------------------------------------------------
	// Make SingleTaskOrder functions.
	//--------------------------------------------------------------------------
	/**
	 * Construct a new SingleTaskOrder with the specified deadline, and specification,
	 * the current time of the domain, and an unique order identifier and a 
	 * UnspecifiedModel model. 
	 * 
	 * @param deadline
	 * 		The deadline of this new SingleTaskOrder.
	 * @param specification
	 * 		The specification of this new SingleTaskOrder.
	 * 
	 * @return a new SingleTaskOrder with the specified deadline, specification 
	 * 		   and the current time, unique order identifier, and a UnspecifiedModel.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * 		| !this.isValidInputSingleTaskOrder()
	 */
	public SingleTaskOrder makeNewSingleTaskOrder(DateTime deadline, 
			                                      Specification specification) 
			                                    		  throws IllegalArgumentException{
		if(!isValidInputSingleTaskOrder(deadline, specification)) {
			throw new IllegalArgumentException("Input parameters are not valid.");
		}
		
		int orderId = this.getCurrentOrderIdentifier();
		this.incrementOrderIdentifier();
		
		return new SingleTaskOrder(this.getManufacturer().getSingleTaskModel(), 
				                   specification, 
				                   orderId, 
				                   this.getCurrentTime(), 
				                   deadline);	
	}
	
	/**
	 * Construct a new SingleTaskOrder with the specified deadline, and option,
	 * the current time of the domain, and an unique order identifier and a 
	 * UnspecifiedModel model. A Specification is generated from the specified Option,
	 * then all input is passed to makeNewSingleTaskOrder(DateTime, Specification)
	 * 
	 * @param deadline
	 * 		The deadline of this new SingleTaskOrder.
	 * @param option
	 * 		The option of this new SingleTaskOrder.
	 * 
	 * @return a new SingleTaskOrder with the specified deadline, specification 
	 * 		   and the current time, unique order identifier, and a UnspecifiedModel.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * 		| !this.isValidInputSingleTaskOrder()
	 */
	public SingleTaskOrder makeNewSingleTaskOrder(DateTime deadline, 
			                                      Option option) 
			                                    		  throws IllegalArgumentException{
		
		Specification spec = new Specification(option);
		
		return makeNewSingleTaskOrder(deadline, spec);
	}

	/**
	 * Check whether the specified SingleTaskOrder parameters are valid parameters
	 * for the creation of a SingleTaskOrder.
	 *  
	 * @param deadline
	 * 		The deadline of the specified SingleTaskOrder.
	 * @param specification
	 * 		The specification with a single option of the specified SingleTaskOrder.
	 * 
	 * @return | specification != null &&
	 * 	       | deadline != null &&
	 * 		   | specification.getAmountOfOptions() == 1 &&
	 * 		   | manufacturer.singleTaskCatalogContains(specification.getOption(0)
	 */
	public boolean isValidInputSingleTaskOrder(DateTime deadline, Specification specification) {
		return (specification != null && 
				deadline != null &&
				specification.getAmountOfOptions() == 1 &&
				this.getManufacturer().singleTaskCatalogContains(specification.getOption(0)));
	}
	
	//--------------------------------------------------------------------------
	// Make StandardOrder functions.
	//--------------------------------------------------------------------------
	/**
	 * Construct a new StandardOrder with the specified Specification, specified 
	 * Model, the current time of the domain, an unique order identifier.
	 * 
	 * @param model
	 * 		The Model of this new StandardOrder.
	 * @param specification
	 * 		The Specification of this new StandardOrder.
	 * 
	 * @return A new StandardOrder with the specified Model, specification,
	 * 		   the current time, and an unique order identifier.
	 * 
	 * @throws IllegalArgumentException
	 * 		| !this.isValidInputStandardOrder()
	 */
	public StandardOrder makeNewStandardOrder(Model model, 
			                                  Specification specification) 
			                                		  throws IllegalArgumentException{
		if (!this.isValidInputStandardOrder(model, specification)) {
			throw new IllegalArgumentException("Input parameters are not valid.");
		}
		
		int orderId = this.getCurrentOrderIdentifier();
		this.incrementOrderIdentifier();

		return new StandardOrder(model, 
								 specification, 
								 orderId, 
								 this.getCurrentTime());
	}
	
	//TODO Check if this should be split up or not.
	/**
	 * Check whether the specified StandardOrder parameters are valid parameters
	 * for the creation of a StandardOrder.
	 * 
	 * @param model
	 * 		The model of the specified StandardOrder
	 * @param specification
	 * 		The specification of the specified StandardOrder
	 * 
	 * @return | model == null || spec == null -> False
	 * 		   | !this.manufacturer.modelCatalogContains(model)
	 *         | specification.options == null || contains(null) || size == 0 -> False
	 *         | else: model.checkOptionsValidity(options) && 
	 *         | this.manufacturer.checkSpecificationRestriction(model, spec)
	 */
	public boolean isValidInputStandardOrder(Model model, Specification specification) {
		// Check not null.
		if (model == null || specification == null)
			return false;
		
		// Check Model related values.
		if (!this.getManufacturer().modelCatalogContains(model)) {
			return false;
		}
		
		// Check Option related values.
		List<Option> options = specification.getOptions();
		if (options == null || options.contains(null) || 
				specification.getAmountOfOptions() == 0) {
			return false;
		}
		
		return model.checkOptionsValidity(options) &&
				this.getManufacturer().checkSpecificationRestrictions(model, specification);
	}
	
	//--------------------------------------------------------------------------
	// Order Identifier methods
	//--------------------------------------------------------------------------
	/** 
	 * Get the current order identifier of this ProductionSchedule.
	 * 
	 * @return The current order identifier of this ProductionSchedule.
	 */
	private int getCurrentOrderIdentifier() {
		return this.currentIdentifier;
	}
	
	/**
	 * Increment the current order identifier of this ProductionSchedule by one. 
	 * 
	 * @effect | this.setCurrentIdentifier(this.getCurrentIdentifier() + 1)
	 */
	private void incrementOrderIdentifier() {
		this.setOrderIdentifier(this.getCurrentOrderIdentifier() + 1);
	}
	
	/**
	 * Set the currentOrderIdentifier of this ProductionSchedule to newIdentifier.
	 * 
	 * @param newIdentifier
	 * 		The new value of the currentIdentifier.
	 * 
	 * @postcondition | (new this).getCurrentIdentifier() == newIdentifier.
	 */
	private void setOrderIdentifier(int newIdentifier) {
		this.currentIdentifier = newIdentifier;
	}
	
	/** The next unused order identifier issued by this ProductionSchedule. */
	private int currentIdentifier = 0;
	
	//--------------------------------------------------------------------------
	// TimeObserver methods
	//--------------------------------------------------------------------------
	@Override
	public void update(DateTime time) throws IllegalArgumentException {
		if (time == null)
			throw new IllegalArgumentException("Time cannot be null.");
		
		this.currentTime = time;
	}	
	
	/** 
	 * Get the currentTime of the domain of this OrderFactory. 
	 * 
	 * @return the currentTime of the domain of this OrderFactory.
	 * 
	 * @throws IllegalStateException
	 * 		Time has not been set (it has not been attached to a TimeSubject yet).
	 */
	protected DateTime getCurrentTime() throws IllegalStateException{
		if (this.currentTime == null)
			throw new IllegalStateException("Time has not been set.");
		
		return this.currentTime;
	}
	
	/** The currentTime of the domain. */
	private DateTime currentTime = null;
}
