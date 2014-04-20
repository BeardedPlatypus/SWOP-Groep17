package domain.restrictions;

import java.util.Set;

import domain.Option;

/**
 * Superclass of all Restriction classes.
 * This series of classes has its own inherent restriction made up of options and
 * some way they interact. A restriction class can then accept a set of options
 * and check whether or not its restriction is enforced, or the set is deemed as
 * illegal by the restriction.
 * 
 * @author Frederik Goovaerts
 */
public abstract class Restriction {
	
	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Check wether or not a set of options is a valid set to check for 
	 * restrictions and pass it to the actual restriction function if it is
	 * a legal set.
	 * 
	 * @param options
	 * 		The set of options on which to check the restriction
	 * @return
	 * 		Whether or not the set of options satisfies the condition(s) of this
	 * 		restriction.
	 * @effect
	 * 		checkRestriction(options)
	 * @throws IllegalArgumentException
	 * 		if the set or one of its elements is null
	 */
	public boolean isLegalOptionSet(Set<Option> options) throws IllegalArgumentException{
		if(options == null)
			throw new IllegalArgumentException("Given list of options is null.");		
		if(options.contains(null))
			throw new IllegalArgumentException("One of given options is null.");
		return checkRestriction(options);
	}
	
	/**
	 * Checks this restriction on given set of options. Restriction details are
	 * explained in the header of each non-abstract restriction type.
	 * 
	 * @pre
	 * 		options != null
	 * @pre
	 * 		!options.contains(null)		
	 * @param options
	 * 		The set of options for which to check this restriction
	 * @return
	 * 		whether or not given set of options matches this restriction
	 */
	protected abstract boolean checkRestriction(Set<Option> options);
}
