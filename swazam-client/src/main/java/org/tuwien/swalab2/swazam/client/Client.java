package org.tuwien.swalab2.swazam.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tuwien.swalab2.swazam.client.clientUI.Cli;
import org.tuwien.swalab2.swazam.client.clientUI.SwingUI;
import org.tuwien.swalab2.swazam.client.communication.TcpDispatcher;
import org.tuwien.swalab2.swazam.peer.p2p.SearchMessage;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

import org.tuwien.swalab2.swazam.client.communication.ClientRestClient;

public class Client {

    private static SwingUI swingUI;
    private static Cli cli;
    private InetAddress localIp;
    private Socket socket = null;
    private ObjectOutputStream out = null;
    private TcpDispatcher tcpDispatcher = null;
    private List<KnownPeer> knownPeers = new ArrayList<KnownPeer>();
    private int localPort = 38000;
    private InetAddress currentPeerIp;
    private int currentPeerPort;
    private ClientRestClient restClient = new ClientRestClient();

    public static void main(String[] args) {
        Client client = new Client();

        System.out.println("Welcome to the SWAzam client.");
        
        client.setUp();
        //if (!client.setUp()) {
        //    client.shutdown();
        //}
        //client.startSwingUI(args);
        cli = new Cli(client);
        //swingUI = new SwingUI(client);
    }
    
    private void setUp() {
                
        File knowPeersFile = new File("knownPeers.swazam");
        
        // a: try known peers from local list
        if (knowPeersFile.exists()) {
            
            System.out.println("Loading known peers from file " + knowPeersFile.getName() + ".");

            try {
                FileInputStream knownPeersFileIn = new FileInputStream(knowPeersFile);
                ObjectInputStream knownPeersStreamIn = new ObjectInputStream(knownPeersFileIn);

                knownPeers = (List<KnownPeer>) knownPeersStreamIn.readObject();
                
                socket = connectToPeer(knownPeers);
                
                if (socket == null) {
                    socket = connectToPeer(getPeersFromServer());
                    
                    if (socket == null) {
                        System.out.println("Could not connect to SWAzam network");
                    }               
                }               

            } catch (FileNotFoundException ex) {
//                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            	ex.printStackTrace();
            } catch (IOException ex) {
//                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            	ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            	ex.printStackTrace();
            }
        }
        
        // b: connect to server and get list of known peers
        else {
            System.out.println("No locally known peers.");
            
            socket = connectToPeer(getPeersFromServer());
        } 
        
        try {
            FileOutputStream knownPeersFileOut = new FileOutputStream(knowPeersFile);
            ObjectOutputStream knownPeersStreamOut = new ObjectOutputStream(knownPeersFileOut);
            knownPeersStreamOut.writeObject(knownPeers); 
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Socket connectToPeer(List<KnownPeer> knownPeers) {

        Socket initSocket;
        
        for (KnownPeer currentPeer : knownPeers) {
            
            currentPeerIp = currentPeer.getIp();
            currentPeerPort = currentPeer.getPort();

            //create connection to peer  
            try {
                initSocket = new Socket(currentPeerIp, currentPeerPort);
                localIp = initSocket.getLocalAddress();
                //out = new ObjectOutputStream(initSocket.getOutputStream());

                System.out.println("Connected to peer " + currentPeerIp + ":" + currentPeerPort + ".");

                // add new knownPeers to list
                if (!knownPeers.contains(currentPeer)) {
                    knownPeers.add(currentPeer);
                    //break;
                }

                return initSocket;

            } catch (IOException ex) {
                System.out.println("Could not connect to peer " + currentPeerIp + ":" + currentPeerPort + ".");
                //knownPeers.remove(currentPeer);
            }
        }
        
        return null;

    }
    
    public List<KnownPeer> getPeersFromServer() {
    
        System.out.println("Trying to get list of peers from server...");

        List<KnownPeer> knownPeersFromServer = new ArrayList<KnownPeer>();
        
            String list = (String) restClient.getPeerList();

            String[] peers = list.split("\\-");
            

            for (int i = 0; i < peers.length; i++) {

                try {
                    String[] result = peers[i].split("\\:");
                    if (result.length == 3) {
                    	InetAddress adr = InetAddress.getByName(result[0]);
                    	int port = Integer.parseInt(result[1]);
                    	String uid = result[2];
                    	System.out.println(result[0].toString() + ":" + result[1].toString() + ":" +result[2]);
                        System.out.println("DEBUG: (getPeersFromServer)" + result[0].toString() + ":" + result[1].toString());
                        KnownPeer thisPeer;
                        thisPeer = new KnownPeer(adr, port, uid);
                        knownPeersFromServer.add(thisPeer);
                    }

                } catch (UnknownHostException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            return knownPeersFromServer;
    }

    public void submitRequest(Fingerprint fingerprint) {

        System.out.println("Submitting FingerPrint to network...");

        //Create the port we are listening on
        try {
            System.out.println("DEBUG: building TcpDispatcher...");
            tcpDispatcher = new TcpDispatcher(new ServerSocket(localPort + 1));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //create the message
        Date d = new Date();
        SearchMessage searchMessage = null;
        try {
            System.out.println("DEBUG: building searchMessage...");
            searchMessage = new SearchMessage(localIp.getHostAddress(), localPort, fingerprint, localIp.getHostAddress() + localPort + d.toString());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //send message to peer
        try {

            if (socket == null || socket.isClosed()) {
                System.out.println("socket is null or closed");
                socket = new Socket(currentPeerIp, currentPeerPort);
            }      
            
            System.out.println("just before sending");
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("searchMessage: " + searchMessage);
            out.writeObject(searchMessage);
            out.flush();

        } catch (UnknownHostException e) {
            System.err.println("Cannot find the peer  " + localIp + ":" + localPort + ".");
            //serverSocket.close();
            System.exit(1); //TODO: remove
        } catch (IOException e) {
            System.err.println("Could not send the search request to peer " + localIp + ".");
            e.printStackTrace();
            //System.exit(1);
        }

        //close the connection at this point we dont care about errors any more
        try {
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //give the listening thread 10 seconds time to wait for answers then kill it
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tcpDispatcher.kill();
    }

    public void shutdown() {

        try {
            out.close();
            socket.close();
            //swingUI.close();
            cli.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
}
