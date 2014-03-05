package domain;

import java.util.ArrayList;

public class Model {
	private final String modelName;
	private ArrayList<Option> options = new ArrayList<Option>();
	
	public Model(String modelName, ArrayList<Option> options){
		this.modelName = modelName;
		this.options = options;
	}

	public String getModelName() {
		return this.modelName;
	}
	
	public int getAmountOfOptions() {
		return options.size();
	}

	public Option getModelOption(int optionNb) {
		if(optionNb < 0 || optionNb >= getAmountOfOptions())
			throw new IllegalArgumentException("The option you requested does not exist.");
		return options.get(optionNb);
	}
	
	public Specification makeSpecification(int[] choices){
		if(choices.length != getAmountOfOptions())
			throw new IllegalArgumentException("Not the right amount of specification choices for this model have been submitted.");
		return new Specification(choices);
	}

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