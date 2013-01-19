package org.tuwien.swalab2.swazam.peer;

import java.net.Socket;

public class IncommingPeerConnectionHandler extends IncommingConnectionHandler {
	
	private ConnectionHandler ch;
	
	public IncommingPeerConnectionHandler(int port, ConnectionHandler ch) {
		super(port);
		this.ch = ch;
		this.start();
	}

	@Override
	public void handleSocket(Socket n) {
		this.ch.handleIncommingPeerConnection(n);	
	}

}
