package org.barak.myStore.server;

import java.util.HashMap;

import org.barak.myStore.common.Product;

public final class TestsClass {
	private boolean isQueitMode;
	private int numberOfPassedTests;
	private int numberOfFailedTests;
	private int totalTests;
	protected TestsClass(boolean quietMode) {
		isQueitMode = quietMode;
		numberOfPassedTests = 0;
		numberOfFailedTests = 0;
		totalTests = 0;
	}
	
	
	public static boolean runAllTests(boolean queitMode) {
		TestsClass tester = new TestsClass(queitMode);
		tester.assertStoreCreatedEmpty();
		tester.assertDeleteNonExistingValuesDoesNotCrashApp();
		tester.assertUpdateNonExistingItemsDoesNotCrashApp();
		tester.assertUpdateNonExistingProductDoesNotAddThemToStore();
		tester.assertOrderToNonExistingItemFails();
		tester.assertOrderCantBeHandledNotHandled();
		tester.assertInserProductWithNoQuantitySetsAsUnavailable();
		tester.assertInserProductStockWorksAsExpected();
		tester.assertUpdateAmountWorks();
		tester.assertHandlingTwoProducts();
		tester.assertProfitsReportIsCorrect();
		tester.printMessage("Total tests: " + tester.totalTests + ", Passed: " + tester.numberOfPassedTests + ", Failed: " + tester.numberOfFailedTests);
		return tester.totalTests == tester.numberOfPassedTests;
	}
	
	private void printMessage(String str) {
		if (!isQueitMode)
			System.out.println(str);
	}
	
	private void assertSimpleTest(double expectedValue, double actualValue, String error, String testName) {
		if (actualValue != expectedValue) {
			System.out.println(error + ". Expected: " + expectedValue + ", Actual: " + actualValue);
			numberOfFailedTests++;
		}
		else {			
			printMessage(testName + " has passed!");
			numberOfPassedTests += 1;
		}
		totalTests++;
	}
	
