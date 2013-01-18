package org.tuwien.swalab2.swazam.peer;

import java.io.Serializable;
import java.net.InetAddress;

public class Host implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6951132774557785036L;
	private InetAddress adr;
	private int         port;
	private boolean 	online;
	
	public Host(InetAddress adr, int port) {
		this.adr = adr;
		this.port = port;
		this.online = false;
	}
	
	public InetAddress getAdr() {
		return this.adr;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public boolean isOnline() {
		return this.online;
	}
	
	public void setOnline() {
		this.online = true;
	}
	
	public void setOffline() {
		this.online = false;
	}

}
