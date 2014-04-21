package domain;

import java.util.ArrayList;
import java.util.List;

import domain.Order;
import domain.order.OrderContainer;

//TODO new in iteration 2
public class CompletedOrderCatalog {
	
	private List<Order> completedOrders = new ArrayList<Order>();

	public List<OrderContainer> getCompletedOrderContainers() {
		return new ArrayList<OrderContainer>(completedOrders);
	}
	
	private List<Order> getCompletedOrders() {
		return this.completedOrders;
	}
	
	public void addCompletedOrder(Order order) {
		this.getCompletedOrders().add(order);
	}
}