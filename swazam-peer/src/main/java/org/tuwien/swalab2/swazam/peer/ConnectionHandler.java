package org.tuwien.swalab2.swazam.peer;

public class ConnectionHandler {
	public void remove(NodeConnection n) {
		System.out.println("stub");
	}
	
	public void closeAndRemove(NodeConnection n) {
		n.disconnect();
	}
}
