package domain;

import java.util.Set;

/**
 * Restriction which has two participants, one present Option, and one set of
 * required options. The restriction states the if the former is present in the
 * checked set, the set MUST contain ONE OR MORE elements of the latter.
 * 
 * @author Frederik Goovaerts
 *
 */
public class OptionRequiresOtherSetRestriction extends Restriction {
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Create a restriction of this type with given option and set as presentOption
	 * and requiredSet respectively.
	 * 
	 * @param present
	 * 		The option which will function as the present option
	 * @param required
	 * 		The set of options which will function as the required set
	 * @throws IllegalArgumentException
	 * 		If either the present, required set or one of its elements is null.
	 */
	public OptionRequiresOtherSetRestriction(Option present, Set<Option> required)
			throws IllegalArgumentException
	{
		if(present == null)
			throw new IllegalArgumentException("PresentOption should not be null.");
		if(required == null)
			throw new IllegalArgumentException("Prohibited options should not be null");
		if(required.contains(null))
			throw new IllegalArgumentException("Prohibited options should not contain null.");
		this.presentOption = present;
		this.requiredSet = required;
	}
	
	//-------------------------------------------------------------------------
	// Options
	//-------------------------------------------------------------------------
	
	/** The option that has to be present for the restriction to apply */
	private final Option presentOption;
	
	/** Set of option which are required when the presentOption is present */
	private final Set<Option> requiredSet;
	
	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see domain.Restriction#checkRestriction(java.util.Set)
	 */
	@Override
	protected boolean checkRestriction(Set<Option> options) {
		if(options.contains(presentOption)){
			for(Option requiredOption : this.requiredSet){
				if(options.contains(requiredOption))
					return true;
			}
			return false;
		} else {
			return true;
		}
	}

}
