package org.barak.myStore.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.barak.myStore.common.ActionsParser;
import org.barak.myStore.common.Constants;

public class StoreClient {

	private static Socket socket;
	private static InputStreamReader userInput;
	private static BufferedReader userInputReader;
	private static DataOutputStream out;
	private static DataInputStream in;

	private static int totalMessagesSent;
	private static int totalMessagesReceived;
	
	public static void startClient(String address, int port) 
	{
		try {
			initializeMembers(address, port);	
			
			// Send messages to store's server until user wants to end it
			sendMessages();
			
			// close the connection
			closeConnection();
			
			printConnectionStatistics();
		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}
	}
	
	private static void initializeMembers(String address, int port) throws UnknownHostException, IOException {
		totalMessagesReceived = 0;
		totalMessagesSent = 0;
		socket = new Socket(address, port);

		// takes input from terminal
		userInput = new InputStreamReader(System.in);
		
		// Setting buffered reader to read from the above input stream reader
		userInputReader = new BufferedReader(userInput);

		// sends output to the socket
		out = new DataOutputStream(socket.getOutputStream());

		// reads input from the socket
		in = new DataInputStream(socket.getInputStream());
	}
		
	private static void sendMessages() throws IOException {
		String inputMessage = "";
		showOptions();
		while (!(inputMessage.equals(Constants.STOP_CONNECTION))) {
			inputMessage = getActionFromUser();
			sendMessageAndResponse(inputMessage);
		}
	}
	
	private static void showOptions() {
		System.out.println("	Store options: type your desired option");
		System.out.println("	add -> add new item, you will be requested to insert paramertes such as code, price, name & availability");
		System.out.println("	delete -> delete an item from the store, you will be requested to insert the name of the product to delete");
		System.out.println("	query -> query all the items in the store, both available in stock, and not");
		System.out.println("	type HELP in every step to show the menu again!");
	}
	
	private static String getActionFromUser() throws IOException {
		String input = userInputReader.readLine();
		return input.toUpperCase();
	}
	
	private static void sendMessageAndResponse(String requstedAction) throws IOException {
		if (requstedAction.equals("HELP"))
			showOptions();
		else {
			String command = ActionsParser.parseClientRequest(requstedAction);
			if (command != null) {
				out.writeUTF(command);
				totalMessagesSent++;
				String response = in.readUTF();
				if (response != null) {
					System.out.println(response);
					totalMessagesReceived++;
				}
			} else {
				System.out.println("<Error> you have chosen wrong action which the server does not know how to handle. ignoring your request!");
			}	
		}
	}
	
	private static void closeConnection() throws IOException {
		sendMessageAndResponse(Constants.STOP_CONNECTION);
		userInputReader.close();
		out.close();
		socket.close();
	}
	
	private static void printConnectionStatistics() {
		System.out.println("Total messages sent to server: " + totalMessagesSent);
		System.out.println("Total messages received from server: " + totalMessagesReceived);
	}
}
