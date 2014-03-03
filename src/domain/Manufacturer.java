import java.util.ArrayList;

public class Manufacturer {
	public NewOrderSessionController unnamed_NewOrderSessionController_;
	public ProductionSchedule productionSchedule;
	public ArrayList<Order> completedOrders = new ArrayList<Order>();
	public ModelCatalog unnamed_ModelCatalog_;

	public OrderContainer[] getCompletedOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public OrderContainer[] getPendingOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public Model[] getModels() {
		throw new UnsupportedOperationException();
	}

	public void createOrder(Object model, Object specifications) {
		throw new UnsupportedOperationException();
	}

	public void completeOrder(Object order) {
		throw new UnsupportedOperationException();
	}
}