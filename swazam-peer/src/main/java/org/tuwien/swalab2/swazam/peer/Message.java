package org.tuwien.swalab2.swazam.peer;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = 1988151102745064993L;
	
	private InetAddress senderAdr;
	private int         senderPort;
	private int         ttl;
	private String      id;
	
	public Message(String ip, int port, String id) throws UnknownHostException {
		this.senderAdr = InetAddress.getByName(ip);
		this.senderPort = port;
		this.ttl = 5;
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getTTL() {
		return this.ttl;
	}
	
	public void decreaseTTL() {
		this.ttl -= 1;
	}
	
	public InetAddress getSender() {
		return this.senderAdr;
	}
	
	public int getSenderPort() {
		return this.senderPort;
	}

        
        
}
