package domain.restrictions;

import java.util.List;
import java.util.Set;

import domain.car.Option;

/**
 * Restriction which has two participants, one present Option, and one set of
 * prohibited options. The restriction states the if the former is present in the
 * checked set, the set may NOT contain ANY element of the latter.
 * 
 * @author Frederik Goovaerts
 *
 */
public class OptionProhibitsOtherSetRestriction extends Restriction {
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Create a restriction of this type with given option and set as presentOption
	 * and prohibitedSet respectively.
	 * 
	 * @param present
	 * 		The option which will function as the present option
	 * @param prohibited
	 * 		The set of options which will function as the prohibited set
	 * @throws IllegalArgumentException
	 * 		If either the present, prohibited set or one of its elements is null.
	 */
	public OptionProhibitsOtherSetRestriction(Option present, Set<Option> prohibited)
			throws IllegalArgumentException
	{
		if(present == null)
			throw new IllegalArgumentException("PresentOption should not be null.");
		if(prohibited == null)
			throw new IllegalArgumentException("Prohibited options should not be null");
		if(prohibited.contains(null))
			throw new IllegalArgumentException("Prohibited options should not contain null.");
		this.presentOption = present;
		this.prohibitedSet = prohibited;
	}
	
	//-------------------------------------------------------------------------
	// Options
	//-------------------------------------------------------------------------
	
	/** The option that has to be present for the restriction to apply */
	private final Option presentOption;
	
	/** Set of option which are prohibited when the presentOption is present */
	private final Set<Option> prohibitedSet;
	
	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see domain.Restriction#checkRestriction(java.util.Set)
	 */
	@Override
	protected boolean checkRestriction(List<Option> options) {
		if(options.contains(presentOption)){
			for(Option prohibitedOption : this.prohibitedSet){
				if(options.contains(prohibitedOption))
					return false;
			}
		}
		return true;
	}

}
