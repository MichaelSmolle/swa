package org.tuwien.swalab2.swazam.client.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class TcpDispatcher extends Thread {

    private ServerSocket server;
    private volatile boolean running;

    public TcpDispatcher(ServerSocket serverSocket) {
        this.server = serverSocket;
        this.running = true;
        this.start();
    }
    
    public void kill() {
    	this.running = false;
    	try {
			join();
		} catch (InterruptedException e) {}
    }

    public void run() {

        Socket client = null;

        try {
            while (this.running) {
                client = server.accept();
                ClientThread c  = new ClientThread(client);  
            }

        } catch (SocketException e) {
            System.out.println("DEBUG: SocketException occurred.");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //wait for client threads
        try {
			join();
		} catch (InterruptedException e) {}
    }
}