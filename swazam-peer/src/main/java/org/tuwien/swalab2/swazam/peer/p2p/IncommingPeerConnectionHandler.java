package org.tuwien.swalab2.swazam.peer.p2p;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

public class IncommingPeerConnectionHandler extends IncommingConnectionHandler {
	
	private ConnectionHandler ch;
	
	public IncommingPeerConnectionHandler(int port, ConnectionHandler ch) {
		super(port);
		this.ch = ch;
		this.start();
	}
	
	private String getRemoteUid(Socket n, ObjectInputStream ois) {
		//we expect that the peer will send its uid after connecting
		try {
			return (String)ois.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void handleSocket(Socket n) {
		//System.out.println("Giving connection to handler");
		try {
			InputStream is = null;
			is 		 = n.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			String uid = this.getRemoteUid(n,ois);
			if(uid == null) {
				return;
			}
			this.ch.handleIncommingPeerConnection(n, uid, ois);	
			//System.out.println("Giving connection to handler done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
