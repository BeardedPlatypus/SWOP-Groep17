package domain;

//TODO: new in iteration 2
public class ProductionScheduleFacade implements ProductionSchedule {
	public Manufacturer manufacturer;

	public void getPendingOrdersContainers() {
		throw new UnsupportedOperationException();
	}

	public void submitStandardOrder(Model model, Option options) {
		throw new UnsupportedOperationException();
	}

	public void getEstimatedCompletionTime(Object parameter) {
		throw new UnsupportedOperationException();
	}
}