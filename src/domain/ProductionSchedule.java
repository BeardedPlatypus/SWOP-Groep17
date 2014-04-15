package domain;

import java.util.ArrayList;
import java.util.Comparator;

import domain.Order;

/**
 * The ProductionSchedule class. Acts as interface between the manufacturer and
 * the AssemblyLine. Provides methods for converting a Model and (valid)
 * specification to an Order, as well as managing the overall Time of the system.
 * 
 * @author Martinus Wilhelmus Tegelaers
 * 
 * @invariant | this.getOverTime() >= 0
 */
public class ProductionSchedule {
	private ProductionScheduleFacade productionScheduleFacade;
	public TimeManager timeManager;
	public ScheduleContext scheduleContext;

	public void getPendingOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public void submitSingleTaskOrder(Object parameter, Object parameter2) {
		throw new UnsupportedOperationException();
	}

	public void getEstimatedCompletionTime(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public void setNewSchedulingAlgorithm(Comparator<Order> comparator) {
		throw new UnsupportedOperationException();
	}
}