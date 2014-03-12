package domain;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An Option object represents a choice a user can make when placing an order.
 * An Option has a name and multiple choices, of which one can be chosen in a Specification object.
 * 
 * @author Frederik Goovaerts
 *
 */
public class Option {
	/**
	 * The name of this Option, eg. body.
	 */
	private final String optionName;
	
	/**
	 * A small text depicting on how a choice of this option should be applied to the car.
	 */
	private final String optionActionDescription;
	
	/**
	 * A list of Strings which represent the possible choices for this options.
	 * Eg. sedan, break...
	 */
	private final ArrayList<String> choices;
	
	/**
	 * The type of the task this option will amount to
	 */
	private final TaskType optionType;
	
	/**
	 * Constructor
	 * 
	 * Creates a new Option object with the first argument being the name and all subsequent arguments the possible choices.
	 * 
	 * @param name
	 * 		The name of the option.
	 * @param choicesArgs
	 * 		The possible choices, these can be variable in number.
	 */
	public Option(String name, String optionActionDescription, TaskType type, String...choicesArgs){
		this.optionName = name;
		this.optionActionDescription = optionActionDescription;
        this.choices = new ArrayList<String>();
        this.optionType = type;
        for (String choice : choicesArgs) {
            this.choices.add(choice);
        }
	}
	
	/**
	 * @return
	 * 		The name for this option
	 */
	public String getOptionName() {
		return this.optionName;
	}
	
	/**
	 * @return
	 * 		The type associated with this option
	 */
	public TaskType getType(){
		return this.optionType;
	}
	
	/**
	 * @return
	 * 		The description of the action to execute for adding this option to a car
	 */
	public String getOptionActionDescription() {
		return this.optionActionDescription;
	}
	
	/**
	 * Returns the name of the choice at a given index
	 * 
	 * @param choiceNb
	 * 		The index of the wanted choice
	 * @return
	 * 		The name of the choice at given index
	 */
	public String getChoiceName(int choiceNb){
		if(choiceNb < 0 || choiceNb > getAmountOfChoices())
			throw new IllegalArgumentException("Not a valid choice number.");
		return choices.get(choiceNb);
	}
	
	/**
	 * @return
	 * 		The amount of choices this Option has
	 */
	public int getAmountOfChoices() {
		return choices.size();
	}

	/**
	 * @return
	 * 		An Iterator which iterates over all possible choices, for easy listing the choices.
	 */
	public Iterator<String> getChoicesIterator(){
		return choices.iterator();
	}
	
}