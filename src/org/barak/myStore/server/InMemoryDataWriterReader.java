package org.barak.myStore.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map.Entry;

import org.barak.myStore.common.Product;
import org.barak.myStore.common.StockedProduct;

public class InMemoryDataWriterReader implements IDataWriterReader {
	private HashMap<Product, Integer> productsAtStore;
	private Collection<Order> orders;

	public InMemoryDataWriterReader() {
		productsAtStore = new HashMap<Product, Integer>();
		orders = new ArrayList<Order>();
	}

	@Override
	public void storeProduct(StockedProduct productToStore) {
		// TODO Auto-generated method stub
		productsAtStore.put(productToStore.getProduct(), productToStore.getAvailability());
	}

	@Override
	public boolean setProductAvailability(String productName, int newAvailability) {
		_updateAvailableStock(productsAtStore.entrySet().stream()
				.filter(f -> f.getKey().getProductName().equals(productName)).findFirst(), newAvailability);
		return true;
	}

	@Override
	public boolean setProductAvailability(Product product, int newAvailability) {
		_updateAvailableStock(productsAtStore.entrySet().stream().filter(f -> f.getKey().equals(product)).findFirst(),
				newAvailability);
		return true;
	}

	@Override
	public boolean setProductAvailability(long productCode, int newAvailability) {
		_updateAvailableStock(
				productsAtStore.entrySet().stream().filter(f -> f.getKey().getProductCode() == productCode).findFirst(),
				newAvailability);
		return true;
	}

	@Override
	public boolean removeProduct(String productName) {
		_deleteItem(productsAtStore.entrySet().stream().findFirst()
				.filter(s -> s.getKey().getProductName().equals(productName)).orElse(null));
		return true;
	}

	@Override
	public boolean removeProduct(long productCode) {
		_deleteItem(productsAtStore.entrySet().stream().findFirst()
				.filter(s -> s.getKey().getProductCode() == productCode).orElse(null));
		return true;
	}

	@Override
	public boolean removeProduct(Product product) {
		_deleteItem(
				productsAtStore.entrySet().stream().findFirst().filter(s -> s.getKey().equals(product)).orElse(null));
		return true;
	}

	private void _deleteItem(Entry<Product, Integer> stockedProduct) {
		if (stockedProduct != null)
			productsAtStore.remove(stockedProduct.getKey());
	}

	private void _updateAvailableStock(Optional<Entry<Product, Integer>> productToUpdate, int newAvailability) {
		if (productToUpdate.isPresent())
			productsAtStore.put(productToUpdate.get().getKey(), newAvailability);
	}

	@Override
	public Collection<StockedProduct> getAvailableProducts() {
		Collection<StockedProduct> availableProducts = new ArrayList<StockedProduct>();
		productsAtStore.entrySet().stream().filter(s -> s.getValue() > 0).forEach(a -> availableProducts.add(new StockedProduct(a.getKey(), a.getValue())));
		return availableProducts;
	}

	@Override
	public Collection<StockedProduct> getUnAvailableProducts() {
		Collection<StockedProduct> unavailableProducts = new ArrayList<StockedProduct>();
		productsAtStore.entrySet().stream().filter(s -> s.getValue() == 0).forEach(a -> unavailableProducts.add(new StockedProduct(a.getKey(), a.getValue())));
		return unavailableProducts;		
	}

	@Override
	public Order makeNewOrder(HashMap<Product, Integer> productsInOrder) {
		ArrayList<Product> flatProductsList = new ArrayList<Product>();
		if (canDeliverOrder(productsInOrder)) {
			productsInOrder.forEach((k, v) -> {
				for (int i = 0; i < v; i++) 
					flatProductsList.add(k);
			});
			Order order = new Order(flatProductsList);
			orders.add(order);
			
			for (Product p : flatProductsList)
				productsAtStore.replace(p, productsAtStore.get(p), productsAtStore.get(p) -1);
			return order;
		}
		return null;
	}

	@Override
	public boolean canDeliverOrder(HashMap<Product, Integer> productsInOrder) {
		if (productsAtStore.isEmpty())
			return false;
		for (Entry<Product, Integer> product : productsInOrder.entrySet())
			if (productsAtStore.containsKey(product.getKey()) && productsAtStore.get(product.getKey()) < product.getValue())
					return false;
		return true;
	}

	@Override
	public Collection<Order> getAllOrders() {
		return orders;
	}
}
