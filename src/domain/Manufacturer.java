package domain;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

public class Manufacturer {
	public Manufacturer(ProductionSchedule productionSchedule, 
			            ModelCatalog modelCatalog) {
		this.productionSchedule = productionSchedule;
		this.modelCatalog = modelCatalog;
	}
	
	private ProductionSchedule getProductionSchedule() {
		return this.productionSchedule;
	}
	private final ProductionSchedule productionSchedule;
	private List<Order> completedOrders = new ArrayList<Order>();
	private ModelCatalog modelCatalog;

	public OrderContainer[] getCompletedOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public List<OrderContainer> getPendingOrderContainers() {
		return this.getProductionSchedule().getPendingOrderContainers();
	}

	public Model[] getModels() {
		throw new UnsupportedOperationException();
	}

	public void createOrder(Model model, SpecialAction specifications) {
		throw new UnsupportedOperationException();
	}

	public void completeOrder(Order order) {
		throw new UnsupportedOperationException();
	}
}
