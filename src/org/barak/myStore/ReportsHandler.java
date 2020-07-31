package org.barak.myStore;

public class ReportsHandler {
	
	private Store store;
	
	public ReportsHandler(Store store) {
		this.store = store;
	}
	
	public void printProfitsReports() {
		System.out.println("PROFITS:");
		store.getAllOrders().forEach(o -> System.out.println(o));
	}
	
	public void printAvailableStock() {
		System.out.println("AVAILABLE STOCK:");
		store.getAvailableProducts().stream().forEach(p -> p.toString());
	}
	
	public void printUnavailableProducts() {
		System.out.println("UNAVAILABLE STOCK:");
		store.getUnAvailableProducts().stream().forEach(p -> p.toString());
	}
}
