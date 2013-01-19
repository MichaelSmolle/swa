package org.tuwien.swalab2.swazam.peer;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

//Handle the messages by calling the appropriate methods in the ConnectionHandler and the MusicLibrary
//TODO nearly everything
public class MessageHandler {
	
	private ConnectionHandler ch;
	
	public MessageHandler() {
		
	}
	
	public synchronized void handleMessage(Message m) {
		if(m instanceof SearchMessage) {
			System.out.println("stub");
			Fingerprint fingerprint = ((SearchMessage) m).getFingerprint();
			
		} else if (m instanceof requestPeerReplyMessage) {
			ch.addNodes(((requestPeerReplyMessage)m).getHostCache());
		} else if (m instanceof requestPeerMessage) {
			ch.replyToRequestNodes(m.getSender(), m.getSenderPort());
		}
	}
}
