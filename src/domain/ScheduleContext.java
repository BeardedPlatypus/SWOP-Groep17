package domain;

import java.util.ArrayList;
import domain.Order;

public class ScheduleContext {
	public ArrayList<Order> pendingOrders = new ArrayList<Order>();
	public SchedulingStrategy strategy;
}