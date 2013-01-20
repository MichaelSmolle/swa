package org.tuwien.swalab2.swazam.peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;

public class ConnectionHandler extends Thread {
	
	private String myAddrString;
	private int myPort;
	//private InetAddress myAddr;
	private int maxConnections;
	private HostCache connectedNodes;
	private HostCache knownNodes;
	private LinkedList<NodeConnection> currentConnections;
	private MessageHandler mh;
	private volatile boolean running;
	private InetAddress serverIp;
	private int serverPort;
	private IncommingClientConnectionHandler icch;
	private IncommingPeerConnectionHandler   ipch;
	private ReentrantLock l;
    //private Library library;
    
    public ConnectionHandler(
            String myIp,
            int myPort,
            int maxConnections,
            InetAddress serverIp,
            int serverPort,
            Library lib) {
        this.myAddrString = myIp;
        this.myPort = myPort;
        this.maxConnections = maxConnections;
        this.connectedNodes = new HostCache();
        this.knownNodes = new HostCache();
        this.knownNodes.load();
        this.currentConnections = new LinkedList<NodeConnection>();
        this.mh = new MessageHandler(lib, this);
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.icch = new IncommingClientConnectionHandler(this.myPort + 1, this.mh);
        this.ipch = new IncommingPeerConnectionHandler(this.myPort, this);
        this.l = new ReentrantLock();
        this.running = true;
        this.start();
    }
    
    public void forwardMessage(Message m) {
    	this.l.lock();
    	m.decreaseTTL();
    	if(m.getTTL()>0) {
    		for(int i = 0; i < this.currentConnections.size(); i++) {
    			this.currentConnections.get(i).sendMessage(m);
    		}
    	}
    	this.l.unlock();
    }
	
