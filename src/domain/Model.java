package domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.annotations.Immutable;
import domain.Option;
import domain.OptionCategory;
import exceptions.NoOptionCategoriesRemainingException;

/**
 * A model class is a class representing a car model. Has a set of OptionCategories
 * with possible Options chosen when a car of this model is ordered.
 * 
 * When an order is to be placed, options from the categories are stored in a specification.
 * 
 * @author Frederik Goovaerts
 */
@Immutable
public class Model {
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**  
	 * Instantiate a new Model with the specified name, OptionCategories and
	 * estimated time spent per workpost. 
	 * 
	 * @param modelName
	 * 		Name this model receives
	 * @param optionCategories
	 * 		List containing all the optionCategories this model should offer
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
	
	/** The name of this Model */
	private final String modelName;
	
	//--------------------------------------------------------------------------
	// OptionCategory methods
	
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
	private final List<OptionCategory> optionCategories;
	
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
	private boolean containsOption(Option option) throws IllegalArgumentException{
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
			if(listOpt == null)
				throw new IllegalArgumentException("One of given options is null.");
			if(!this.containsOption(listOpt))
				throw new IllegalArgumentException("One of the options does not match this car model.");
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
	
	//--------------------------------------------------------------------------
	// Class Methods
	//--------------------------------------------------------------------------

	/**
	 * Check whether all options in the list are contained in one of the model's
	 * optionCategories, and no two of them come from the same optionCategory.
	 * 
	 * @param options
	 * 		The list of options to check
	 * 
	 * @return whether or not the list of options matches these criteria
	 * 
	 * @throws IllegalArgumentException
	 * 		When either the list of options is or contains null
	 */
	public boolean checkOptionsValidity(List<Option> options) throws IllegalArgumentException{
		if(options == null)
			throw new IllegalArgumentException("Options list should not be null.");
		if(options.contains(null))
			throw new IllegalArgumentException("Options list should not contain null.");
		return this.checkContains(options) && this.checkNoDuplicates(options);
	}

	/**
	 * Check to see if all given options are contained is this model.
	 * 
	 * @pre options is not or does not contain null
	 * 
	 * @param options
	 * 		The options to check.
	 * 
	 * @return whether or not all given options are contained is this model
	 */
	private boolean checkContains(List<Option> options) {
		for(Option opt: options){
			if(!this.containsOption(opt))
				return false;
		}
		return true;
	}


	/**
	 * Check to see if there are two or more options in given list, stemming from
	 * the same optionCategory
	 * 
	 * @pre options is not or does not contain null
	 * 
	 * @param options
	 * 		The list of options to check
	 * 
	 * @return whether or not there are two or more options from the same category.
	 * 		Returns false if there are, true otherwise.
	 */
	private boolean checkNoDuplicates(List<Option> options) {
		for(OptionCategory cat: getOptionCategories()){
			boolean foundOne = false;
			for(Option opt: options){
				if(cat.containsOption(opt)){
					if(foundOne){
						return false;
					} else{
						foundOne = true;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Get the next unfilled optionCategory of this model. Unfilled is based on
	 * given list of options, meaning the returned optionsCategory does not contain
	 * any of given options
	 * 
	 * @param options
	 * 		options to treat as already chosen
	 * 
	 * @return an optionCategory of the model which does not contain any of
	 * 		given options
	 * 
	 * @throws NoOptionCategoriesRemainingException
	 * 		When no unfilled optionCategories remain
	 */
	public OptionCategory getNextOptionCategory(List<Option> options)
			throws NoOptionCategoriesRemainingException
	{
		for(OptionCategory cat : this.getOptionCategories()){
			boolean containsNone = true;
			for(Option opt : options){
				if(cat.containsOption(opt))
					containsNone = false;
			}
			if(containsNone)
				return cat;
		}
		throw new NoOptionCategoriesRemainingException("No unfilled optionCategories remaining.");
	}


	/**
	 * Check, based on given options, if there are optionCategories in the model
	 * which do not contain any of given options.
	 * 
	 * @param options
	 * 		The options to treat as already chosen
	 * 
	 * @return whether there are optionCategories in the system which do not
	 * 		contain any of given options
	 */
	public boolean hasUnfilledOptions(List<Option> options) {
		for(OptionCategory cat : this.getOptionCategories()){
			boolean isUnfilled = true;
			for(Option opt : options){
				if(cat.containsOption(opt))
					isUnfilled = false;
			}
			if(isUnfilled)
				return true;
		}
		return false;
	}
}