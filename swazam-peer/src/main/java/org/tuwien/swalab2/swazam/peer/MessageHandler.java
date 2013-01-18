package org.tuwien.swalab2.swazam.peer;

public class MessageHandler {
	
	public MessageHandler() {
		
	}
	
	public void handleMessage(Message m) {
		if(m instanceof SearchMessage) {
			System.out.println("stub");
		}
	}
}
