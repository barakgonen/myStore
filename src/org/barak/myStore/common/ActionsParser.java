package org.barak.myStore.common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Barak
 * This class converts free text actions to "schema based actions" for bridging client server communication
 */
public final class ActionsParser {
	protected ActionsParser() {
		
	}
	
	// parse for query
	@SuppressWarnings("unchecked")
	public static String parseClientRequest(String methodToExecute) {
		boolean isValidRequest = true;
		JSONObject jo = new JSONObject();
		switch (methodToExecute) {
		case Constants.ADD:
		case Constants.DELETE:
		case Constants.QUERY:
			jo.put(Constants.METHOD_TO_EXECUTE, methodToExecute);
		break;
		default:
			isValidRequest = false;
			break;
		}
		
		return isValidRequest ? jo.toJSONString() : null;
	}
	
	
	
	public static String parseClientRequest(String methodToExecute, Product product) {
		return "";
	}
	
	public static CommandToExectue parseJsonRequestToCommandToExecute(String jsonStr) {
		System.out.println("Received command: " + jsonStr);
		CommandToExectue cmdToExecute = null;
		try {
			Object obj = new JSONParser().parse(jsonStr);
			JSONObject jo = (JSONObject)obj;
			
			String methodToExecute = (String) jo.get(Constants.METHOD_TO_EXECUTE);
			
			MethodToExecute method = null;
			switch (methodToExecute) {
				case Constants.ADD:
					method = MethodToExecute.ADD;
				break;
				case Constants.DELETE:
					method = MethodToExecute.DELETE;
				break;
				case Constants.QUERY:
					method = MethodToExecute.QUERY;
				break;
			}
			
			cmdToExecute = new CommandToExectue(method);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return cmdToExecute;
	}
}
