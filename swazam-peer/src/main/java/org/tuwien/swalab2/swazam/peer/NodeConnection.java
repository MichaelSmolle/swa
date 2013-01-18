package org.tuwien.swalab2.swazam.peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NodeConnection {
	
	private Socket 				s;
	private MessageHandler 		mh;
	private ConnectionHandler   ch;
	private MessageReceiver 	mr;
	
	public NodeConnection(Socket socket, MessageHandler mh, ConnectionHandler ch) {
		this.s = socket;
		this.mh = mh;
		this.ch = ch;
		this.mr = new MessageReceiver(this.mh, this.s);
	}
	
	public synchronized void sendMessage(Message m) {
		  
		try {
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(m);
			oos.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			this.disconnect();
		} 
	}
	
	public synchronized void disconnect() {
		try {
			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.ch.remove(this);
	}
	
	private class MessageReceiver extends Thread {
		private MessageHandler mh;
		private Socket s;
		
		public MessageReceiver(MessageHandler mh, Socket s) {
			this.mh = mh;
			this.s = s;
			this.run();
		}
		
		public void run() {
			InputStream is;
			ObjectInputStream ois = null;
			try {
				is = this.s.getInputStream();
				ois = new ObjectInputStream(is);
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("todo");
			}  
			
			while(true) {
				try {
					mh.handleMessage((Message)ois.readObject());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
