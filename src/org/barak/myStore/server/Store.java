package org.barak.myStore.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.barak.myStore.common.Product;
import org.barak.myStore.common.StockedProduct;

import java.util.Optional;

/**
 * @author Barak
 *
 * This class runs the store - it holds a collection of items and their available stock - 
 * Collection<StockerItem>
 */

public class Store {
	private IDataWriterReader dataHandler;
	public Store(IDataWriterReader data) {
		dataHandler = data;
	}
	
	public void addNewItem(Product product) {
		dataHandler.storeProduct(new StockedProduct(product, 0));
	}
	
	public void addNewItem(Product product, int availability) {
		dataHandler.storeProduct(new StockedProduct(product, availability));
	}
	
	public void updateAvailableStock(String productName, int newAvailability) {
		dataHandler.setProductAvailability(productName, newAvailability);
	}
	
	public void updateAvailableStock(long productCode, int newAvailability) {
		dataHandler.setProductAvailability(productCode, newAvailability);
	}
		
	public void updateAvailableStock(Product product, int newAvailability) {
		dataHandler.setProductAvailability(product, newAvailability);
	}
	
	public void deleteProduct(String productName) {
		dataHandler.removeProduct(productName);
	}
	
	public void deleteProduct(long productCode) {
		dataHandler.removeProduct(productCode);
	}
	
	public void deleteProduct(Product product) {
		dataHandler.removeProduct(product);
	}
		
	public Order makeNewOrder(HashMap<Product, Integer> productsInOrder) {
		return dataHandler.makeNewOrder(productsInOrder);
	}
			
	public Collection<StockedProduct> getAvailableProducts() {
		return dataHandler.getAvailableProducts();
	}
	
	public Collection<StockedProduct> getUnAvailableProducts(){
		return dataHandler.getUnAvailableProducts();
	}
	
	public Collection<Order> getAllOrders(){
		return dataHandler.getAllOrders();
	}
	
	public void printProfitsReports() {
		System.out.println("PROFITS:");
		double sum = 0;
		Collection<Order> allOrders = dataHandler.getAllOrders();
		allOrders.forEach(o -> System.out.println(o));
		for (Order o : allOrders)
			sum += o.getTotalPrice();
		System.out.println("Total profit is: " + sum);	
	}
	

}
