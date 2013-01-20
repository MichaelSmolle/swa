package org.tuwien.swalab2.swazam.peer;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class HostCacheEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6951132774557785036L;
	private InetAddress adr;
	private int         port;
	private Date		timestamp;
	String  uid;

	
	public HostCacheEntry(InetAddress adr, int port, String uid) {
		this.adr = adr;
		this.port = port;
		this.uid = uid;
	}
	
	public InetAddress getAdr() {
		return this.adr;
	}
	
	public int getPort() {
		return this.port;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getUid() {
		return this.uid;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
