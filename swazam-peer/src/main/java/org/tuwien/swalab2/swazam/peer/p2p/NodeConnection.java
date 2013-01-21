package org.tuwien.swalab2.swazam.peer.p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

//Represents a connection to an existing node
//TODO Check Thread Safety and kill method
public class NodeConnection {
	
	private Socket 				s;
	private MessageHandler 		mh;
	private MessageReceiver 	mr;
	private InetAddress			remoteIp;
	private int 				remotePort;
	private volatile boolean	online;
	private ObjectOutputStream  oos;
	private String 				remoteUid;
	
	public NodeConnection(Socket socket, InetAddress remoteIp, int remotePort, String remoteUid, 
						  ObjectInputStream ois, ObjectOutputStream oos, MessageHandler mh) {
		this.s = socket;
		this.remoteIp = remoteIp;
		this.remotePort = remotePort;
		this.remoteUid = remoteUid;
		this.mh = mh;
		this.oos = oos;
		this.mr = new MessageReceiver(this.mh, this.s, this, ois);
		this.online = true;
	}
	
	public boolean isOnline() {
		return this.online;
	}
	
	public InetAddress getRemoteIp() {
		return this.remoteIp;
	}
	
	public String getRemoteUid() {
		return this.remoteUid;
	}
	
	public int getRemotePort() {
		return this.remotePort;
	}
	
	//send a message to the connected node
	public synchronized void sendMessage(Message m) {
		//only do this if we are online
		//if we are offline the ConnectionHandler will take some time to remove this connection
		if(this.online) {  
			try {
				this.oos.writeObject(m);
				this.oos.flush();
			} catch (IOException e) {
				this.disconnect();
			} 
		}
	}
	
	//Inform the ConnectionHandler that this connection is dead
	//set the online flag to false
	//ConnectionHandler will clear the node from the list sometime later
	public void disconnect() {
		this.online = false;
		try {
			this.s.close();
			this.oos.close();
			this.s.close();
		} catch (Exception e) {}
		if(this.mr.isRunning()) {
			System.out.println("killing message receiver");
			this.mr.kill();
			try {
				this.mr.join();
			} catch (InterruptedException e) {}
		}
		System.out.println("Done with disconnect");
	}
	
	//Receive a message and give it to the MessageHandler
	private class MessageReceiver extends Thread {
		private MessageHandler mh;
		private volatile boolean running;
		private ObjectInputStream ois;
		private NodeConnection n;
		private Socket s;
		
		public MessageReceiver(MessageHandler mh, Socket s, NodeConnection n, ObjectInputStream ois) {
			this.mh = mh;
			this.running = true;
			this.n = n;
			this.s = s;
			this.ois = ois;
			this.start(); 
		}
		
		public boolean isRunning() {
			return this.running;
		}
		
		public void kill() {
			this.running = false;
			try {
				this.ois.close();
			} catch (IOException e) {}
			try {
				this.s.close();
			} catch (IOException e1) {}
		}
		
		public void run() {
			while(this.running) {
					try {
						mh.handleMessage((Message)ois.readUnshared(), this.n.getRemoteUid());
					} catch (Exception e) {
						this.n.disconnect();
					} 
			}
		}
	}
}
