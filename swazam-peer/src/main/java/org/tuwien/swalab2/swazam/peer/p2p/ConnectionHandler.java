package org.tuwien.swalab2.swazam.peer.p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.simpleframework.util.buffer.ArrayAllocator;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;

public class ConnectionHandler extends Thread {
	
	private volatile String myAddrString;
	private volatile int myPort;
	//private InetAddress myAddr;
	private volatile int maxConnections;
	private HostCache knownNodes;

	private ConcurrentLinkedQueue<NodeConnection> currentConnections;

	private PeerRestClient restClient = new PeerRestClient();
	private MessageHandler mh;
	private volatile boolean running;
	private InetAddress serverIp;
	private volatile int serverPort;
	private IncommingClientConnectionHandler icch;
	private IncommingPeerConnectionHandler   ipch;
	private String uid;
    //private Library library;
    
    public ConnectionHandler(
    		String myUid,
            String myIp,
            int myPort,
            int maxConnections,
            InetAddress serverIp,
            int serverPort,
            Library lib) {
    	this.uid = myUid;
        this.myAddrString = myIp;
        this.myPort = myPort;
        this.maxConnections = maxConnections;
        this.knownNodes = new HostCache();
        this.knownNodes.load();
        try {
//			this.knownNodes.add(new HostCacheEntry(InetAddress.getLocalHost(), 37000, "testpeer1"));
			//always add this node to the HostCache such that exchange will work
			this.knownNodes.add(new HostCacheEntry(InetAddress.getByName(this.myAddrString), this.myPort, this.uid));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.currentConnections = new ConcurrentLinkedQueue<NodeConnection>();
        this.mh = new MessageHandler(lib, this);
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.icch = new IncommingClientConnectionHandler(this.myPort + 1, this.mh);
        this.ipch = new IncommingPeerConnectionHandler(this.myPort, this);
        this.running = true;
        this.start();
    }
    
    public void forwardMessage(Message m) {
    	m.decreaseTTL();
    	if(m.getTTL()>0) {
    		Iterator<NodeConnection> itr = this.currentConnections.iterator();
    		while(itr.hasNext()) {
    			itr.next().sendMessage(m);
    		}
    	}
    }
	
	//Connects to server, sends a requestPeerMessage
	//waits for a Message from server and calls the MessageHandler
	private void bootstrap() {
		try {

//		 getting list from server
	     System.out.println("Bootstrapping from server!");		
		 String list = (String) restClient.getPeerList();
		 
		 String[] peers = list.split("\\-");
	
		 for (int i = 0; i < peers.length; i++) {
			 String[] result = peers[i].split("\\:");
			 if (result.length == 3){
			 InetAddress adr = InetAddress.getByName(result[0]);
			 int port = Integer.parseInt(result[1]);
			 String uid = result[2];
			 System.out.println(result[0].toString() + ":" + result[1].toString() + ":" +result[2]);
			 HostCacheEntry entry = new HostCacheEntry(adr, port, uid);
			 knownNodes.add(entry);
			 }
		 }
		 	
		 
//		 register me server
		 System.out.println("Registering to server! \n");
		 String register = new String (myAddrString + ":" + myPort + ":" + this.uid);
//		 String register = new String ("127.0.0.1" + ":" + myPort);
		 restClient.registerPeer(register);
		 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printStatus() {
		System.out.println("Number of known nodes: " + this.knownNodes.getHostCache().size());
		System.out.println("MAX_CONNECTIONS: " + this.maxConnections);
		System.out.println("IP: " + this.myAddrString);
		System.out.println("Peer listening port: " + this.myPort);
		System.out.println("Client listening port: " + (this.myPort+1));
		System.out.println("----------");
		System.out.println("Active Connections:");
		NodeConnection cur = null;
		
		Iterator<NodeConnection> itr = this.currentConnections.iterator();
		while(itr.hasNext()) {
			cur = itr.next();
			if(cur.isOnline()) {
				System.out.println(cur.getRemoteIp().getHostAddress() + ":" + cur.getRemotePort()+ " id " + cur.getRemoteUid());
			}
		}
	}
	
	//Merge nodes in current Cache and the ones received from a requestPeerMessage
	//already thread save
	public void addNodes(LinkedList<HostCacheEntry> newHosts) {
		this.knownNodes.merge(newHosts);
	}
	
	//Helper method to find a connection in the currentConnections list
	private NodeConnection findConnection(String remoteUid) {
		NodeConnection cur;
		Iterator<NodeConnection> itr = this.currentConnections.iterator();
		
		while(itr.hasNext()) {
			cur = itr.next();
			if(cur.getRemoteUid().compareTo(remoteUid) == 0) {
				return cur;
			}
		}

		return null;
	}
	
	//call sendMessage in corresponding NodeConnection
	public void sendMessageTo(String remoteUid, Message m) {
		NodeConnection cur = this.findConnection(remoteUid);
		if(cur != null) {
			cur.sendMessage(m);
		}
	}
	
	//Called by IncommingPeerConnectionHandler
	//If we already have engough connections just close the socket
	//else create a new NodeConnection
	public void handleIncommingPeerConnection(Socket s, String remoteUid, ObjectInputStream ois) {
		// check for too many connections
		if (this.currentConnections.size() < this.maxConnections) { 
			// check if we are already connected to this node
			if(this.findConnection(remoteUid) == null) {
				OutputStream os = null;
				ObjectOutputStream oos = null;
				try {
					os = s.getOutputStream();
					oos = new ObjectOutputStream(os);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.currentConnections.add(new NodeConnection(s, s.getInetAddress(), s.getPort(), remoteUid, ois, oos, this.mh));
			}
		} else {
			try {
				s.close();
			} catch (IOException e) {}
		}
	}
	
	//Called by MessageHandler 
	//Send the HostCache to the corresponding NodeConnection
	//As requests for nodes will only travel for one hop there is no need to create a new connection
	public void replyToRequestNodes(String remoteUid) {
		requestPeerReplyMessage m = null;
		try {
			m = new requestPeerReplyMessage(this.myAddrString, this.myPort, null);
			m.setHostCache(this.knownNodes.toLinkedList());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		//System.out.println("Sending " + m.getHostCache().size() + "peers");
		this.sendMessageTo(remoteUid, m);
	}
	
	//send a requestPeerMessage to all NodeConnections
	//if we have to active connections this means that the HostCache is worthless therefore bootstrap
	private void requestNodes() {
		if(this.currentConnections.size()!= 0) {
			requestPeerMessage m = null;
			
			Iterator<NodeConnection> itr = this.currentConnections.iterator();
			while(itr.hasNext()) {
				//System.out.println("sending request messages");
				try {
					m = new requestPeerMessage(this.myAddrString, this.myPort, null);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				itr.next().sendMessage(m);
			}
		} else {
			this.bootstrap();
		}
	}
	
	private void clearDeadConnections() {
		Iterator<NodeConnection> itr = this.currentConnections.iterator();
		NodeConnection cur;
		
		while(itr.hasNext()) {
			cur = itr.next();
			if(!cur.isOnline()) {
				this.currentConnections.remove(cur);
			}
		}
		
	}
	
	public void shutdown() {
		//persist the host cache
		this.knownNodes.persist();
		//ignore the lock just kill everything
		this.icch.kill();
		this.ipch.kill();
		this.running = false;
		try {
			join();
		} catch (InterruptedException e) {}
		
		Iterator<NodeConnection> itr = this.currentConnections.iterator();
		while(itr.hasNext()) {
			itr.next().disconnect();
		}
	}
	
	private void sendUid(Socket n, ObjectOutputStream oos) throws Exception {
		//System.out.println("4");
		oos.writeObject(this.uid);
		oos.flush();
		//System.out.println("done with writing");
		//or.close();
		//os.close();
	}
	
	//Start a conenction to a node
	private void startConnection(InetAddress remoteIp, int remotePort, String remoteUid) {
		Socket s = null;
		try {
			//System.out.println("1");
			s = new Socket(remoteIp, remotePort);
			//System.out.println("2");
			OutputStream os = s.getOutputStream();
			//System.out.println("3");
			ObjectOutputStream oos = new ObjectOutputStream(os);
			//System.out.println("4");
			//System.out.println("2");
			this.sendUid(s, oos);
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			this.currentConnections.add(new NodeConnection(s, remoteIp, remotePort, remoteUid, ois, oos, this.mh));
		} catch (Exception e) {
			//e.printStackTrace();
			if(s != null) {
				try {
					s.close();
				} catch (IOException e1) {}
			}
			return;
		}
		
	}
	
	public void run() { 
		while(running) {
			//clear dead connections
			this.clearDeadConnections();
			//check if we need more connections
			if(this.currentConnections.size() < this.maxConnections) {
				//System.out.println("Trying more connections");
				Enumeration<HostCacheEntry> e = this.knownNodes.getHostCache().elements();
				HostCacheEntry cur = null;
				while(e.hasMoreElements() && this.currentConnections.size() < this.maxConnections) {
					cur = e.nextElement();
					//System.out.println("Trying connection to " + cur.getAdr().getHostAddress() + ":" + cur.getPort() + " id " +cur.getUid());
					if(this.findConnection(cur.getUid()) == null && this.uid.compareTo(cur.getUid())!=0) {
						//System.out.println("Starting connection to " + cur.getAdr().getHostAddress() + ":" + cur.getPort());
						this.startConnection(cur.getAdr(), cur.getPort(), cur.getUid());
					}
				}
			}
			//if we still have not enough connections get more HostCache entries
			if(this.currentConnections.size() < this.maxConnections) {
				this.requestNodes();
			}                          
			//sleep for some time
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {}
		}
	}
}
