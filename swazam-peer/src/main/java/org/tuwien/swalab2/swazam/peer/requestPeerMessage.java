package org.tuwien.swalab2.swazam.peer;

import java.net.UnknownHostException;

public class requestPeerMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3106279008620346910L;
	
	public requestPeerMessage(String ip, int port, String id) throws UnknownHostException {
		super(ip, port, id);
	}

}
