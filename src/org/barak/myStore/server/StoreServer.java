package org.barak.myStore.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.barak.myStore.common.ActionsParser;
import org.barak.myStore.common.CommandToExectue;
import org.barak.myStore.common.Constants;
import org.barak.myStore.common.Product;
import org.barak.myStore.common.StockedProduct;

public class StoreServer {

	private Socket socket;
	private ServerSocket server;
	private DataInputStream in;
	private DataOutputStream out;
	private Store myStore;

	private void initializeMembers(int port, Store myStore) throws IOException {
		this.myStore = myStore;
		server = new ServerSocket(port);
		System.out.println("Server started!");
	}

	private void establishConnection() throws IOException {
		socket = server.accept();
		System.out.println("Client accepted");
		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		out = new DataOutputStream(socket.getOutputStream());
	}

	private void handleRequests() throws IOException {
		String request = "";
		while (!(request.equals(Constants.STOP_CONNECTION))) {
			request = in.readUTF();
			CommandToExectue cmd = new CommandToExectue(request);
			String responeseMessage = "";
			switch (cmd.getMethodToExecute()) {
			case ADD:
				responeseMessage = addProductToStore(cmd.getProduct());
				break;
			case DELETE:
				responeseMessage = deleteProductToStore(cmd.getProduct());
				break;
			case QUERY:
				responeseMessage = getItemsList();
				break;
			case STOP_CONNECTION:
				request = Constants.STOP_CONNECTION;
				break;
			}
			out.writeUTF(responeseMessage);
			out.flush();
		}
	}

	private String getItemsList() {
		return "Available items: " + myStore.getAvailableProducts() + "\n" 
					+ "Unavailable items: " + myStore.getUnAvailableProducts();
	}
	private String deleteProductToStore(StockedProduct stockedProduct) {
		myStore.deleteProduct(stockedProduct.getProduct().getProductName());
		return "Product removed successfully!";
	}

	private String addProductToStore(StockedProduct stockedProduct) {
		myStore.addNewItem(stockedProduct.getProduct(), stockedProduct.getAvailability());
		return "Product added successfully!";
	}

	private void resetConnection() throws IOException {
		System.out.println("Closing connection");
		socket.close();
		in.close();
		out.close();
	}

	public StoreServer(int port, Store myStore) {
		try {
			initializeMembers(port, myStore);
			while (true) {
				establishConnection();
				handleRequests();
				resetConnection();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
