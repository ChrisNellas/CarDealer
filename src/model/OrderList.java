package model;

import java.util.ArrayList;
import java.util.List;

public class OrderList {
	private Long nextId;
	private List<Order> orders;
	
	public OrderList() {
		nextId = 1L;
		orders = new ArrayList<>();
	}

	public Long getNextId() {
		nextId++;
		return (nextId-1);
	}

	public List<Order> getOrders() {
		return orders;
	}
	
//	public void addNewOrder(Order order) {
//		orders.add(order);
//		order.getBuyer().addNewOrder(order); // add the order to customer's list in order to see all the orders of each customer alone
//	}
}
