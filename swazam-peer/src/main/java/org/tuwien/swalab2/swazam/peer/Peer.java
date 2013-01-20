package org.tuwien.swalab2.swazam.peer;

import org.apache.log4j.Logger;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;
import org.tuwien.swalab2.swazam.peer.peerUI.Cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Hello world!
 *
 */
public class Peer {

    private static Integer arg0;
    private static Integer id = null;
    private Cli cli;
    private static Library library;
    private static String myUid;
//	private Logger log;

    public Peer(Integer arg0) {
        id = arg0;
    }

    public static void main(String[] args) {

        try {
            arg0 = Integer.valueOf(args[0]);
            myUid = args[1];
        } catch (Exception e) {

            System.out.println("peer [id][uid]");
            return;
        }
        Peer peer = new Peer(arg0);
        peer.setUp(arg0);

    }

    private void setUp(Integer arg0) {
        library = new Library(id);
        library.load();
        ConnectionHandler c = null;
        String NAT_IP;
        
        Properties prop = new Properties();

        try {
            //load a properties file
            prop.load(new FileInputStream("peer.properties"));
            
            //int maxConnections = prop.getProperty("maxConnections");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        InetAddress myIp = null;
        NAT_IP = prop.getProperty("bindToIp");
        
        //If no ip has been specified then try to find the real ip
        if (NAT_IP == null) {
        	Enumeration<NetworkInterface> eI = null;
        	try {
        		eI = NetworkInterface.getNetworkInterfaces();
        	} catch (SocketException e) {
        		e.printStackTrace();
        	}
        	NetworkInterface ni;
        	try {
        		while(eI.hasMoreElements()) {
        			ni = eI.nextElement();
        			if(!ni.isLoopback() && ni.isUp() && !ni.isVirtual()) {
        				myIp = ni.getInetAddresses().nextElement(); //try the first ip
        			}
        		}
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        } else {
        	try {
				myIp = InetAddress.getByName(NAT_IP);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
        }
        try {
            c = new ConnectionHandler(
            		myUid,
            		myIp.getHostAddress(),		//InetAddress.getLocalHost().getHostAddress(), use real ip
                    arg0.intValue(),
                    Integer.parseInt(prop.getProperty("maxConnections")),
                    InetAddress.getByName(prop.getProperty("serverIp")),
                    Integer.parseInt(prop.getProperty("serverPort")),
                    library);
        } catch (UnknownHostException ex) {
            java.util.logging.Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cli = new Cli(library, c);
        //Thread t = new Thread(new ConnectionHandler(library));
        //t.start();

        //		log = Logger.getLogger(getClass());
        //		log.info("Welcome to the SWAzam Peer.");	
        //		System.setProperty("JTella.Debug", "JTella.Debug");
        //		try {
        //			ConnectionData cn = new ConnectionData();
        //			cn.setIncomingPort(arg0);
        //			cn.setOutgoingConnectionCount(100);
        //		    cn.setIncommingConnectionCount(100);
        //		    cn.setConnectionGreeting("test"); 
        //		    if(arg0%2 == 0) {
        //		    	cn.setUltrapeer(true);
        //		    }
        //			this.connection = new GNUTellaConnection(cn);
        //		} catch (IOException e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}

        //		BasicConfigurator.configure();
        //		this.connection.getHostCache().addHost(new Host("192.168.1.21",37000, 10, 10));
        //		if(arg0 != 37010) {
        //			this.connection.getHostCache().addHost(new Host("127.0.0.1",37010, 10, 10));
        //		}
        ////		this.connection.getHostCache().addHost(new Host("192.168.1.19",37002, 10, 10));
        //		
        //		if(this.connection.getHostCache().getKnownHosts().isEmpty()) {
        //			System.out.println("Empty Cache");
        //		}
        //		//fbla = connection.createFileServerSession(this);
        //		//sbla = connection.getSearchMonitorSession(this);
        //		this.connection.start();
        //		System.out.println("starting...");
        //		if(this.connection.getHostCache().getKnownHosts().isEmpty()) {
        //			System.out.println("Empty Cache");
        //		}



        //this.hostCache.addHost(new Host("",37000));
        // ToDo: somehow bootstrap

    }
}
