package org.barak.myStore;

/**
 * @author Barak
 *
 * This class represents a single product at my store.
 */
public class Product {
	private final String productName;
	private final long productCode;
	private final double productPrice;
	
	public Product(final String name, final long code, final double price) {
		productName = name;
		productCode = code;
		productPrice = price;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public long getProductCode() {
		return productCode;
	}
	
	public double getProductPrice() {
		return productPrice;
	}
	
	@Override
	public String toString() {
		return "Name: " + productName + ", Code: " + productCode + ", Price: " + productPrice;
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (!(other instanceof Product))
			return false;
		
		Product p = (Product)other;
		
		return p.productName.equals(productName) && 
				p.productCode == productCode && 
				p.productPrice == productPrice;
	}
	
}
