package domain.productionSchedule.strategy;

import java.util.List;

import domain.order.Order;

/** 
 * The SchedulingStrategy class provides a base for all SchedulingStrategies. 
 * It defines the methods used by the SchedulerContext to appropriately sort
 * and thus Schedule its pending orders.
 * 
 *  The methods provided are to check if this SchedulingStrategy is done on the 
 *  specified orderQueue. 
 *  It can sort a given queue according to its internal ordering.  
 *  It can add an Order to a specified queue. 
 *  And it can compare two Orders according to the internal ordering.
 *  
 *  It also implements the SchedulingStrategyView 
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public abstract class SchedulingStrategy<O extends Order> implements SchedulingStrategyView {
	
	/**
	 * Given an OrderQueue, check if this SchedulingStrategy has finished its
	 * sorting.
	 * 
	 * @return True if done, false otherwise.
	 */
	public abstract boolean isDone(List<O> orderQueue);
	
	/**
	 * Sort the specified orderQueue according to the internal SchedulingStrategy.
	 * 
	 * @param orderQueue
	 * 		the queue of orders that should be sorted according to this SchedulingStrategy.
	 * 
	 * @postcondition | FORALL order o, order p: (new orderQueue).indexOf(o) < (new orderQueue).indexOf(p) ->
	 * 				  |                          SchedulingStrategy.compare(o, p) <= 0; 
	 */
	public abstract void sort(List<O> orderQueue);
	
	/**
	 * Add the specified order to the specified orderQueue according to the internal SchedulingStrategy.
	 * 
	 * @param order
	 * 		the Order that should be added to the specified orderQueue.
	 * @param orderQueue
	 * 		the orderQueue to which the specified Order should be added.
	 * 
	 * @precondition the list is sorted with this SchedulingStrategy sort.
	 * 
	 * @postcondition | FORALL order p: ((new orderQueue).indexOf(p) < (new orderQueue).indexOf(order) ->
	 * 										SchedulingStrategy.compare(p, order) <= 0) &&
	 * 									((new orderQueue).indexOf(p) > (new orderQueue).indexOf(order) ->
	 * 										SchedulingStrategy.compare(p, order) >= 0) 
	 */
	public void addTo(O order, List<O> orderQueue) {
		if (orderQueue.size() == 0) {
			orderQueue.add(order);
		} else if (orderQueue.size() == 1) {
			this.handleSingleElement(order, orderQueue);
		} else if (this.binarySearch(order, orderQueue) == orderQueue.size() - 1) {
			this.handleLastIndex(order, orderQueue);
		} else{
			orderQueue.add(this.binarySearch(order, orderQueue), order);
		}
	}
	
	/**
	 * Compare the two specified orders according to the specified internal SchedulingStrategy
	 * 
	 * @param o
	 * 		The order that should be compared to Order p.
	 * @param p
	 * 		The order that should be compared to Order o.
	 * 
	 * @return | o < p -> -1
	 * 		   | o = p ->  0
	 * 		   | o > p -> +1
	 */
	public abstract int compare(O o, O p);
	
	@Override
	public abstract String getName();
	
	/**
	 * Search for the position at which the specified Order should be added
	 * to the specified list of Orders.
	 * 
	 * @param order
	 * 		The Order to place
	 * @param orderList
	 * 		The list of Orders to place order in
	 * @return	The position in the list at which the order should be added
	 */
	private int binarySearch(O order, List<O> orderList) {
		int left = 0;
		int right = orderList.size() - 1;
		while (left != right) {
			int mid = (left + right) / 2;
			int compare = this.compare(order, orderList.get(mid));
			if (compare == 0) {
				return mid;
			}
			else if (compare < 0) {
				right = mid;
			}
			else {
				left = mid + 1;
			}
		}
		return left;
	}
	
	/**
	 * When adding, handle the case that there is a single element in the list.
	 * 
	 * @param order
	 * 		The order to be added
	 * @param orderQueue
	 * 		The list to add the order to
	 */
	private void handleSingleElement(O order, List<O> orderQueue) {
		int compare = this.compare(order, orderQueue.get(0));
		if (compare <= 0) {
			orderQueue.add(0, order);
		} else {
			orderQueue.add(order);
		}
	}
	
	/**
	 * When adding, handle the case that binary search returned the largest index
	 * possible.
	 * 
	 * @param order
	 * 		The order to be added
	 * @param orderQueue
	 * 		The list to add the order to
	 */
	private void handleLastIndex(O order, List<O> orderQueue) {
		int compare = this.compare(order, orderQueue.get(orderQueue.size() - 1));
		if (compare <= 0) {
			orderQueue.add(orderQueue.size() - 1, order);
		} else {
			orderQueue.add(order);
		}
	}
}