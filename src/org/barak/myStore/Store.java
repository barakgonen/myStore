package org.barak.myStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author Barak
 *
 * This class runs the store - it holds a collection of items and their available stock - 
 * Collection<StockerItem>
 */

public class Store {
	private HashMap<Product, Integer> productsAtStore;
	private Collection<Order> orders;
	
	public Store() {
		productsAtStore = new HashMap<Product, Integer>();
		orders = new ArrayList<Order>();
	}
	
	public void addNewItem(Product product) {
		productsAtStore.put(product, 0);
	}
	
	public void addNewItem(Product product, int availability) {
		productsAtStore.put(product, availability);
	}
	
	public void updateAvailableStock(String productName, int newAvailability) {
		_updateAvailableStock(productsAtStore.entrySet().stream().filter(f-> f.getKey().getProductName().equals(productName)).findFirst(), newAvailability);
	}
	
	public void updateAvailableStock(long productCode, int newAvailability) {
		_updateAvailableStock(productsAtStore.entrySet().stream().filter(f-> f.getKey().getProductCode() == productCode).findFirst(), newAvailability);
	}
		
	public void updateAvailableStock(Product product, int newAvailability) {
		_updateAvailableStock(productsAtStore.entrySet().stream().filter(f-> f.getKey().equals(product)).findFirst(), newAvailability);
	}
	
	public void deleteProduct(String productName) {
		_deleteItem(productsAtStore.entrySet().stream().findFirst().filter(s -> s.getKey().getProductName().equals(productName)).orElse(null));
	}
	
	public void deleteProduct(long productCode) {
		_deleteItem(productsAtStore.entrySet().stream().findFirst().filter(s -> s.getKey().getProductCode() == productCode).orElse(null));
	}
	
	public void deleteProduct(Product product) {
		_deleteItem(productsAtStore.entrySet().stream().findFirst().filter(s -> s.getKey().equals(product)).orElse(null));
	}
	
	public void printAllAvailableProducts() {
		System.out.println("Available products are: ");
		if (productsAtStore.isEmpty())
			System.out.println("Store is empty");
		else
			productsAtStore.forEach((s, v) -> s.toString());
	}
	
	public Order makeNewOrder(HashMap<Product, Integer> productsInOrder) {
		ArrayList<Product> flatProductsList = new ArrayList<Product>();
		if (canDeliverOrder(productsInOrder)) {
			productsInOrder.forEach((k, v) -> {
				for (int i = 0; i < v; i++) 
					flatProductsList.add(k);
			});
			Order order = new Order(flatProductsList);
			orders.add(order);
			return order;
		}
		return null;
	}
	
	private boolean canDeliverOrder(HashMap<Product, Integer> productsInOrder) {
		if (productsAtStore.isEmpty())
			return false;
		for (Entry<Product, Integer> product : productsInOrder.entrySet())
			if (productsAtStore.containsKey(product.getKey()) && productsAtStore.get(product.getKey()) < product.getValue())
					return false;
		return true;
	}
		
	public Collection<StockedProduct> getAvailableProducts() {
		Collection<StockedProduct> availableProducts = new ArrayList<StockedProduct>();
		productsAtStore.entrySet().stream().filter(s -> s.getValue() > 0).forEach(a -> availableProducts.add(new StockedProduct(a.getKey(), a.getValue())));
		return availableProducts;
	}
	
	public Collection<StockedProduct> getUnAvailableProducts(){
		Collection<StockedProduct> unavailableProducts = new ArrayList<StockedProduct>();
		productsAtStore.entrySet().stream().filter(s -> s.getValue() == 0).forEach(a -> unavailableProducts.add(new StockedProduct(a.getKey(), a.getValue())));
		return unavailableProducts;
	}
	
	public Collection<Order> getAllOrders(){
		return orders;
	}
	
	public void printProfitsReports() {
		System.out.println("PROFITS:");
		double sum = 0;
		orders.forEach(o -> System.out.println(o));
		for (Order o : orders)
			sum += o.getTotalPrice();
		System.out.println("Total profit is: " + sum);	
	}
	
	private void _deleteItem(Entry<Product, Integer> stockedProduct) {
		if (stockedProduct != null)
			productsAtStore.remove(stockedProduct);
	}
		
	private void _updateAvailableStock(Optional<Entry<Product, Integer>> productToUpdate, int newAvailability) {
		if (productToUpdate.isPresent())
			productsAtStore.put(productToUpdate.get().getKey(), newAvailability);
	}
}
