package domain;


import java.util.ArrayList;

public class Model {
	public ArrayList<Option> options = new ArrayList<Option>();

	public String getModelName() {
		return this.modelName;
	}
	
	private final String modelName;

	public void getModelOptions() {
		throw new UnsupportedOperationException();
	}

	// TODO 
	public Boolean isValidSpecification(Specifications specs) {
		return false;
	}
}