	private void assertStoreCreatedEmpty() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		assertSimpleTest(0, myStore.getAllOrders().size(), "Number of orders is different", "Assert orders created as empty list");
		assertSimpleTest(0, myStore.getAvailableProducts().size(), "Number of available products is different", "Assert available items list is empty when store created");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Number of unavailable products is different", "Assert unavailable items list is empty when store created");
	}
	
	private void assertDeleteNonExistingValuesDoesNotCrashApp() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		myStore.deleteProduct(213);
		numberOfPassedTests += 1;
		totalTests++;
		myStore.deleteProduct(tv);
		numberOfPassedTests += 1;
		totalTests++;
		myStore.deleteProduct("Shoes");
		numberOfPassedTests += 1;
		totalTests++;
	}
	
	private void assertUpdateNonExistingItemsDoesNotCrashApp() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		myStore.updateAvailableStock(tv, 14);
		numberOfPassedTests += 1;
		totalTests++;
		myStore.updateAvailableStock(12345, 1);
		numberOfPassedTests += 1;
		totalTests++;
		myStore.updateAvailableStock("Book", 12);
		numberOfPassedTests += 1;
		totalTests++;
	}
	
	private void assertUpdateNonExistingProductDoesNotAddThemToStore() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		myStore.updateAvailableStock(tv, 14);
		assertSimpleTest(0, myStore.getAvailableProducts().size(), "Number of available products is different", "Assert available items list stays empty when update unexisting product");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Number of unavailable products is different", "Assert unavailable items list stays empty when update unexisting product");
		myStore.updateAvailableStock(12345, 1);
		assertSimpleTest(0, myStore.getAvailableProducts().size(), "Number of available products is different", "Assert available items list stays empty when update unexisting product");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Number of unavailable products is different", "Assert unavailable items list stays empty when update unexisting product");
		myStore.updateAvailableStock("Book", 12);
		assertSimpleTest(0, myStore.getAvailableProducts().size(), "Number of available products is different", "Assert available items list stays empty when update unexisting product");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Number of unavailable products is different", "Assert unavailable items list stays empty when update unexisting product");
		
		assertSimpleTest(0, myStore.getAllOrders().size(), "Number of orders is different", "Assert orders stays empty when there are no new orders");
	}
	
	private void assertOrderToNonExistingItemFails() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		HashMap<Product, Integer> productsToOrder = new HashMap<>();
		Product tv = new Product("Tv", 12345, 23);
		productsToOrder.put(tv, 23);
		Order fakeOrder = myStore.makeNewOrder(productsToOrder);
		if (fakeOrder != null)
			numberOfFailedTests++;
		else
			numberOfPassedTests++;
		totalTests++;
	}
	
	private void assertOrderCantBeHandledNotHandled() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		HashMap<Product, Integer> productsToOrder = new HashMap<>();
		Product tv = new Product("Tv", 12345, 23);
		Product car = new Product("Tv", 232, 1);
		myStore.addNewItem(tv);
		myStore.addNewItem(car, 5);
		productsToOrder.put(tv, 2);
		Order fakeOrder = myStore.makeNewOrder(productsToOrder);
		if (fakeOrder != null)
			numberOfFailedTests++;
		else
			numberOfPassedTests++;
		totalTests++;
	}
	
	private void assertInserProductWithNoQuantitySetsAsUnavailable() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		myStore.addNewItem(tv);
		assertSimpleTest(0, myStore.getAvailableProducts().size(), "Wrong number of items at store", "test add item works");
		assertSimpleTest(1, myStore.getUnAvailableProducts().size(), "Wrong number of items at store", "test add item works");
	}
	
	private void assertInserProductStockWorksAsExpected() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		myStore.addNewItem(tv, 4);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of items at store", "test add item works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of items at store", "test add item works");
	}
	
	private void assertUpdateAmountWorks() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		myStore.addNewItem(tv, 2);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(tv.getProductCode(), 5);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(tv.getProductName(), 15);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		assertSimpleTest(0, myStore.getAllOrders().size(), "Number of orders is different", "Assert orders stays empty when there are no new orders");
	}
	
	private void assertHandlingTwoProducts() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		Product tv = new Product("Tv", 12345, 23);
		Product car = new Product("Car", 123, 231212.12);
		myStore.addNewItem(tv, 2);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(tv.getProductCode(), 5);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(tv.getProductName(), 15);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.addNewItem(car, 13);
		assertSimpleTest(2, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(car.getProductCode(), 0);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(1, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(car, 12);
		assertSimpleTest(2, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(0, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(car.getProductName(), 0);
		assertSimpleTest(1, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(1, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		myStore.updateAvailableStock(tv.getProductCode(), 0);
		assertSimpleTest(0, myStore.getAvailableProducts().size(), "Wrong number of available products", "test update available quantity works");
		assertSimpleTest(2, myStore.getUnAvailableProducts().size(), "Wrong number of unavailable products", "test update available quantity works");
		assertSimpleTest(0, myStore.getAllOrders().size(), "Number of orders is different", "Assert orders stays empty when there are no new orders");
	}
	
	private void assertProfitsReportIsCorrect() {
		InMemoryDataWriterReader inMemryDataWriterReader = new InMemoryDataWriterReader();
		Store myStore = new Store(inMemryDataWriterReader);
		double carPrice = 231212.12;
		double tvPrice = 23;
		Product tv = new Product("Tv", 12345, tvPrice);
		Product car = new Product("Car", 123, carPrice);
		HashMap<Product, Integer> firstOrder = new HashMap<>();
		firstOrder.put(tv, 10);
		firstOrder.put(car, 15);
		HashMap<Product, Integer> secondOrder = new HashMap<>();
		secondOrder.put(car, 13);
		HashMap<Product, Integer> thirdOrder = new HashMap<>();
		thirdOrder.put(tv, 3);
		myStore.addNewItem(tv, 18);
		myStore.addNewItem(car);
		myStore.updateAvailableStock(car.getProductName(), 29);
		myStore.makeNewOrder(firstOrder);
		myStore.makeNewOrder(secondOrder);
		myStore.makeNewOrder(thirdOrder);
//		myStore.printProfitsReports();
		assertSimpleTest(carPrice * 28 + tvPrice * 13, 6474238.359999999, "calculationError", "");
		assertSimpleTest(3, myStore.getAllOrders().size(), "Number of orders is wrong", "test orders and print profits report");
	}
}
