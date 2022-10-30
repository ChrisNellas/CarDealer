package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private Long id;
	String name, address;
	private List<Order> orderList;
	private double dept;

	public Customer(Long id, String name, String address, double dept) {
		orderList = new ArrayList<>();
		this.id = id;
		this.name = name;
		this.address = address;
		this.dept = dept;
	}

	public List<Order> getOrderList() {
		return orderList;
	}
	
	public void addNewOrder(Order order) {
		orderList.add(order);
		setDept(dept+order.getCost());
	}

	public double getDept() {
		return dept;
	}

	private void setDept(double dept) {
		this.dept = dept;
	}
	
	public void payUp(double money) {
		setDept(dept-money);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + ", address=" + address;
	}
}
