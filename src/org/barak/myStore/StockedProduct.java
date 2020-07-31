package org.barak.myStore;

/**
 * @author Barak
 *
 * This class represents stocked item - item at store with current inventory(?)
 */
public class StockedProduct {
	private final Product product;
	private int availableUnits;
	
	public StockedProduct(final Product product, int availableUnits) {
		this.product = product;
		this.availableUnits = availableUnits;
	}
	
	public StockedProduct(final Product product) {
		this(product, 0);
	}
	
	public void setAvailability(int newAvailablity) {
		availableUnits = newAvailablity;
	}
	
	public int getAvailability() {
		return availableUnits;
	}
	
	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return product.toString() + ", availableUnits: " + availableUnits;
	}
}
