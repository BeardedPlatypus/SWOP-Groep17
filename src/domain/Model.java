package domain;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Immutable;
import domain.Option;
import domain.OptionCategory;

/**
 * A model class is a class representing a car model. Has a set of OptionCategories
 * with possible Options chosen when a car of this model is ordered.
 * 
 * When an order is to be placed, options from the categories are stored in a specification.
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
	 * Instantiate a new Model with the specified name and OptionCategories. 
	 * 
	 * @param modelName
	 * 		Name this model receives
	 * @param optionCategories
	 * 		Arraylist containing all the optionCategories this model should offer
	 * @param minsPerWorkPost
	 * 		The amount of minutes that cars of this model are expected to spend
	 * 		on each WorkPost
	 */
	public Model(String modelName,
				 List<OptionCategory> optionCategories, int minsPerWorkPost)
	{
		this.modelName = modelName;
		this.optionCategories = new ArrayList<OptionCategory>(optionCategories);
		this.minsPerWorkPost = minsPerWorkPost;
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
	
	/**
	 * Get the amount of OptionCategories that can be used when placing an order with
	 * this Model. 
	 *  
	 * @return The amount of OptionCategories that can be used when placing an order with
	 * 		   this Model
	 */
	public int getAmountOfOptionCategories() {
		return this.getOptionCategories().size();
	}

	/**
	 * Request the OptionCategory corresponding with the specified index.
	 * 
	 * @param optionCategoryNb
	 * 		The index of the requested OptionCategory. 
	 * 	
	 * @return The OptionCategory corresponding with index optionCategoryNb
	 */
	public OptionCategory getModelOptionCategory(int optionCategoryNb) {
		if(optionCategoryNb < 0 || optionCategoryNb >= getAmountOfOptionCategories())
			throw new IllegalArgumentException("The optionCategory you requested does not exist.");
		return optionCategories.get(optionCategoryNb);
	}
	
	/**
	 * Get all the OptionCategories of this Model. This is not the contained list
	 * itself, but a copy containing the same elements.
	 * 
	 * @return the OptionCategories of this Model.
	 */
	public List<OptionCategory> getOptionCategories() {
		return new ArrayList<>(this.optionCategories);
	}
	
	/** The OptionCategories of this Model. */
	private  final List<OptionCategory> optionCategories;
	
	/**
	 * Check whether or not this model contains given options in one of its
	 * optionCategories, and is therefore a possible choice.
	 * 
	 * @param option
	 * 		The option to check for if it is an option of this model
	 * @return
	 * 		Whether or not the option is contained in this model
	 * @throws IllegalArgumentException
	 * 		If given option is null
	 */
	public boolean containsOption(Option option) throws IllegalArgumentException{
		if(option == null)
			throw new IllegalArgumentException("Option can not be null!");
		for(OptionCategory cat : getOptionCategories()){
			if(cat.containsOption(option)){
				return true;
			}
		}
		return false;
	}
	
	//--------------------------------------------------------------------------
	// Specifications
	//--------------------------------------------------------------------------
	/**
	 * Take a list of options and make a specification containing said options
	 * 
	 * @param options
	 * 		List containing the options from which to make a specification
	 * 	
	 * @return
	 * 		Specification object containing the given options
	 * @throws IllegalArgumentException
	 * 		When the list of options or one of the options is null, or one of the options
	 * 		is not contained in this model
	 */
	public Specification makeSpecification(List<Option> options) throws IllegalArgumentException{
		if(options == null)
			throw new IllegalArgumentException("Given list of options is null.");
		for(Option listOpt : options){
			if(!this.containsOption(listOpt))
				throw new IllegalArgumentException("One of the options does not match this car model.");
			if(listOpt == null)
				throw new IllegalArgumentException("One of given options is null.");
		}
		Specification newSpecs = new Specification(options);
		return newSpecs;
	}
	
	/** The amount of time in minutes that cars of this model are expected to spend
	 * on each WorkPost */
	private final int minsPerWorkPost;
	
	/**
	 * Get the amount of time in minutes that cars of this model are expected to spend
	 * on each WorkPost.
	 * 
	 * @return The amount
	 */
	public int getMinsPerWorkPost() {
		return this.minsPerWorkPost;
	}
}