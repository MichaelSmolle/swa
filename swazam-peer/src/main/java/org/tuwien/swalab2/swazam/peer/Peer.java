package org.tuwien.swalab2.swazam.peer;

import org.apache.log4j.Logger;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;
import org.tuwien.swalab2.swazam.peer.peerUI.Cli;

import com.kenmccrary.jtella.FileServerSession;
import com.kenmccrary.jtella.GNUTellaConnection;
import com.kenmccrary.jtella.MessageReceiver;
import com.kenmccrary.jtella.PushMessage;
import com.kenmccrary.jtella.SearchMessage;
import com.kenmccrary.jtella.SearchMonitorSession;
import com.kenmccrary.jtella.SearchReplyMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Hello world!
 *
 */
public class Peer implements MessageReceiver {

    private static Integer arg0;
    private static Integer id = null;
//	private GNUTellaConnection connection;
//	private FileServerSession fbla;
//	private SearchMonitorSession sbla;
    private Cli cli;
    private static Library library;
//	private Logger log;

    public Peer(Integer arg0) {
        id = arg0;
    }

    public void receiveSearch(SearchMessage m) {
        System.out.println("stub");
    }

    public void receiveSearchReply(SearchReplyMessage m) {
        System.out.println("stub");
    }

    public void receivePush(PushMessage m) {
        System.out.println("stub");
    }

    public static void main(String[] args) {

        try {
            arg0 = Integer.valueOf(args[0]);
        } catch (Exception e) {

            System.out.println("peer [id]");
            return;
        }
        Peer peer = new Peer(arg0);
        peer.setUp(arg0);

    }

    private void setUp(Integer arg0) {
        library = new Library(id);
        library.load();

        cli = new Cli(library);
//		cli.run();

        Properties prop = new Properties();

        try {
            //load a properties file
            prop.load(new FileInputStream("peer.properties"));

            //get the property value and print it out
            System.out.println(prop.getProperty("database"));
            System.out.println(prop.getProperty("dbuser"));
            System.out.println(prop.getProperty("dbpassword"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ConnectionHandler c = new ConnectionHandler(
                    InetAddress.getLocalHost().getHostAddress(),
                    arg0,
                    Integer.getInteger(prop.getProperty("maxConnections")),
                    InetAddress.getByName(prop.getProperty("serverIp")),
                    Integer.getInteger(prop.getProperty("serverPort")),
                    library);
        } catch (UnknownHostException ex) {
            java.util.logging.Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
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
