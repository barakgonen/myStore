package org.barak.myStore.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.barak.myStore.common.ActionsParser;
import org.barak.myStore.common.CommandToExectue;
import org.barak.myStore.common.Constants;

public class StoreClient {
	
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream out;
	
	public StoreClient(String address, int port) {
		// establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
  
            // takes input from terminal 
            input  = new DataInputStream(System.in); 
  
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream()); 
        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
  
        // string to read message from input 
        String line = ""; 
  
        // keep reading until "Over" is input 
        while (!line.equals(Constants.STOP_WORD)) 
        { 
            try
            { 
                line = input.readLine();
                String command = ActionsParser.parseClientRequest(line);
                if (command != null)
                	out.writeUTF(command); 
            } 
            catch(IOException i) 
            { 
                System.out.println(i); 
            } 
        } 
  
        // close the connection 
        try
        { 
            input.close(); 
            out.close(); 
            socket.close();
            System.out.println("Disconnected");
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
        
	}
}
