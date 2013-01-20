package org.tuwien.swalab2.swazam.peer.p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Wait for IncommingConnections
public abstract class IncommingConnectionHandler extends Thread {
	
	private ServerSocket s;
	private volatile boolean running;
	
	public IncommingConnectionHandler(int port) {
		try {
			this.s = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.running = true;
	}
	
	public void kill() {
		this.running = false;
		try {
			this.s.close();
		} catch (IOException e) {}
	}
	
	public void run() {
		while(this.running) {
			try {
				this.handleSocket(s.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public abstract void handleSocket(Socket n);

}
