package org.tuwien.swalab2.swazam.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tuwien.swalab2.swazam.client.clientUI.Cli;
import org.tuwien.swalab2.swazam.client.clientUI.SwingUI;
import org.tuwien.swalab2.swazam.peer.SearchMessage;
import org.tuwien.swalab2.swazam.peer.SearchReplyMessage;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Client {

    private static SwingUI swingUI;
    private static Cli cli;
    private Socket socket = null;
    private InetAddress ip;
    private Integer port;
    private static ServerSocket serverSocket;
    private Socket initSocket = null;
    private ObjectOutputStream out = null;
 
    private static class ClientThread extends Thread {

        private Socket socket = null;
        private SearchReplyMessage replyMessage;

        public ClientThread(Socket socket) {
            this.socket = socket;
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

                System.out.println("filename: " + replyMessage.getFilename() + "(found by peer )" + replyMessage.getSender().toString() + ":" + replyMessage.getSenderPort());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TcpDispatcher implements Runnable {

        ServerSocket server;

        TcpDispatcher(ServerSocket serverSocket) {
            this.server = serverSocket;
        }

        public void run() {

            Socket client = null;

            try {
                while (true) {
                    client = server.accept();
                    Thread t = new Thread(new ClientThread(client));
                    t.start();
                }

            } catch (SocketException e) {
                System.out.println("DEBUG: SocketException occurred.");
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        
        cli = new Cli(client);
//        swingUI = new SwingUI();
        
        try {
            client.setUp();
            //client.startSwingUI(args);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    private void setUp() throws IOException {
        System.out.println("Welcome to the SWAzam client.");

        // ToDo: somehow bootstrap

        ip = InetAddress.getByName("93.82.83.41");
        port = 37000;

        try {
            initSocket = new Socket(ip, port);
            out = new ObjectOutputStream(initSocket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Cannot find the peer  " + ip + ":" + port + ".");
            //serverSocket.close();
            System.exit(1); //TODO: remove
        } catch (IOException e) {
            System.err.println("Could not connect to peer " + ip + ".");
            //System.exit(1);
        }

    }

//    private void startSwingUI(String[] args) {
//        swingUI = new SwingUI();
//        //TODO: better
//        //swingUI.main(args);
//    }


    public void submitRequest(Fingerprint fingerprint) {
        
        System.out.println("Submitting FingerPrint to network...");
        
        try {
            
            Date d = new Date();
            SearchMessage searchMessage = new SearchMessage(ip.toString(), port, fingerprint, ip.toString() + port.toString() + d.toString());

            out.writeObject(searchMessage);
            out.flush();
            out.close();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void shutdown() {
    	swingUI.close();
    	cli.close();
		System.exit(0);
	}
}
