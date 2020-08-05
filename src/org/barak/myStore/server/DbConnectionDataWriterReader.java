package org.barak.myStore.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.barak.myStore.common.Product;
import org.barak.myStore.common.StockedProduct;

public class DbConnectionDataWriterReader implements IDataWriterReader {

	private Connection dbConnector;

	public DbConnectionDataWriterReader() {
		String dbURL = "jdbc:sqlserver://localhost\\SQLExpress;user=sa;password=123456";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			dbConnector = DriverManager.getConnection(dbURL);
			if (dbConnector != null) {
//				System.out.println("Connected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void storeProduct(StockedProduct productToStore) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate("use strore INSERT INTO items VALUES (" + "'"
					+ productToStore.getProduct().getProductName() + "', "
					+ +productToStore.getProduct().getProductCode() + ","
					+ +productToStore.getProduct().getProductPrice() + "," + +productToStore.getAvailability() + ")");
		} catch (SQLException e) {
			String exception = e.getMessage();
			// If Violation of PRIMARY KEY constraint 'PK_items'. Cannot insert duplicate
			// key in object == tell user that item with the same name already exists
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

	@Override
	public boolean setProductAvailability(String name, int newAvailability) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate(
					"use strore UPDATE items SET availableItems = " + newAvailability + "WHERE name = '" + name + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean setProductAvailability(Product product, int newAvailability) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate("use strore UPDATE items SET availableItems = " + newAvailability
					+ " WHERE name = '" + product.getProductName() + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean setProductAvailability(long productCode, int newAvailability) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate(
					"use strore UPDATE items SET availableItems = " + newAvailability + " WHERE code = " + productCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeProduct(String name) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate("use strore DELETE FROM items WHERE name = '" + name + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeProduct(long productCode) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate("use strore DELETE FROM items WHERE code = " + productCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeProduct(Product product) {
		try {
			Statement dbStatements = dbConnector.createStatement();
			dbStatements.executeUpdate("use strore DELETE FROM items WHERE name = '" + product.getProductName() + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Collection<StockedProduct> getAvailableProducts() {
		ArrayList<StockedProduct> availableProducts = new ArrayList<>();
		Statement dbStatements;
		try {
			dbStatements = dbConnector.createStatement();
			ResultSet myRs = dbStatements.executeQuery("use strore select * from items where availableItems > 0");

			while (myRs.next()) {
				StockedProduct product = new StockedProduct(
						new Product(myRs.getString("name").replaceAll("\\s",""), myRs.getInt("code"), myRs.getDouble("price")),
						myRs.getInt("availableItems"));
				availableProducts.add(product);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return availableProducts;
	}

	@Override
	public Collection<StockedProduct> getUnAvailableProducts() {
		ArrayList<StockedProduct> availableProducts = new ArrayList<>();
		Statement dbStatements;
		try {
			dbStatements = dbConnector.createStatement();
			ResultSet myRs = dbStatements.executeQuery("use strore select * from items where availableItems = 0");

			while (myRs.next()) {
				StockedProduct product = new StockedProduct(
						new Product(myRs.getString("name").replaceAll("\\s",""), myRs.getInt("code"), myRs.getDouble("price")),
						myRs.getInt("availableItems"));
				availableProducts.add(product);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return availableProducts;
	}

	@Override
	public Order makeNewOrder(HashMap<Product, Integer> productsInOrder) {

		if (canDeliverOrder(productsInOrder)) {
			try {
				// Parse user request to order object
				Order orderToMake = placeOrder(productsInOrder);
	
				// Update store's stock
				if (updateStoreStock(orderToMake)) {
					String query = getOrderQuery(orderToMake);
					Statement dbStatements = dbConnector.createStatement();
					dbStatements.executeUpdate(query);	
				}
				
			} catch (SQLException e) {
				String exception = e.getMessage();
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// Parsing user request to order object
	private Order placeOrder(HashMap<Product, Integer> productsInOrder) {
		ArrayList<Product> flatProductsList = new ArrayList<Product>();
		productsInOrder.forEach((k, v) -> {
				for (int i = 0; i < v; i++) 
					flatProductsList.add(k);
		});
		return new Order(flatProductsList);
	}
	
	private boolean updateStoreStock(Order orderToPlace) {
		for (Product product : orderToPlace.getOrderedItems()) {
			try {
				Statement dbStatements = dbConnector.createStatement();			 
				dbStatements.execute("use strore UPDATE items set availableItems = availableItems - 1 where name = '" + product.getProductName() + "' and code = " + product.getProductCode());
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		return true;
	}
	
	
	private String orderLstToString(Collection<Product> products) {
		String productsStr = "";
		for (Product product : products) {
			productsStr += product.getProductCode() + " ";
		}
		productsStr.replaceAll("\\s","");
		return productsStr;
	}
	
	private String getOrderQuery(Order orderToPlace) {
		String orderQuery = "use strore INSERT INTO orders VALUES (";
		orderQuery += orderToPlace.getOrderNumber() + ",";
		orderQuery += "'" + orderToPlace.getOrderDate() + "',";
		orderQuery += "'";
		orderQuery += orderLstToString(orderToPlace.getOrderedItems());
		orderQuery += "'";
		orderQuery += ",";
		orderQuery += orderToPlace.getTotalPrice();
		orderQuery += ")";
		
		return orderQuery;
		
	}
	@Override
	public boolean canDeliverOrder(HashMap<Product, Integer> productsInOrder) {
		for (Entry<Product, Integer> product : productsInOrder.entrySet()) {
			Statement dbStatement;
			try {
				dbStatement = dbConnector.createStatement();
				ResultSet myRs = dbStatement.executeQuery("use strore select availableItems from items where name = '" + product.getKey().getProductName() + "' and code = " + product.getKey().getProductCode());
				while (myRs.next()) {
					int b = myRs.getInt("availableItems");
					if (b < product.getValue()) {
						System.out.println("Error, order could not be placed because user wants to buy: " 
											+ product.getValue() + " " + product.getKey().getProductName() 
											+ ((product.getValue() > 1) ? "s" : "") 
											+ " and there are just: " + b + " available at the store");
						return false;
					}
//					System.out.println("availability of: " + product.getKey().getProductName() + " is: " + myRs.getInt("availableItems"));
				}
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}

	@Override
	public Collection<Order> getAllOrders() {
		ArrayList<Order> orders = new ArrayList<>();
		Statement dbStatements;
		try {
			dbStatements = dbConnector.createStatement();
			ResultSet myRs = dbStatements.executeQuery("use strore select * from orders");

			while (myRs.next()) {
				int orderNumber = myRs.getInt("orderNumber");
				String orderDate = myRs.getString("orderDate");
				String orderdItems = myRs.getString("orderedItems");
				double totalAmount = myRs.getDouble("totalAmount");
				Order o = new Order(orderNumber, orderDate, orderdItems, totalAmount);
				orders.add(o);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return orders;
	}

}
