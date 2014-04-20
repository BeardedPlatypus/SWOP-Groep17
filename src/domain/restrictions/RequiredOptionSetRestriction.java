package domain.restrictions;

import java.util.List;
import java.util.Set;

import domain.Option;

/**
 * Restriction which requires the checked set of options to contain ONE OR MORE
 * options of a set of required options, contained in this restriction.
 * 
 * @author Frederik Goovaerts
 */
public class RequiredOptionSetRestriction extends Restriction {
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	
	/**
	 * Create a restriction of this type with the given options as required
	 * option set.
	 * 
	 * @param required
	 * 		The set of required options for this restriction
	 * @throws IllegalArgumentException
	 * 		If the given set or one of its elements is null
	 */
	public RequiredOptionSetRestriction(Set<Option> required) throws IllegalArgumentException{
		if(required == null)
			throw new IllegalArgumentException("Required options should not be null.");
		if(required.contains(null))
			throw new IllegalArgumentException("Required options should not contain null.");
		this.requiredOptions = required;
	}
	
	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------
	
	/** The set of required options of this restriction */
	private final Set<Option> requiredOptions;
	
	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see domain.Restriction#checkRestriction(java.util.Set)
	 */
	@Override
	protected boolean checkRestriction(List<Option> options) {
		for(Option req : this.requiredOptions){
			if(options.contains(req))
				return true;
		}
		return false;
	}

}
