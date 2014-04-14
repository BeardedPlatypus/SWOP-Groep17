package domain;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Immutable;
import domain.Option;
import domain.OptionCategory;

/**
 * A model class is a class representing a car model. Has a set of possible options chosen when a car
 * of this model is ordered made.
 * 
 * When an order is to be placed, choices based on the options are stored in specifications.
 * 
 * This class is instantiated by the ModelCatalog and instantiates Specification objects.
 * 
 * @author Frederik Goovaerts
 */
@Immutable
public class Model {
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**  
	 * Instantiate a new Model with the specified name and options. 
	 * 
	 * @param modelName
	 * 		Name this model receives
	 * @param options
	 * 		Arraylist containing all the options this model should offer
	 */
	public Model(String modelName, 
				 List<Option> options, 
				 List<OptionCategory> optionCategories)
	{
		this.modelName = modelName;
		this.options = new ArrayList<Option>(options);
		this.optionCategories = new ArrayList<OptionCategory>(optionCategories);
	}
	

	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	/**
	 * Get the name of this Model.
	 * 
	 * @return The name of this Model.
	 */
	public String getName() {
		return this.modelName;
	}
	
	/** The name of this Model. Eg. "Ford Focus". */
	private final String modelName;
	
	//--------------------------------------------------------------------------
	/**
	 * Get the Options of this Model. 
	 * 
	 * @return The Options of this Model.
	 */
	public List<Option> getOptions() {
		return new ArrayList<>(this.options);
	}
	
	//FIXME: old functionality
	/**
	 * Get the number of Options that can be chosen when placing an order with
	 * this Model. 
	 *  
	 * @return The number of Options that can be chosen when placing an order with
	 * 		   this Model
	 */
	public int getAmountOfOptions() {
		return this.getOptions().size();
	}

	//FIXME: old functionality
	/**
	 * Request the Option corresponding with the specified index.
	 * 
	 * @param optionNb
	 * 		The index of the requested Option. 
	 * 	
	 * @return The Option corresponding with index optionNb
	 */
	public Option getModelOption(int optionNb) {
		if(optionNb < 0 || optionNb >= getAmountOfOptions())
			throw new IllegalArgumentException("The option you requested does not exist.");
		return options.get(optionNb);
	}
	
	/** The options of this Model. */
	private final List<Option> options;

	//--------------------------------------------------------------------------
	/**
	 * Get the OptionCategories of this Model.
	 * 
	 * @return the OptionCategories of this Model.
	 */
	public List<OptionCategory> getOptionCategories() {
		return new ArrayList<>(this.optionCategories);
	}
	
	/** The OptionCategories of this Model. */
	private  final List<OptionCategory> optionCategories;
	
	//--------------------------------------------------------------------------
	// Specifications
	//--------------------------------------------------------------------------
	//FIXME: old functionality
	/**
	 * Takes an array of integers and makes a specification out of it if the amount of choices matches the
	 * amount of choices to make for this model.
	 * 
	 * @param choices
	 * 		int array containing the indices depicting choices made for this model's options
	 * 	
	 * @return
	 * 		Specification object containing the choices from the parameters
	 */
	public Specification makeSpecification(int[] choices){
		if(choices.length != getAmountOfOptions())
			throw new IllegalArgumentException("Not the right amount of specification choices for this model have been submitted.");
		Specification newSpecs = new Specification(choices);
		if(!isValidSpecification(newSpecs))
			throw new IllegalArgumentException("Not a valid set of specifications for this model.");
		return newSpecs;
	}

	//FIXME: old functionality
	/**
	 * Checks for a Specification object whether the encompassed specifications match the model's options, both in amount,
	 * as not being too high an index for each option.
	 * 
	 * @param specs
	 * 		The Specification object we want to compare to the options of the model
	 * @return
	 * 		A boolean depicting whether or not the given specs match the options of the model.
	 */
	public boolean isValidSpecification(Specification specs) {
		if(specs.getAmountofSpecs() != options.size())
			return false;
		int amountsOfSpecs = specs.getAmountofSpecs();
		for(int i=0; i < amountsOfSpecs; i++){
			if(options.get(i).getAmountOfChoices() < specs.getSpec(i))
				return false;
		}
		return true;
	}
}