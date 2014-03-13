package interfaceLayer;

import java.util.Comparator;
import domain.DateTime;
import domain.OrderContainer;

public class OrderDatesDescendingComparator implements Comparator<OrderContainer> {
	
	@Override
	public int compare(OrderContainer o1, OrderContainer o2) {
		DateTime dt1 = o1.getEstimatedCompletionTime();
		DateTime dt2 = o1.getEstimatedCompletionTime();
		if(Integer.compare(dt2.days, dt1.days) == 0){
			if(Integer.compare(dt2.hours, dt1.hours) == 0){
				return Integer.compare(dt2.minutes, dt1.minutes);
			} else {
				return Integer.compare(dt2.hours, dt1.hours);
			}
		} else {
			return Integer.compare(dt2.days, dt1.days);
		}
	}
}