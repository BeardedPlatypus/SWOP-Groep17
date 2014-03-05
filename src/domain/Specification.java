package domain;

public class Specification {
	private final int[] specs;
	
	public Specification(int[] choices){
		this.specs = choices.clone();
	}
	
	public int getAmountofSpecs(){
		return specs.length;
	}
	
	public int getSpec(int choice){
		if(choice < 0 || choice > getAmountofSpecs())
			throw new IllegalArgumentException("Not a valid choice number.");
		return specs[choice];
	}
	
	
}