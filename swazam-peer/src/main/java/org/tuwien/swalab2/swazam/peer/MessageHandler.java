package org.tuwien.swalab2.swazam.peer;

//Handle the messages by calling the appropriate methods in the ConnectionHandler and the MusicLibrary
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;
import org.tuwien.swalab2.swazam.peer.musiclibrary.MatchResult;

//TODO nearly everything
public class MessageHandler {

    private ConnectionHandler connectionHandler;
    private Library library;

    public MessageHandler(Library library, ConnectionHandler ch) {

        this.connectionHandler = connectionHandler;
        this.library = library;
    }

    public synchronized void handleMessage(Message m) {
        if (m instanceof SearchMessage) {

            SearchMessage thisMessage = (SearchMessage) m;

            //TODO: message über den ConnectionHandler forwarden

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

            } catch (IOException ex) {
                Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (m instanceof requestPeerReplyMessage) {
            System.out.println("TODO.");
            //ch.addNodes(((requestPeerReplyMessage) m).getHostCache());
        } else if (m instanceof requestPeerMessage) {
            System.out.println("TODO.");
            //ch.replyToRequestNodes(m.getSender(), m.getSenderPort());
        }
    }
}
