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
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Hello world!
 * 
 */
public class Peer implements MessageReceiver {

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

		Integer arg0;

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
                
                ConnectionHandler c = new ConnectionHandler(library);
                
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
                    Thread t = new Thread(new ConnectionHandler(library));
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

}
