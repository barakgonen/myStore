package org.barak.myStore;

import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		boolean quietMode = true;
		if (TestsClass.runAllTests(quietMode)) {
			
			// New store creation
			Store myStore = new Store();
			
			// Verify it has created without any product and order
			System.out.println("Available products: " + myStore.getAvailableProducts());
			System.out.println("Unavailable products: " + myStore.getUnAvailableProducts());
			System.out.println("All Orders: " + myStore.getAllOrders());
			
			System.out.println("Thre are all empty as expected :-}");
			
			// Creation of new products to put in the store
			Product table = new Product("Table", 1234, 40);
			Product chair = new Product("Chair", 123, 10);
			Product laptop = new Product("laptop", 112, 200);
			Product phone = new Product("Phone", 12, 100);
			
			System.out.println("Adding products to the store");
			// Adding in 2 ways - just an item, and item with available stock / quantity
			myStore.addNewItem(table);
			myStore.addNewItem(phone);
			myStore.addNewItem(laptop, 20);
			myStore.addNewItem(chair, 45);
			
			System.out.println("Lets look at our store products now:");
			System.out.println("Available products: " + myStore.getAvailableProducts());
			System.out.println("Unavailable products: " + myStore.getUnAvailableProducts());
			
			// Update number of available tables and phones:
			// Uniq API - let the user choose to use Product object, code or name 
			myStore.updateAvailableStock(table.getProductCode(), 100);
			myStore.updateAvailableStock(phone.getProductName(), 2);
			System.out.println("Lets look at our store products after the update:");
			System.out.println("Available products: " + myStore.getAvailableProducts());
			System.out.println("Unavailable products: " + myStore.getUnAvailableProducts());
			
			// Delete chairs from the store
			myStore.deleteProduct(chair.getProductCode());
			System.out.println("Available products: " + myStore.getAvailableProducts());
			System.out.println("Unavailable products: " + myStore.getUnAvailableProducts());
			System.out.println("Chairs has gone, as expected");
			
			// Creation of new order
			HashMap<Product, Integer> orderSpec = new HashMap<>();
			orderSpec.put(table, 10);
			orderSpec.put(phone, 1);
			Order o = myStore.makeNewOrder(orderSpec);
			if (o != null) {
				System.out.println("Order has been completed! ");
				System.out.println(o);
			} else {
				System.out.println("Order has failed");
			}
			// Store status after order
			System.out.println("Available products: " + myStore.getAvailableProducts());
			System.out.println("Unavailable products: " + myStore.getUnAvailableProducts());
				
			// Reports:
			
			// Total incomes
			System.out.println("Total incomes:");
			myStore.printProfitsReports();
			
			// Available products
			System.out.println("Available products:");
			System.out.println("Available products: " + myStore.getAvailableProducts());

			// Unavailable products
			myStore.updateAvailableStock(laptop, 0);
			System.out.println("Unavailable products: (printing available and unavailable to see differences");
			System.out.println("Available products: " + myStore.getAvailableProducts());
			System.out.println("Unavailable products: " + myStore.getUnAvailableProducts());
		} else {
			System.out.println("Running tests failed, not starting app");
		}
	}
}
