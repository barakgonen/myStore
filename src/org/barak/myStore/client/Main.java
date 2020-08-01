package org.barak.myStore.client;

import org.barak.myStore.common.Constants;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hi im client");
		int port = Constants.SERVER_PORT;
		String address = Constants.SERVER_IP;
		StoreClient myStoreClient = new StoreClient(address, port);
	}

}
