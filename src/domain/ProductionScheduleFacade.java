package domain;

import java.util.List;

//TODO: new in iteration 2
public class ProductionScheduleFacade {
	//If necessary
	//private TimeManager timeLord;
	private Manufacturer manufacturer;

	public List<OrderContainer> getPendingOrdersContainers() {
		throw new UnsupportedOperationException();
	}

	public Order submitStandardOrder(Model model, Specification orderSpecs) {
		throw new UnsupportedOperationException();
	}

	public void getEstimatedCompletionTime(Object parameter) {
		throw new UnsupportedOperationException();
	}
	
	public Order popNextOrderFromSchedule() {
		return this.getProductionSchedule().popNextOrderFromSchedule();
	}
	
	private ProductionSchedule productionSchedule;
	
	/**
	 * Return this ProductionScheduleFacade's ProductionSchedule
	 * 
	 * @return The ProductionSchedule
	 */
	private ProductionSchedule getProductionSchedule() {
		return this.productionSchedule;
	}
}