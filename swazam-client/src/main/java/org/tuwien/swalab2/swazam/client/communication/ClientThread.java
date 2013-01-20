package org.tuwien.swalab2.swazam.client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tuwien.swalab2.swazam.client.Client;
import org.tuwien.swalab2.swazam.peer.SearchReplyMessage;

public class ClientThread extends Thread {

    private Socket socket = null;
    private SearchReplyMessage replyMessage;

    public ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }

    public void run() {

        System.out.println("Received a SearchReplyMessage...");

        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(socket.getInputStream());

            try {
                replyMessage = (SearchReplyMessage) in.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //TODO: Ergebnisse der diversen Peers sammeln und dann das beste auswählen

            if (replyMessage.getFilename().contains("No results found.")) {
             System.out.println(replyMessage.getFilename());   
            }
            else {
                System.out.println("filename: " + replyMessage.getFilename() + " (found by peer " + replyMessage.getSender().toString() + ":" + replyMessage.getSenderPort() + ")");
            }       

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
