package domain;

import java.util.ArrayList;

/**
 * A model class is a class representing a car model. Has a set of possible options chosen when a car
 * of this model is ordered made.
 * 
 * When an order is to be placed, choices based on the options are stored in specifications.
 * 
 * This class is instantiated by the ModelCatalog and instantiates Specification objects. 
 * 
 * @author Frederik Goovaerts
 *
 */
public class Model {
	
	/**
	 * The name of the particular model. Eg. "Ford Focus".
	 */
	private final String modelName;
	/**
	 * A list of all options that have to be decided when ordering a new car.
	 */
	private ArrayList<Option> options = new ArrayList<Option>();
	
	/**
	 * 
	 * Constructor for the class.
	 * 
	 * Instantiates a model object with a given name and the elements of the argument array.
	 * The resulting two variables are immutable. modelName because of the final modifier,
	 * options because it is incapsulated and protected by the class, and it's contents are
	 * immutable.
	 * 
	 * @param modelName
	 * 		Name this model receives
	 * @param options
	 * 		Arraylist containing all the options this model should offer
	 */
	public Model(String modelName, ArrayList<Option> options){
		this.modelName = modelName;
		this.options = new ArrayList<Option>(options);
	}

	/**
	 * @return
	 * 		The name of this model
	 */
	public String getModelName() {
		return this.modelName;
	}
	
	/**
	 * @return
	 * 		The amount of total choices one can make when placing an order with this car. NOT
	 * 		the overall total amount of options in all options of the model.
	 */
	public int getAmountOfOptions() {
		return options.size();
	}

	/**
	 * Requests a certain Option object.
	 * 
	 * @param optionNb
	 * 		The index in the option list at which position the wanted object resides.
	 * 	
	 * @return
	 * 		The Option object with index optionNb
	 */
	public Option getModelOption(int optionNb) {
		if(optionNb < 0 || optionNb >= getAmountOfOptions())
			throw new IllegalArgumentException("The option you requested does not exist.");
		return options.get(optionNb);
	}
	
	/**
	 * Takes an array of integers and makes a specification out of it if the amount of choices matches the
	 * amount of choices to make for this model.
	 * 
	 * @param choices
	 * 		int array containing the indices depicting choices made for this model's options
	 * 	
	 * @return
	 * 		Specification object containing the choices from the parameters
	 */
	public Specification makeSpecification(int[] choices){
		if(choices.length != getAmountOfOptions())
			throw new IllegalArgumentException("Not the right amount of specification choices for this model have been submitted.");
		return new Specification(choices);
	}

	/**
	 * Checks for a Specification object whether the encompassed specifications match the model's options, both in amount,
	 * as not being too high an index for each option.
	 * 
	 * @param specs
	 * 		The Specification object we want to compare to the options of the model
	 * @return
	 * 		A boolean depicting whether or not the given specs match the options of the model.
	 */
	public boolean isValidSpecification(Specification specs) {
		if(specs.getAmountofSpecs() != options.size())
			return false;
		int amountsOfSpecs = specs.getAmountofSpecs();
		for(int i=0; i < amountsOfSpecs; i++){
			if(options.get(i).getAmountOfChoices() < specs.getSpec(i))
				return false;
		}
		return true;
	}
}