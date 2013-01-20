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
import org.tuwien.swalab2.swazam.client.communication.TcpDispatcher;
import org.tuwien.swalab2.swazam.peer.SearchMessage;
import org.tuwien.swalab2.swazam.peer.SearchReplyMessage;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Client {

    private static SwingUI swingUI;
    private static Cli cli;
    private Socket socket = null;
    private InetAddress ip;
    private Integer port;
    private Socket initSocket = null;
    private ObjectOutputStream out = null;
    private TcpDispatcher tcpDispatcher = null;

    public static void main(String[] args) {
        Client client = new Client();

        cli = new Cli(client);
        //swingUI = new SwingUI(client);

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
    }

    public void submitRequest(Fingerprint fingerprint) {

        System.out.println("Submitting FingerPrint to network...");

        //Create the port we are listening on
        try {
			tcpDispatcher = new TcpDispatcher(new ServerSocket(port + 1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create the message
		Date d = new Date();
		SearchMessage searchMessage = null;
        try {
			searchMessage = new SearchMessage(ip.getHostAddress(), port, fingerprint, ip.getHostAddress() + port.toString() + d.toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create connection to peer and send message
		try {
			ip = InetAddress.getByName("127.0.0.1");
			ip = InetAddress.getLocalHost();
			port = 37001;
            initSocket = new Socket(ip, port);
            out = new ObjectOutputStream(initSocket.getOutputStream());

            out.writeObject(searchMessage);
            out.flush();
        } catch (UnknownHostException e) {
            System.err.println("Cannot find the peer  " + ip + ":" + port + ".");
            //serverSocket.close();
            System.exit(1); //TODO: remove
        } catch (IOException e) {
            System.err.println("Could not connect to peer " + ip + ".");
            //System.exit(1);
        }
        
        //close the connection at this point we dont care about errors any more
        try {
        	out.close();
        	initSocket.close();
        } catch (Exception e) {}
        
        //give the listening thread 15 seconds time to wait for answers then kill it
        try {
			Thread.sleep(15000);
		} catch (Exception e) {}
		tcpDispatcher.kill();
    }

    public void shutdown() {

        try {
            out.close();
            initSocket.close();
            //swingUI.close();
            cli.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
}
