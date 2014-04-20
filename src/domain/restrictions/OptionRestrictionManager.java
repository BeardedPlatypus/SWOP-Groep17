package domain.restrictions;

import java.util.ArrayList;
import java.util.List;

import domain.Model;
import domain.Option;


/**
 * A class containing restriction objects. This class can take a set of options
 * and verify whether or not all of its contained restrictions apply.
 * 
 * @author Frederik Goovaerts
 */
public class OptionRestrictionManager {
	
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------

	/**
	 * Create a new restrictionManager with given list of restrictions as its
	 * restrictions. The list must be a well-formed list of restrictions.
	 * 
	 * @param restrictions
	 * 		The restriction for the new restrictionManager
	 * 
	 * @throws IllegalArgumentException
	 * 		When the list of resrtictions is or contains null
	 */
	public OptionRestrictionManager(List<Restriction> restrictions)
			throws IllegalArgumentException
	{
		if (restrictions == null)
			throw new IllegalArgumentException("restrictions list should not be null.");
		if (restrictions.contains(null))
			throw new IllegalArgumentException("restrictions list should not contain null");
		this.restrictions = restrictions;
	}
	
	//--------------------------------------------------------------------------
	// Properties
	//--------------------------------------------------------------------------
	
	/**
	 * Get the manager's restrictions as a list, for internal usage
	 * 
	 * @return the restrcitions of this manager as a list
	 */
	private List<Restriction> getRestrictions(){
		return new ArrayList<>(this.restrictions);
	}
	
	/** The restrictions this manager imposes */
	private final List<Restriction> restrictions;
	
	//--------------------------------------------------------------------------
	// Class Methods
	//--------------------------------------------------------------------------
	
	/**
	 * Check whether given list of options matches all restrictions of the manager.
	 * A model parameter is also supplied, for when restrictions might become
	 * dependent on a model. Currently this parameter is unused.
	 * 
	 * @param model
	 * 		The model for which to check the restrictions, currently unused
	 * @param options
	 * 		The options for which to check the restrictions
	 * @return
	 * 		Whether or not given model and options match ALL restrictions
	 * @throws IllegalArgumentException
	 * 		When either of the parameters is or contains null
	 */
	public boolean checkValidity(Model model, List<Option> options)
			throws IllegalArgumentException
	{
		if(model == null)
			throw new IllegalArgumentException("Model should not be null.");
		if (options == null)
			throw new IllegalArgumentException("options list should not be null.");
		if (options.contains(null))
			throw new IllegalArgumentException("options list should not contain null");
		for(Restriction res : getRestrictions()){
			if(!res.isLegalOptionList(options))
				return false;
		}
		return true;
	}
	
}