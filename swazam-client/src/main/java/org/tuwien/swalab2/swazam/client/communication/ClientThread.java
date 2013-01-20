package org.tuwien.swalab2.swazam.client.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tuwien.swalab2.swazam.client.Client;
import org.tuwien.swalab2.swazam.peer.SearchReplyMessage;

public class ClientThread extends Thread {

    private Socket socket = null;
    private SearchReplyMessage replyMessage;
    private ClientRestClient clientRestClient;

    public ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }

    public void run() {

        System.out.println("DEBUG: Received a SearchReplyMessage...");

        ObjectInputStream in = null;
        InputStream is = null;
        
        try {
        	is = socket.getInputStream();
        	in = new ObjectInputStream(is);
        	replyMessage = (SearchReplyMessage) in.readObject();
        } catch (Exception ex) {
        	//Exceptions at this point are an error
        	ex.printStackTrace();
        	Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (replyMessage.getFilename().contains("No results found.")) {
            System.out.println(replyMessage.getFilename());  
            //record transaction on server
            
            //clientRestClient = new ClientRestClient();
            //clientUserId =  clientRestClient.updateAccount(String searchTerm, 0, String userId)
            //clientRestClient.close(); 
            
        } else {
            System.out.println("filename: " + replyMessage.getFilename() + " (found by peer " + replyMessage.getSender().toString() + ":" + replyMessage.getSenderPort() + ")");
            //record transaction on server
            
            //clientRestClient = new ClientRestClient();
            //clientUserId =  clientRestClient.updateAccount(String searchTerm, 1, String userId)
            //clientRestClient.close();
        }

        //Close IO stuff we dont care for an exception as we alread have the message
        try {
            System.out.println("DEBUG: Closing IO stuff");
            in.close();
            is.close();
            this.socket.close();
            System.out.println("DEBUG: Closed IO stuff");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
