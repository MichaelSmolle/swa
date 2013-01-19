package org.tuwien.swalab2.swazam.peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;

//TODO:
//Thread Sicherheit kontrollieren
//Konstruktor
//Messages weiterleiten, gilt nur für SearchMessages
//restliche Todos im Source Code
public class ConnectionHandler extends Thread {
	
	private String myAddrString;
	private int myPort;
	//private InetAddress myAddr;
	private int maxConnections;
	private HostCache connectedNodes;
	private HostCache knownNodes;
	private Vector<NodeConnection> currentConnections;
	private MessageHandler mh;
	private volatile boolean running;
	private InetAddress serverIp;
	private int serverPort;
        private Library library;

        public ConnectionHandler(Library library) {
            mh = new MessageHandler(library);
            this.library = library;
            this.start();
        }
	
	//Connects to server, sends a requestPeerMessage
	//waits for a Message from server and calls the MessageHandler
	private void bootstrap() {
		try {
			Socket s = new Socket(serverIp, serverPort);
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			oos.writeObject(new requestPeerMessage(myAddrString, myPort, null));
			
			this.mh.handleMessage((Message)ois.readObject());
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Merge nodes in current Cache and the ones received from a requestPeerMessage
	public void addNodes(HostCache newHosts) {
		this.knownNodes.merge(newHosts);
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
		NodeConnection cur = this.findConnection(requestorIp, requestorPort);
		if(cur != null) {
			cur.sendMessage(m);
		}
	}
	
	//Called by IncommingPeerConnectionHandler
	//If we already have engough connections just close the socket
	//else create a new NodeConnection
	//TODO: Peer shoudl be added to HostChache if not already there
	public void handleIncommingPeerConnection(Socket s) {
		if (this.currentConnections.size() < this.maxConnections) { // check for too many connections
			// check if we are already connected to this node
			if(this.findConnection(s.getInetAddress(), s.getPort()) == null) {
				this.currentConnections.add(new NodeConnection(s, s.getInetAddress(), s.getPort(), this.mh, this));
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
	public void replyToRequestNodes(InetAddress requestorIp, int requestorPort) {
		//todo not thread save
		requestPeerReplyMessage m = null;
		try {
			m = new requestPeerReplyMessage(this.myAddrString, this.myPort, null);
			m.setHostCache(this.knownNodes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		this.sendMessageTo(requestorIp, requestorPort, m);
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
	protected synchronized void removeFromConnectedNodes(InetAddress remoteIp, int remotePort) {
		this.connectedNodes.remove(new HostCacheKeyEntry(remoteIp, remotePort));
	}
	
	//Add a node to the connectedNode list, called by NodeConnection in Constructor if everything went alright
	protected synchronized void addToConnectedNodes(InetAddress remoteIp, int remotePort) {
		this.connectedNodes.add(knownNodes.search(new HostCacheKeyEntry(remoteIp, remotePort)));
	}
	
	private void clearDeadConnections() {
		for (int i = currentConnections.size()-1; i >= 0; i--) {
			if (!currentConnections.get(i).isOnline()) {
				this.currentConnections.remove(i);
			}
		}
	}
	
	private void shutdown() {
		this.running = false;
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
            
            IncommingClientConnectionHandler icch = new IncommingClientConnectionHandler(myPort, mh);
                        
		while(running) {
			//clear dead connections
			this.clearDeadConnections();
			//check if we need more connections
			if(this.currentConnections.size() < this.maxConnections) {
				Enumeration<HostCacheEntry> e = this.knownNodes.getHostCache().elements();
				HostCacheEntry cur = null;
				while(e.hasMoreElements() && this.currentConnections.size() < this.maxConnections) {
					cur = e.nextElement();
					if(!this.connectedNodes.getHostCache().containsKey(new HostCacheKeyEntry(cur.getAdr(), cur.getPort()))) {
					//TODO: check if we already have a connection to this node
						this.startConnection(cur.getAdr(), cur.getPort());
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
