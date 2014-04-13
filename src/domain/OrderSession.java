package domain;

public class OrderSession {
	public ModelCatalog modelCatalog;
	public Specification specification;
	public Manufacturer manufacturer;

	public void getCarModels() {
		throw new UnsupportedOperationException();
	}

	public void chooseModel(Model model) {
		throw new UnsupportedOperationException();
	}

	public void setSessionModel(Model model) {
		throw new UnsupportedOperationException();
	}

	public void getNextOptionCategory() {
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
}