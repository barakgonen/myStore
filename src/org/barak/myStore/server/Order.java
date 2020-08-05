package org.barak.myStore.server;

import java.util.Collection;
import java.util.Date;
/**
 * @author Barak
 * This class represents single order from my store
 */

import java.util.Random;

import org.barak.myStore.common.Product;

public class Order {
	private final long orderNumber;
	private final Date orderDate;
	private Collection<Product> orderedItems;
	private double totalAmount;
	
	public Order(Collection<Product> itemsInOrder) {
		Random rnd = new Random();
		orderNumber = rnd.nextInt(1000000 - 1) + 1;
		orderDate = new Date();
		orderedItems = itemsInOrder;
		totalAmount = orderedItems.stream().mapToDouble(x->x.getProductPrice()).sum();
	}
	
	@SuppressWarnings("deprecation")
	public Order(int orderNumber, String orderDateStr, String ordredItems, double totalAmount) {
		this.orderNumber = orderNumber;
		orderDate = new Date(orderDateStr);
		orderedItems = null;
		this.totalAmount = totalAmount;
	}
		
	public double getTotalPrice() {
		return totalAmount;
	}
	
	public long getOrderNumber() {
		return orderNumber;
	}
	
	public String getOrderDate() {
		return orderDate.toString();
	}
	
	public Collection<Product> getOrderedItems(){
		return orderedItems;
	}
	
	@Override
	public String toString() {
		return "Order number: " + orderNumber + ", order date: " + orderDate + ", total price: " + getTotalPrice();
	}
}
