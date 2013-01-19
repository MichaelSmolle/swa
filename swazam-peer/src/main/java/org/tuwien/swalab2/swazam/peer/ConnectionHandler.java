package org.tuwien.swalab2.swazam.peer;

public class ConnectionHandler {
	public void remove(NodeConnection n) {
		System.out.println("stub");
	}
	
	public void disconnectAndRemove(NodeConnection n) {
		n.disconnect();
	}
}
