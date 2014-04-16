package domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.*;

import domain.Option;

/**
 * Class representing the choice in options for a single order.
 * These options are received from a model's optionCategories, and put into an
 * object of this class.
 * 
 * @author Frederik Goovaerts
 */
public class Specification {
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Create a specification object containing given options
	 * 
	 * @param options
	 * 		The options for the new specification object
	 * 
	 * @throws IllegalArgumentException
	 * 		When the list or an element in the list is null
	 */
	public Specification(List<Option> options) throws IllegalArgumentException{
		if(options == null)
			throw new IllegalArgumentException("Given list of options is null.");
		if(options.contains(null))
			throw new IllegalArgumentException("One of given options is null.");
		this.options = options;
	}
	
	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------

	/**
	 * Get the Option objects contained in this Specification.
	 * This method returns a copy of the list with the same elements, and the
	 * same order as the original list.
	 * 
	 * @return a list with the options of this specification.
	 */
	public List<Option> getOptions() {
		return new ArrayList<Option>(this.options);
	}
	
	/**
	 * Get the option at a given index in the list of options in this specification object.
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
	 * Get the amount of options contained in this specification
	 * 
	 * @return the amount of options contained in this specification
	 */
	public int getAmountOfOptions(){
		return options.size();
	}
	
	/** The Option objects contained in this Specification. */
	private final List<Option> options;

	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Specification other = (Specification) obj;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (! CollectionUtils.isEqualCollection(this.getOptions(), other.getOptions()))
			return false;
		return true;
	}
	
	
}