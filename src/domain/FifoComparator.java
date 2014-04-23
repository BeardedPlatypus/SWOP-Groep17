package domain;

import java.util.Comparator;

import domain.order.Order;

/**
 * Imposes an ordering on Order objects based on the time they were submitted to
 * the system.
 * @author Thomas Vochten
 *
 */
public class FifoComparator implements Comparator<Order> {

	@Override
	public int compare(Order order1, Order order2) {
		DateTime time1 = order1.getSubmissionTime();
		DateTime time2 = order2.getSubmissionTime();
		return time1.compareTo(time2);
	}
}
