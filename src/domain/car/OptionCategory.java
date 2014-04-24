package domain.car;

//TODO new, original option, but now uses option object
import java.util.ArrayList;
import java.util.List;

public class OptionCategory {
	

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Construct an OptionsCategory object with given options list from the
	 * arguments.
	 * 
	 * @param options
	 * 		The list of options the new optionCategory should contain
	 * @throws IllegalArgumentException
	 * 		When the name, the list or an element in the list is null
	 */
	public OptionCategory(List<Option> options, String categoryName) throws IllegalArgumentException{
		if(categoryName == null)
			throw new IllegalArgumentException("Name can not be null");
		if(options == null)
			throw new IllegalArgumentException("Given list of options is null.");
		if(options.contains(null))
			throw new IllegalArgumentException("One of given options is null.");
		this.options = new ArrayList<>(options);
		this.name = categoryName;
	}
	
	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------

	/**
	 * Get the name of this category
	 * 
	 * @return the name of this category
	 */
	public String getName(){
		return this.name;
	}
	
	/** name of this category */
	private final String name;
	
	
	/** The list of options this OptionCategory encompasses  */
	private final ArrayList<Option> options;

	/**
	 * Get the Option objects contained in this OptionCategory.
	 * This method returns a copy of the list with the same elements, and the
	 * same order as the original list.
	 * 
	 * @return a list with the options of this category.
	 */
	private ArrayList<Option> getOptions() {
		return new ArrayList<>(options);
	}
	
	/**
	 * Get the option at a given index in the list of options in this category object.
	 * 
	 * @param optionNb
	 * 		The index of the option to return
	 * @return
	 * 		The option at given index
	 * @throws IllegalArgumentException
	 * 		When the index is out of the bounds of the options list
	 */
	public Option getOption(int optionNb) throws IllegalArgumentException{
		if(optionNb<0 || optionNb>=this.getAmountOfOptions())
			throw new IllegalArgumentException("Selected optionNb is not valid. It is either too small or large.");
		return this.getOptions().get(optionNb);
	}
	
	/**
	 * Get the amount of options contained in this category
	 * 
	 * @return the amount of options contained in this category
	 */
	public int getAmountOfOptions(){
		return options.size();
	}

	/**
	 * Check if given option is contained in this category.
	 * 
	 * @param option
	 * 		The option to check for if it is contained in this category
	 * @return
	 * 		Whether or not the option is contained in this category
	 */
	public boolean containsOption(Option option) {
		for(Option catOpt : this.getOptions()){
			if(catOpt.equals(option))
				return true;
		}
		return false;
	}
	
}