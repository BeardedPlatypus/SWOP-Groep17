package domain;

/**
 * Class representing the choices made for a certain model's options.
 * A Specification is tailored for a certain Model, since it depends on the models order of its options, and those options' orders of their choices.
 * 
 * This class is instatiated by a model object, probably in the UI layer.
 * 
 * @author Frederik Goovaerts
 *
 */
public class Specification {
	/**
	 * int array representing the choices made for a model's options, as indices for these options, in the order the model has the options.
	 */
	private final int[] specs;
	
	/**
	 * Constructor
	 * 
	 * Instantiates the object with the int array in the argument. This int array should be built by utilising the model's options.
	 * 
	 * @param choices
	 */
	public Specification(int[] choices){
		this.specs = choices.clone();
	}
	
	/**
	 * @return
	 * 		The amount of choices this object contains
	 */
	public int getAmountofSpecs(){
		return specs.length;
	}
	
	/**
	 * Returns the choice as an int at the given index.
	 * 
	 * @param choice
	 * 		The index in this object's choices where the wanted choice is located
	 * @return
	 * 		The choice's value, which can be used with the model's matching option.
	 */
	public int getSpec(int choice){
		if(choice < 0 || choice > getAmountofSpecs())
			throw new IllegalArgumentException("Not a valid choice number.");
		return specs[choice];
	}
	
	
}