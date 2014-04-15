package domain;

/**
 * An Option object represents a choice a user can make when placing an order.
 * Options are gathered in OptionCategories. Each OptionCategory is a type of choice
 * an order can have, and its Options are the concrete choices to fill in the blank.
 * 
 * @author Frederik Goovaerts
 */
public class Option {

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	/**
	 * Construct an Option object with given parameters as its type, name and
	 * description of the action to undertake to execute the option on a car.
	 * 
	 * @param optionType
	 * 		The type the new Option object will have
	 * @param optionName
	 * 		The name of the new Option
	 * @param optionActionInfo
	 * 		The description of the new Option
	 * @throws IllegalArgumentException
	 * 		If one of the arguments is null
	 */
	public Option(TaskType optionType, String optionName, String optionActionInfo){
		if(optionType == null)
			throw new IllegalArgumentException("optionType can not be null.");
		if(optionName == null)
			throw new IllegalArgumentException("optionName can not be null.");
		if(optionActionInfo == null)
			throw new IllegalArgumentException("optionActionInfo can not be null.");
		this.optionType = optionType;
		this.optionName = optionName;
		this.optionActionInfo = optionActionInfo;
	}
	
	//-------------------------------------------------------------------------
	// Properties
	//-------------------------------------------------------------------------
	/**
	 * Get the name of this Option
	 * 
	 * @return the name of this Option
	 */
	public String getName(){
		return this.optionName;
	}
	
	/** The String representing the name of this option */
	private final String optionName;
	
	/**
	 * Get the description of this Option.
	 * The description is a piece of text explaining which actions to take when
	 * applying this option to a car.
	 * 
	 * @return the description of this Option
	 */
	public String getDescription(){
		return this.optionActionInfo;
	}

	/** The String representing the description of the action of this option */
	private final String optionActionInfo;
	
	/**
	 * Get the TaskType of this Option object.
	 * 
	 * @return the TaskType of this Option
	 */
	public TaskType getType(){
		return this.optionType;
	}

	/** The TaskType of this option */
	private final TaskType optionType;
	
	//-------------------------------------------------------------------------
	// Class Methods
	//-------------------------------------------------------------------------

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
		Option other = (Option) obj;
		if (optionName == null) {
			if (other.optionName != null)
				return false;
		} else if (!optionName.equals(other.optionName))
			return false;
		if (optionType != other.optionType)
			return false;
		return true;
	}
}