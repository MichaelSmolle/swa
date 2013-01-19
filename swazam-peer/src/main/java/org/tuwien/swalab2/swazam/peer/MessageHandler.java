package org.tuwien.swalab2.swazam.peer;

//Handle the messages by calling the appropriate methods in the ConnectionHandler and the MusicLibrary
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;
import org.tuwien.swalab2.swazam.peer.musiclibrary.MatchResultList;

//TODO nearly everything
public class MessageHandler {

    private ConnectionHandler ch;

    public MessageHandler() {
    }

    public synchronized void handleMessage(Message m) {
        if (m instanceof SearchMessage) {

            SearchMessage thisMessage = (SearchMessage) m;

            //TODO: message über den ConnectionHandler forwarden

            Library library;
            MatchResultList matchResultList = library.match(thisMessage.getFingerprint());

            if (matchResultList.getList().size() > 0) {

                Socket replySocket = null;
                ObjectOutputStream out = null;

                try {
                    replySocket = new Socket(thisMessage.getSender(), thisMessage.getSenderPort());
                    out = new ObjectOutputStream(replySocket.getOutputStream());
                } catch (UnknownHostException e) {
                    System.err.println("Cannot find the client  " + thisMessage.getSender() + ":" + thisMessage.getSenderPort() + ".");
                    System.exit(1); //TODO: remove
                } catch (IOException e) {
                    System.err.println("Could not connect to client " + thisMessage.getSender() + ":" + thisMessage.getSenderPort() + ".");
                    //System.exit(1);
                }
                
                SearchReplyMessage searchReplyMessage = new SearchReplyMessage(ip, port, thisMessage.getId(), filename);
                
                try {
                    
                    out.writeObject(searchReplyMessage);
                    out.flush();
                    out.close();

                } catch (IOException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

           } else {
                System.out.println("No results found for searchMessage:" + thisMessage.getId() + ".");
            }

        } else if (m instanceof requestPeerReplyMessage) {
            ch.addNodes(((requestPeerReplyMessage) m).getHostCache());
        } else if (m instanceof requestPeerMessage) {
            ch.replyToRequestNodes(m.getSender(), m.getSenderPort());
        }
    }
}
