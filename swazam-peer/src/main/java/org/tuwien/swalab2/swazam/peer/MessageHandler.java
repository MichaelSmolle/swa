package org.tuwien.swalab2.swazam.peer;

public class MessageHandler {
	
	private ConnectionHandler ch;
	
	public MessageHandler() {
		
	}
	
	public synchronized void handleMessage(Message m) {
		if(m instanceof SearchMessage) {
			System.out.println("stub");
		} else if (m instanceof requestPeerReplyMessage) {
			ch.addNodes(((requestPeerReplyMessage)m).getHostCache());
		} else if (m instanceof requestPeerMessage) {
			ch.replyToRequestNodes(m.getSender(), m.getSenderPort());
		}
	}
}
