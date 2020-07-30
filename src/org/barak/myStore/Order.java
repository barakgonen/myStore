package org.barak.myStore;

import java.util.Collection;
import java.util.Date;
/**
 * @author Barak
 * This class represents single oreder from my store
 */
import java.util.HashMap;
import java.util.Random;
public class Order {
	private final long orderNumber;
	private final Date purchaseDate;
	private Collection<Product> orderedItems;
	
	public Order(Collection<Product> orderedItems) {
		Random rnd = new Random();
		orderNumber = rnd.nextInt();
		purchaseDate = new Date();
		orderedItems = orderedItems;
	}
	
	public int getTotalPrice() {
		return 
	}
}
