package org.barak.myStore.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.barak.myStore.common.ActionsParser;
import org.barak.myStore.common.CommandToExectue;
import org.barak.myStore.common.Constants;

public class StoreServer {
	
	private Socket socket;
	private ServerSocket server;
	private DataInputStream in;
	private Store myStore;
	
	public StoreServer(int port, Store myStore) {
		try {
			this.myStore = myStore;
			server = new ServerSocket(port);
			System.out.println("Server started!");
			
			socket = server.accept();
			System.out.println("Client accepted");
			
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			String line = "";
			while (!(line.equals(Constants.STOP_WORD))) {
				line = in.readUTF(); 
				CommandToExectue cmd = ActionsParser.parseJsonRequestToCommandToExecute(line);
				
				switch (cmd.getMethodToExecute()) {
				case ADD:
					break;
				case DELETE:
					break;
				case QUERY:
					System.out.println(myStore.getAvailableProducts());
					break;
				}
				
			}
            System.out.println("Closing connection"); 
            socket.close();
            in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
