package domain.productionSchedule.strategy;

import java.util.List;

import domain.Specification;
import domain.order.Order;

//TODO
/**
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class BatchStrategy extends SchedulingStrategy {
	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	public BatchStrategy(Specification specs) {
		throw new UnsupportedOperationException();
	}
	
	//--------------------------------------------------------------------------
	// Scheduling Strategy methods.
	//--------------------------------------------------------------------------
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDone(List<Order> orderQueue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sort(List<Order> orderQueue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTo(Order order, List<Order> orderQueue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compare(Order o, Order p) {
		// TODO Auto-generated method stub
		return 0;
	}
}
