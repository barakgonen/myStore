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
	
	public Order(Collection<Product> itemsInOrder) {
		Random rnd = new Random();
		orderNumber = rnd.nextInt();
		orderDate = new Date();
		orderedItems = itemsInOrder;
	}
	
	public double getTotalPrice() {
		return orderedItems.stream().mapToDouble(x->x.getProductPrice()).sum();
	}
	
	@Override
	public String toString() {
		return "Order number: " + orderNumber + ", order date: " + orderDate + ", total price: " + getTotalPrice();
	}
}
