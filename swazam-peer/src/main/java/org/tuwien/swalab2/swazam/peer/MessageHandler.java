package org.tuwien.swalab2.swazam.peer;

//Handle the messages by calling the appropriate methods in the ConnectionHandler and the MusicLibrary
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;
import org.tuwien.swalab2.swazam.peer.musiclibrary.MatchResult;

//TODO nearly everything
public class MessageHandler {

    private ConnectionHandler connectionHandler;
    private Library library;
    private ConcurrentHashMap<String, Message> knownMessages;

    public MessageHandler(Library library, ConnectionHandler ch) {

        this.connectionHandler = ch;
        this.library = library;
        this.knownMessages = new ConcurrentHashMap<String, Message>();
    }

    public void handleMessage(Message m, String uid) {
    	//check if we already have seen this message
    	//if the id is null its a message that will not be forwarded and therefore there is no need to remember it
    	//TODO clear this sometime
    	if(m.getId() != null) {
    		if(this.knownMessages.containsKey(m.getId())) {
    			return;
    		}
    		this.knownMessages.put(m.getId(), m);
    	}
    	
        if (m instanceof SearchMessage) {

            SearchMessage thisMessage = (SearchMessage) m;

            this.connectionHandler.forwardMessage(m);

            MatchResult matchResult = library.match(thisMessage.getFingerprint());

            Socket replySocket = null;
            ObjectOutputStream out = null;

            try {
                replySocket = new Socket(thisMessage.getSender(), thisMessage.getSenderPort() + 1);
                out = new ObjectOutputStream(replySocket.getOutputStream());
            } catch (UnknownHostException e) {
                System.err.println("Cannot find the client  " + thisMessage.getSender() + ":" + thisMessage.getSenderPort() + ".");
                System.exit(1); //TODO: remove
            } catch (IOException e) {
                System.err.println("Could not connect to client " + thisMessage.getSender() + ":" + thisMessage.getSenderPort() + ".");
                //System.exit(1);
            }

            // TODO: IP und Port von beantworteten einbauen
            SearchReplyMessage searchReplyMessage = null;

            if (matchResult != null) {
                try {
                    searchReplyMessage = new SearchReplyMessage(thisMessage.getSender().getHostAddress(), thisMessage.getSenderPort(), thisMessage.getId(), matchResult.getFilename());
                    System.out.println("searchReplyMessage: " + searchReplyMessage.getFilename());
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                try {
                    searchReplyMessage = new SearchReplyMessage(thisMessage.getSender().getHostAddress(), thisMessage.getSenderPort(), thisMessage.getId(), "No results found.");
                    System.out.println("No results found for searchMessage:" + thisMessage.getId() + ".");
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                out.writeObject(searchReplyMessage);
                out.flush();
                out.close();
                replySocket.close();

            } catch (IOException ex) {
                Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (m instanceof requestPeerReplyMessage) {
        	System.out.println("Received a requestPeerReplyMessage with the following nodes:");
        	connectionHandler.addNodes(((requestPeerReplyMessage) m).getHostCache());
        } else if (m instanceof requestPeerMessage) {
        	connectionHandler.replyToRequestNodes(uid);
        }
    }
}
