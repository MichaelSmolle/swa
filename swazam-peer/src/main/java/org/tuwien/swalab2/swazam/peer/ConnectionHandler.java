package org.tuwien.swalab2.swazam.peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class ConnectionHandler extends Thread {
	
	private String		myAddrString;
	private int 		myPort;
	private InetAddress myAddr;
	private int 		maxConnections;
	private HostCache   connectedNodes;
	private HostCache   knownNodes;
	private Vector<NodeConnection> currentConnections;
	private MessageHandler mh;
	private volatile boolean running;
	
	private void bootstrap() {
		//todo;
	}
	
	public void addNodes(HostCache newHosts) {
		//todo merge caches
	}
	
	public void replyToRequestNodes(InetAddress requestorIp, int requestorPort) {
		//todo not thread save
		requestPeerReplyMessage m = null;
		try {
			m = new requestPeerReplyMessage(this.myAddrString, this.myPort, null);
			m.setHostCache(this.knownNodes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < this.currentConnections.size(); i++) {
			if(this.currentConnections.get(i).getRemoteIp().equals(requestorIp) && this.currentConnections.get(i).getRemotePort() == requestorPort) {
				this.currentConnections.get(i).sendMessage(m);
				return;
			}
		}
		
	}
	
	private void requestNodes() {
		if(this.currentConnections.size()!= 0) {
			Message m = null;
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
	
	protected synchronized void removeFromConnectedNodes(InetAddress remoteIp, int remotePort) {
		this.connectedNodes.remove(new HostCacheKeyEntry(remoteIp, remotePort));
	}
	
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
			//clear dead connections
			this.clearDeadConnections();
			HostCacheEntry tmp = null;
			//check if we need more connections
			if(this.currentConnections.size() < this.maxConnections) {
					//check if we already have a connection to this node
					//todo
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
