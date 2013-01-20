package org.tuwien.swalab2.swazam.client.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class TcpDispatcher extends Thread {

    ServerSocket server;

    public TcpDispatcher(ServerSocket serverSocket) {
        this.server = serverSocket;
        this.start();
    }

    public void run() {

        Socket client = null;

        try {
            while (true) {
                client = server.accept();
                ClientThread c  = new ClientThread(client);  
            }

        } catch (SocketException e) {
            System.out.println("DEBUG: SocketException occurred.");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}