package org.tuwien.swalab2.swazam.peer.p2p;

import java.net.UnknownHostException;
import java.util.LinkedList;


public class requestPeerReplyMessage extends Message{
	
	private LinkedList<HostCacheEntry> c;

	public requestPeerReplyMessage(String ip, int port, String id) throws UnknownHostException {
		super(ip, port, id);
	}
	
	public void setHostCache(LinkedList<HostCacheEntry> c) {
		this.c = c;
	}
	
	public LinkedList<HostCacheEntry> getHostCache() {
		return this.c;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3720769063849484227L;

}
