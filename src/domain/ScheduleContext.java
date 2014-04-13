package domain;

import java.util.ArrayList;
import domain.Order;

//TODO new in iteration 2
public class ScheduleContext {
	public ArrayList<Order> pendingOrders = new ArrayList<Order>();
	public SchedulingStrategy strategy;
}