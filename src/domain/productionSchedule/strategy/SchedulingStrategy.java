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
public abstract class SchedulingStrategy implements SchedulingStrategyView {
	/**
	 * Given an OrderQueue, check if this SchedulingStrategy has finished its
	 * sorting.
	 * 
	 * @return True if done, false otherwise.
	 */
	public abstract boolean isDone(List<Order> orderQueue);
	
	/**
	 * Sort the specified orderQueue according to the internal SchedulingStrategy.
	 * 
	 * @param orderQueue
	 * 		the queue of orders that should be sorted according to this SchedulingStrategy.
	 * 
	 * @postcondition | FORALL order o, order p: (new orderQueue).indexOff(o) < (new orderQueue).indexOff(p) ->
	 * 				  |                          SchedulingStrategy.compare(o, p) <= 0; 
	 */
	public abstract void sort(List<Order> orderQueue);
	
	/**
	 * Add the specified order to the specified orderQueue according to the internal SchedulingStrategy.
	 * 
	 * @param order
	 * 		the Order that should be added to the specified orderQueue.
	 * @param orderQueue
	 * 		the orderQueue to which the specified Order should be added.
	 * 
	 * @postcondition | FORALL order p: ((new orderQueue).indexOf(p) < (new orderQueue).indexOf(order) ->
	 * 										SchedulingStrategy.compare(p, order) <= 0) &&
	 * 									((new orderQueue).indexOf(p) > (new orderQueue).indexOf(order) ->
	 * 										SchedulingStrategy.compare(p, order) >= 0) 
	 */
	public abstract void addTo(Order order, List<Order> orderQueue);
	
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
	public abstract int compare(Order o, Order p);
}