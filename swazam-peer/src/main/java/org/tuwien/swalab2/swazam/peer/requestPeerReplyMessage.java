package org.tuwien.swalab2.swazam.peer;

import java.net.UnknownHostException;

public class requestPeerReplyMessage extends Message{
	
	private HostCache c;

	public requestPeerReplyMessage(String ip, int port, String id) throws UnknownHostException {
		super(ip, port, id);
	}
	
	public void setHostCache(HostCache c) {
		this.c = c;
	}
	
	public HostCache getHostCache() {
		return this.c;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3720769063849484227L;

}
