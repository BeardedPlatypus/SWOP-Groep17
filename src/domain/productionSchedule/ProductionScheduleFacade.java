package domain.productionSchedule;

import domain.Manufacturer;
import domain.Model;
import domain.Option;
import domain.ProductionSchedule;
import domain.order.OrderContainer;

public class ProductionScheduleFacade {
	private Manufacturer manufacturer; 
	
	public void getPendingOrdersContainers() {
		throw new UnsupportedOperationException();
	}

	public void submitStandardOrder(Model model, Option options) {
		throw new UnsupportedOperationException();
	}

	public void getEstimatedCompletionTime(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public boolean contains(OrderContainer order) {
		throw new UnsupportedOperationException();
	}
}