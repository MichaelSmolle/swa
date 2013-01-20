package org.tuwien.swalab2.swazam.client.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            this.server.close();
        } catch (IOException ex) {
            Logger.getLogger(TcpDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        //try {
        //    join();
        //} catch (InterruptedException e) {
        //}
    }

    public void run() {

        Socket client = null;

        try {
            while (this.running) {
                client = server.accept();
                ClientThread c = new ClientThread(client);
            }

        } catch (SocketException e) {
            System.out.println("DEBUG: SocketException occurred.");
            System.out.println("DEBUG: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //wait for client threads
        System.out.println("DEBUG: wait for client threads");
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}