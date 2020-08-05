package org.barak.myStore.server;

import java.util.Collection;
import java.util.HashMap;

import org.barak.myStore.common.Product;
import org.barak.myStore.common.StockedProduct;

// Generic interface - the adapters will implement db connection / in-memory
public interface IDataWriterReader {
	public void storeProduct(StockedProduct productToStore);
	public boolean setProductAvailability(String name, int newAvailability);
	public boolean setProductAvailability(Product product, int newAvailability);
	public boolean setProductAvailability(long productCode, int newAvailability);
	public boolean removeProduct(String name);
	public boolean removeProduct(long productCode);
	public boolean removeProduct(Product product);
	public Collection<StockedProduct> getAvailableProducts();
	public Collection<StockedProduct> getUnAvailableProducts();
	public Order makeNewOrder(HashMap<Product, Integer> productsInOrder);
	public boolean canDeliverOrder(HashMap<Product, Integer> productsInOrder);
	public Collection<Order> getAllOrders();
}
