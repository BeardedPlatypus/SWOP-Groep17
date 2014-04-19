package domain;

import java.util.ArrayList;
import java.util.List;

//TODO: new in iteration 2
public class OrderSession {
	private ArrayList<Option> options;
	private Manufacturer manufacturer;

	public List<Model> getCarModels() {
		throw new UnsupportedOperationException();
	}

	public void chooseModel(Model model) {
		throw new UnsupportedOperationException();
	}

	public void setSessionModel(Model model) {
		throw new UnsupportedOperationException();
	}

	public OptionCategory getNextOptionCategory() {
		throw new UnsupportedOperationException();
	}

	public void selectOption(Option option) {
		throw new UnsupportedOperationException();
	}

	public void addOption(Option option) {
		throw new UnsupportedOperationException();
	}

	public void submitOrder() {
		throw new UnsupportedOperationException();
	}

	public boolean hasUnfilledOptions() {
		// TODO Auto-generated method stub
		return false;
	}
}