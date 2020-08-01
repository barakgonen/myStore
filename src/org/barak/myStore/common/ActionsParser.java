package org.barak.myStore.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import org.barak.myStore.common.StockedProduct;
import org.json.simple.JSONObject;

/**
 * @author Barak
 * This class converts free text actions to "schema based actions" for bridging client server communication
 */
public final class ActionsParser {
	
	private static InputStreamReader userInput;
	private static BufferedReader userInputReader;
	
	protected ActionsParser() {
		
	}
	
	// parse for query
	@SuppressWarnings("unchecked")
	public static String parseClientRequest(String methodToExecute) throws IOException {
		MethodToExecute methodEnum = getMethodToExecute(methodToExecute);
		if (methodEnum != MethodToExecute.UKNOWN) {
			JSONObject jo = new JSONObject();
			jo.put(Constants.METHOD_TO_EXECUTE, methodEnum.label);
			// add parameters
			switch (methodEnum) {
				case ADD:
					addNewProduct(jo);
					break;
				case DELETE:
					deleteProduct(jo);
					break;
				default:
					break;
			}
			return jo.toJSONString();
		}
		return null;
	}
	
	public static StockedProduct json2Product(JSONObject jo) {
		String productName = (String) jo.get(Constants.PRODUCT_NAME);
		long productCode = (long) jo.get(Constants.PRODUCT_CODE);
		double productPrice = (double) jo.get(Constants.PRODUCT_PRICE);
		long availableUnits = (long)jo.get(Constants.AVAILABLE_UNITS);
		if (productName != null && 
			!(Objects.isNull(productCode)) && 
			!(Objects.isNull(productPrice)))
			return new StockedProduct(new Product(productName, productCode, productPrice), (int)availableUnits);
		return null;
	}
	
	public static void product2Json(JSONObject jo, StockedProduct stockedProduct) {
		JSONObject productJson = new JSONObject();
		productJson.put(Constants.PRODUCT_NAME, stockedProduct.getProduct().getProductName());
		productJson.put(Constants.PRODUCT_PRICE, stockedProduct.getProduct().getProductPrice());
		productJson.put(Constants.PRODUCT_CODE, stockedProduct.getProduct().getProductCode());
		productJson.put(Constants.AVAILABLE_UNITS, stockedProduct.getAvailability());
		jo.put(Constants.PRODUCT, productJson);
	}
	
	private static void addNewProduct(JSONObject jo) throws IOException {
		StockedProduct productFromUser = getProductFromUser();
		product2Json(jo, productFromUser);
	}
	
	private static StockedProduct getProductFromUser() throws IOException {
		userInput = new InputStreamReader(System.in);
		userInputReader = new BufferedReader(userInput);
		System.out.println("Insert product name:");
		String productName = userInputReader.readLine();
		System.out.println("Insert product price:");
		double productPrice = Double.parseDouble(userInputReader.readLine());
		System.out.println("Insert product code: ");
		long productCode = Long.parseLong(userInputReader.readLine());
		System.out.println("Insert number of current available units:");
		int availableUnits = Integer.parseInt(userInputReader.readLine());
		return new StockedProduct(new Product(productName, productCode, productPrice), availableUnits);
	}
	
	private static void deleteProduct(JSONObject jo) throws IOException {
		userInput = new InputStreamReader(System.in);
		userInputReader = new BufferedReader(userInput);
		System.out.println("Insert product name:");
		String productName = userInputReader.readLine();
		product2Json(jo, new StockedProduct(new Product(productName, 0, 0), 0));
	}
	
	private static MethodToExecute getMethodToExecute(String methodToExecute) {
		MethodToExecute isValidRequest = MethodToExecute.UKNOWN;
		switch (methodToExecute) {
			case Constants.ADD:
				isValidRequest = MethodToExecute.ADD;
				break;
			case Constants.DELETE:
				isValidRequest = MethodToExecute.DELETE;
				break;
			case Constants.STOP_CONNECTION:
				isValidRequest = MethodToExecute.STOP_CONNECTION;
				break;
			case Constants.QUERY:
				isValidRequest = MethodToExecute.QUERY;
				break;
			default:
				isValidRequest = MethodToExecute.UKNOWN;
				break;
		}
		return isValidRequest;
	}
}