	//Connects to server, sends a requestPeerMessage
	//waits for a Message from server and calls the MessageHandler
	private void bootstrap() {
		try {
			//Socket s = new Socket(serverIp, serverPort);
			//OutputStream os = s.getOutputStream();
			//ObjectOutputStream oos = new ObjectOutputStream(os);
			//InputStream is = s.getInputStream();
			//ObjectInputStream ois = new ObjectInputStream(is);
			//oos.writeObject(new requestPeerMessage(myAddrString, myPort, null));
			//oos.flush();
			
			//this.mh.handleMessage((Message)ois.readObject());
			
			//oos.close();
			//os.close();
			//ois.close();
			//is.close();
			//s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printStatus() {
		System.out.println("Number of known nodes: " + this.knownNodes.getHostCache().size());
		System.out.println("MAX_CONNECTIONS: " + this.maxConnections);
		System.out.println("IP: " + this.myAddrString);
		System.out.println("Peer listening port: " + this.myPort);
		System.out.println("Client listening port: " + this.myPort+1);
		System.out.println("----------");
		System.out.println("Active Connections:");
		for (int i = 0; i < this.currentConnections.size(); i++) {
			if(this.currentConnections.get(i).isOnline()) {
				System.out.println(this.currentConnections.get(i).getRemoteIp().getHostAddress() + ":" + this.currentConnections.get(i).getRemotePort());
			}
		}
	}
	
	//Merge nodes in current Cache and the ones received from a requestPeerMessage
	public void addNodes(HostCache newHosts) {
		this.l.lock();
		this.knownNodes.merge(newHosts);
		this.l.unlock();
	}
	
	//Helper method to find a connection in the currentConnections list
	private NodeConnection findConnection(InetAddress requestorIp, int requestorPort) {
		NodeConnection cur;
		for(int i = 0; i < this.currentConnections.size(); i++) {
			cur = this.currentConnections.get(i);
			if(cur.getRemoteIp().equals(requestorIp) && cur.getRemotePort() == requestorPort) {
				return cur;
			}
		}
		return null;
	}
	
	//call sendMessage in corresponding NodeConnection
	public void sendMessageTo(InetAddress requestorIp, int requestorPort, Message m) {
		this.l.lock();
		NodeConnection cur = this.findConnection(requestorIp, requestorPort);
		if(cur != null) {
			cur.sendMessage(m);
		}
		this.l.unlock();
	}
	
	//Called by IncommingPeerConnectionHandler
	//If we already have engough connections just close the socket
	//else create a new NodeConnection
	public void handleIncommingPeerConnection(Socket s) {
		this.l.lock();
		if (this.currentConnections.size() < this.maxConnections) { // check for too many connections
			// check if we are already connected to this node
			if(this.findConnection(s.getInetAddress(), s.getPort()) == null) {
				this.currentConnections.add(new NodeConnection(s, s.getInetAddress(), s.getPort(), this.mh, this));
				this.knownNodes.add(new HostCacheEntry(s.getInetAddress(),s.getPort()));
			}
		} else {
			try {
				s.close();
			} catch (IOException e) {}
		}
		this.l.unlock();
	}
	
	//Called by MessageHandler 
	//Send the HostCache to the corresponding NodeConnection
	//As requests for nodes will only travel for one hop there is no need to create a new connection
	public void replyToRequestNodes(InetAddress requestorIp, int requestorPort) {
		this.l.lock();
		requestPeerReplyMessage m = null;
		try {
			m = new requestPeerReplyMessage(this.myAddrString, this.myPort, null);
			m.setHostCache(this.knownNodes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		this.sendMessageTo(requestorIp, requestorPort, m);
		this.l.unlock();
	}
	
	//send a requestPeerMessage to all NodeConnections
	//if we have to active connections this means that the HostCache is worthless therefore bootstrap
	private void requestNodes() {
		if(this.currentConnections.size()!= 0) {
			requestPeerMessage m = null;
			try {
				m = new requestPeerMessage(this.myAddrString, this.myPort, null);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			} //todo
			for(int i=0; i < this.currentConnections.size(); i++) {
				if (this.currentConnections.get(i).isOnline()) {
					this.currentConnections.get(i).sendMessage(m);
				}
			}
		} else {
			this.bootstrap();
		}
	}
	
	//Remove a node from the connectedNode list, called by a NodeConnection
	protected void removeFromConnectedNodes(InetAddress remoteIp, int remotePort) {
		this.l.lock();
		this.connectedNodes.remove(new HostCacheKeyEntry(remoteIp, remotePort));
		this.l.unlock();
	}
	
	//Add a node to the connectedNode list, called by NodeConnection in Constructor if everything went alright
	protected void addToConnectedNodes(InetAddress remoteIp, int remotePort) {
		this.l.lock();
		this.connectedNodes.add(knownNodes.search(new HostCacheKeyEntry(remoteIp, remotePort)));
		this.l.unlock();
	}
	
	private void clearDeadConnections() {
		for (int i = currentConnections.size()-1; i >= 0; i--) {
			if (!currentConnections.get(i).isOnline()) {
				this.currentConnections.remove(i);
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
		for (int i = this.currentConnections.size(); i >=0; i++) {
			this.currentConnections.get(i).disconnect();
		}
	}
	
	//Start a conenction to a node
	private void startConnection(InetAddress remoteIp, int remotePort) {
		Socket s;
		try {
			s = new Socket(remoteIp, remotePort);
		} catch (IOException e) {
			return;
		}
		this.currentConnections.add(new NodeConnection(s, remoteIp, remotePort, this.mh, this));
	}
	
	public void run() { 
		while(running) {
			this.l.lock();
			//clear dead connections
			this.clearDeadConnections();
			//check if we need more connections
			if(this.currentConnections.size() < this.maxConnections) {
				Enumeration<HostCacheEntry> e = this.knownNodes.getHostCache().elements();
				HostCacheEntry cur = null;
				while(e.hasMoreElements() && this.currentConnections.size() < this.maxConnections) {
					cur = e.nextElement();
					if(!this.connectedNodes.getHostCache().containsKey(new HostCacheKeyEntry(cur.getAdr(), cur.getPort()))) {
						this.startConnection(cur.getAdr(), cur.getPort());
					}
				}
			}
			//if we still have not enough connections get more HostCache entries
			if(this.currentConnections.size() < this.maxConnections) {
				this.requestNodes();
			}                 
            this.l.unlock();           
			//sleep for some time
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {}
		}
	}
}
