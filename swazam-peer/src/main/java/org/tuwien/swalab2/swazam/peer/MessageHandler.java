package org.tuwien.swalab2.swazam.peer;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

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
    private Socket communicationPartner = null;

    public MessageHandler(Library library) {
        
        //this.connectionHandler = connectionHandler;
        //this.communicationPartner = communicationPartner;
        this.library = library;
    }

    public synchronized void handleMessage(Message m) {
        if (m instanceof SearchMessage) {

            SearchMessage thisMessage = (SearchMessage) m;

            //TODO: message über den ConnectionHandler forwarden

            MatchResult matchResult = library.match(thisMessage.getFingerprint());

            if (matchResult != null) {

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
                
                // TODO: IP und Port von beantworteten einbauen
                SearchReplyMessage searchReplyMessage;
                try {
                    searchReplyMessage = new SearchReplyMessage(thisMessage.getSender().toString(), thisMessage.getSenderPort(), thisMessage.getId(), matchResult.getFilename());
                      
                    out.writeObject(searchReplyMessage);
                    out.flush();
                    out.close();
                    
              } catch (UnknownHostException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                } 

           } else {
                System.out.println("No results found for searchMessage:" + thisMessage.getId() + ".");
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
