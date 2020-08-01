package org.barak.myStore.common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CommandToExectue {
	private MethodToExecute action;
	
	private StockedProduct product;
	
	public CommandToExectue(String commandStrJson) {
		System.out.println("Received command: " + commandStrJson);
		try {
			Object obj = new JSONParser().parse(commandStrJson);
			JSONObject jo = (JSONObject)obj;
			
			String methodToExecute = (String) jo.get(Constants.METHOD_TO_EXECUTE);
			
			switch (methodToExecute) {
				case Constants.ADD:
					action = MethodToExecute.ADD;
				break;
				case Constants.DELETE:
					action = MethodToExecute.DELETE;
				break;
				case Constants.QUERY:
					action = MethodToExecute.QUERY;
				break;
				case Constants.STOP_CONNECTION:
					action = MethodToExecute.STOP_CONNECTION;
					break;
			}
			parseProductFromMsg(jo.get(Constants.PRODUCT));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MethodToExecute getMethodToExecute() {
		return action;
	}
	
	public StockedProduct getProduct() {
		return product;
	}
	
	private void parseProductFromMsg(Object object) {
		if (object != null) {
			JSONObject obj = (JSONObject)object;
			product = ActionsParser.json2Product(obj);
		}
			  
	}
}
