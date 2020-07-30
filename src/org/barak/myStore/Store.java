package org.barak.myStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Barak
 *
 * This class runs the store - it holds a collection of items and their available stock - 
 * Collection<StockerItem>
 */
public class Store {
	private Collection<StockedProduct> productsAtStore;
	
	public Store() {
		productsAtStore = new ArrayList<StockedProduct>();
	}
	
	public void addNewItem(Product product) {
		productsAtStore.add(new StockedProduct(product));
	}
	
	public void addNewItem(Product product, int availability) {
		productsAtStore.add(new StockedProduct(product, availability));
	}
	
	public void updateAvailableStock(String productName, int newAvailability) {
		_updateAvailableStock(productsAtStore.stream().findFirst().filter(s -> s.getProductName().equals(productName)).get(), newAvailability);
	}
	
	public void updateAvailableStock(long productCode, int newAvailability) {
		_updateAvailableStock(productsAtStore.stream().findFirst().filter(s -> s.getProductCode() == productCode).get(), newAvailability);
	}
	
	public void updateAvailableStock(Product product, int newAvailability) {
		_updateAvailableStock(productsAtStore.stream().findFirst().filter(s -> s.getProduct().equals(product)).get(), newAvailability);
	}
	
	public void deleteProduct(String productName) {
		_deleteItem(productsAtStore.stream().findFirst().filter(s -> s.getProductName().equals(productName)).get());
	}
	
	public void deleteProduct(long productCode) {
		_deleteItem(productsAtStore.stream().findFirst().filter(s -> s.getProductCode() == productCode).get());
	}
	
	public void deleteProduct(Product product) {
		_deleteItem(productsAtStore.stream().findFirst().filter(s -> s.getProduct().equals(product)).get());
	}
		
	public void printAllAvailableProducts() {
		System.out.println("Available products are: ");
		productsAtStore.forEach(s -> s.toString());
	}
	
	public Order makeNewOrder(HashMap<Product, Integer> productsInOrder) {
		ArrayList<Product> flatProductsList = new ArrayList<Product>();
		productsInOrder.forEach((k, v) -> {
											for (int i = 0; i < v; i++) 
												flatProductsList.add(k);
										});
		return new Order(flatProductsList);
	}
	
	private void _deleteItem(StockedProduct stockedProduct) {
		productsAtStore.remove(stockedProduct);
	}
	
	private void _updateAvailableStock(StockedProduct stockedProduct, int newAvailablity) {
		productsAtStore.stream().findFirst().filter(f -> f.equals(stockedProduct)).get().setAvailability(newAvailablity);
	}
}
