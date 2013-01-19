package org.tuwien.swalab2.swazam.peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class NodeConnection {
	
	private Socket 				s;
	private MessageHandler 		mh;
	private ConnectionHandler   ch;
	private MessageReceiver 	mr;
	private InetAddress			remoteIp;
	private int 				remotePort;
	private volatile boolean	online;
	
	public NodeConnection(Socket socket, InetAddress remoteIp, int remotePort, MessageHandler mh, ConnectionHandler ch) {
		this.s = socket;
		this.remoteIp = remoteIp;
		this.remotePort = remotePort;
		this.mh = mh;
		this.ch = ch;
		this.mr = new MessageReceiver(this.mh, this.s, this);
		this.ch.addToConnectedNodes(this.remoteIp, this.remotePort);
		this.online = true;
	}
	
	public boolean isOnline() {
		return this.online;
	}
	
	public InetAddress getRemoteIp() {
		return this.remoteIp;
	}
	
	public int getRemotePort() {
		return this.remotePort;
	}
	
	public synchronized void sendMessage(Message m) {
		  
		try {
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(m);
			oos.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			this.disconnect();
		} 
	}
	
	public synchronized void disconnect() {
		this.mr.kill();
		try {
			this.s.close();
		} catch (IOException e) {}
		this.ch.removeFromConnectedNodes(this.remoteIp, this.remotePort);
		this.online = false;
	}
	
	private class MessageReceiver extends Thread {
		private MessageHandler mh;
		private volatile boolean running;
		private InputStream is;
		private ObjectInputStream ois;
		private NodeConnection n;
		
		public MessageReceiver(MessageHandler mh, Socket s, NodeConnection n) {
			this.mh = mh;
			this.running = true;
			this.n = n;
			try {
				this.is = s.getInputStream();
				this.ois = new ObjectInputStream(is);
				this.start();
			} catch (Exception e) {
				this.n.disconnect();
				e.printStackTrace();
			}  
		}
		
		public void kill() {
			this.running = false;
			try {
				this.ois.close();
			} catch (IOException e) {}
			try {
				this.is.close();
			} catch (IOException e) {}
			try {
				join();
			} catch (InterruptedException e) {}
		}
		
		public void run() {
			while(this.running) {
					try {
						mh.handleMessage((Message)ois.readObject());
					} catch (Exception e) {
						e.printStackTrace();
						this.n.disconnect();
					} 
			}
		}
	}
}
