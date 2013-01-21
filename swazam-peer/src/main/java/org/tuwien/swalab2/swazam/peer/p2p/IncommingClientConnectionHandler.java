package org.tuwien.swalab2.swazam.peer.p2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class IncommingClientConnectionHandler extends IncommingConnectionHandler {

	private MessageHandler mh;
	
	public IncommingClientConnectionHandler(int port, MessageHandler mh) {
		super(port);
		this.mh = mh;
		this.start();
	}
	
	@Override
	public void handleSocket(Socket n) {
		InputStream is = null;
		ObjectInputStream ois = null;
		try {
			is = n.getInputStream();
			ois = new ObjectInputStream(is);
			this.mh.handleMessage((Message)ois.readObject(), null);
                        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ois.close();
		} catch (Exception e) {}
		try {
			is.close();
		} catch (Exception e) {}
		try {
			n.close();
		} catch (IOException e) {}
	}

}
