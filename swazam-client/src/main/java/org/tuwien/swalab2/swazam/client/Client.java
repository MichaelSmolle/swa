package org.tuwien.swalab2.swazam.client;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tuwien.swalab2.swazam.client.clientUI.SwingUI;
import org.tuwien.swalab2.swazam.peer.SearchMessage;
import org.tuwien.swalab2.swazam.peer.SearchReplyMessage;

public class Client {

    private SwingUI swingUI;
    private Socket socket = null;
    private InetAddress ip;
    private Integer port;
    private static ServerSocket serverSocket;
    private Socket initSocket = null;
    private ObjectOutputStream out = null;
 
    private static class ClientThread extends Thread {

        private Socket socket = null;
        private SearchReplyMessage replyMessage;

        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            System.out.println("Received a SearchReplyMessage...");

            ObjectInputStream in = null;

            try {
                in = new ObjectInputStream(socket.getInputStream());

                try {
                    replyMessage = (SearchReplyMessage) in.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("artist: " + replyMessage.getArtist());
                System.out.println("album: " + replyMessage.getAlbum());
                System.out.println("title: " + replyMessage.getTitle());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TcpDispatcher implements Runnable {

        ServerSocket server;

        TcpDispatcher(ServerSocket serverSocket) {
            this.server = serverSocket;
        }

        public void run() {

            Socket client = null;

            try {
                while (true) {
                    client = server.accept();
                    Thread t = new Thread(new ClientThread(client));
                    t.start();
                }

            } catch (SocketException e) {
                System.out.println("DEBUG: SocketException occurred.");
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.setUp();
            //client.startSwingUI(args);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        client.startCLI();

    }

    private void setUp() throws IOException {
        System.out.println("Welcome to the SWAzam client.");

        // ToDo: somehow bootstrap

        ip = InetAddress.getByName("93.82.83.41");
        port = 37000;

        try {
            initSocket = new Socket(ip, port); //TODO: add args
            out = new ObjectOutputStream(initSocket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Cannot find the peer  " + ip + ":" + port + ".");
            //serverSocket.close();
            System.exit(1); //TODO: remove
        } catch (IOException e) {
            System.err.println("Could not connect to peer " + ip + ".");
            //initSocket.close();
            //System.exit(1);
        }

    }

    private void startSwingUI(String[] args) {
        swingUI = new SwingUI();
        //TODO: better
        //swingUI.main(args);
    }

    private void startCLI() {
        String cmd;
        String filename;
        Fingerprint fingerprint;

        usage();

        boolean run = true;
        while (run) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String input;
            Scanner s = null;

            try {
                input = in.readLine();
                s = new Scanner(input);

            } catch (IOException e) {
                System.err.println("Couldn't read line.");
            }

            if (s.hasNext()) {
                cmd = s.next();

                if (cmd.equals("query")) {

                    filename = s.next();

                    File file = new File(filename);
                    
                    try {
                        if (file.getName().contains("mp3") || file.getName().contains("MP3")) {

                            if (file.exists()) {
                                fingerprint = org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile.getFingerprint(file);
                                System.out.println("Fingerprint is: " + fingerprint.toString());
                                
                                submitRequest(fingerprint);
                                
                            } else {
                                System.out.println("Couldn't find file " + filename + ".\n");
                            }
                        } else {
                            System.out.println("The file must be of type mp3.\n");
                        }
                    } catch (Exception e) {
                        System.out.println("An exception ocurred.");
                        System.out.println(e.getMessage());
                    }
                    
                } else if (cmd.equals("usage")) {
                    usage();
                } else if (cmd.equals("quit")) {
                    run = false;
                } else {
                    usage();
                }
            }
        }
        //swingUI.close();
        System.out.println("Shutting down client");

    }

    private void usage() {
        System.out.println("\n Interactive commands:"
                + "\n - query <path to mp3>"
                + "\n - quit"
                + "\n - usage"
                + "\n");
    }

    private void submitRequest(Fingerprint fingerprint) {
        
        System.out.println("Submitting FingerPrint to network...");
        
        try {

            SearchMessage searchMessage = new SearchMessage("127.0.0.1", 37010, fingerprint, "test");

            out.writeObject(searchMessage);
            out.flush();
            out.close();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
