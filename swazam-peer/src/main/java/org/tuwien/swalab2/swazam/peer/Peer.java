package org.tuwien.swalab2.swazam.peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import javax.rmi.CORBA.Util;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;
import ac.at.tuwien.infosys.swa.audio.SubFingerprint;

import com.kenmccrary.jtella.*;

/**
 * Hello world!
 * 
 */
public class Peer implements MessageReceiver {

	private ConcurrentHashMap<Fingerprint, File> library = new ConcurrentHashMap<Fingerprint, File>();
	private static Integer id = null;
	private GNUTellaConnection connection;

	public Peer(Integer arg0) {
		id = arg0;
	}
	
	public void receiveSearch(SearchMessage m) {
	         System.out.println("stub");                                  
	}
	
	public void receiveSearchReply(SearchReplyMessage m) {
		System.out.println("stub");
	}
	
	public void receivePush(PushMessage m) {
		System.out.println("stub");	
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Integer arg0;

		try {
			arg0 = Integer.valueOf(args[0]);
		} catch (NumberFormatException e) {

			System.out.println("peer [id]");
			return;
		}

		Peer peer = new Peer(arg0);
		peer.setUp();

		// load library
		File file = new File(id + ".lib");
		try {
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(f);
			peer.library = ((ConcurrentHashMap<Fingerprint, File>) s
					.readObject());
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not load library, setting up new one");
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		peer.startSwingUI();

		peer.startCLI(args);

	}

	private void setUp() {
		System.out.println("Welcome to the SWAzam client.");
		try {
			this.connection = new GNUTellaConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connection.getHostCache().addHost(new Host("91.186.155.61",37000, 10, 10));
		this.connection.getHostCache().addHost(new Host("91.186.155.61",37000, 10, 10));
		this.connection.getHostCache().addHost(new Host("91.186.155.61",37000, 10, 10));
		//this.hostCache.addHost(new Host("",37000));
		
		// ToDo: somehow bootstrap
	}

	private void startSwingUI() {
		// ToDo: start Swing UI
	}

	private void startCLI(String[] args) {

		String cmd = "";
		String filename = "";
		Fingerprint fingerprint = null;

		usage();

		boolean run = true;
		while (run) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
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

				if (cmd.equals("add")) {

					if (s.hasNextInt()) {
						// TODO: send file to peer
						System.out.println("TODO: send file to peer");
						Integer id = Integer.valueOf(s.next());
					} else {
						filename = s.next();

						File file = new File(filename);
						try {
							if (file.getName().contains("mp3")
									|| file.getName().contains("MP3")) {
								fingerprint = org.tuwien.swalab2.swazam.util.Fingerprint
										.fingerprint(file);
								library.put(fingerprint, file);
							} else {
								System.out
										.println("The file must be of type mp3 \n");
							}
						} catch (Exception e) {
							System.out.println("Couldn't find file \n");
						}
					}
				} else if (cmd.equals("list")) {
					
					SearchSession s = connection.createSearchSession("bla",10,1,this);
					
					Iterator<Entry<Fingerprint, File>> it = library.entrySet()
							.iterator();
					while (it.hasNext()) {
						System.out.println(it.next().getValue());
					}
				} else if (cmd.equals("match")) {
					// org.tuwien.swalab2.swazam.util.Fingerprint.fingerprint("file");
					double time = -1;
					filename = s.next();

					File file = new File(filename);
					try {
						if (file.getName().contains("mp3")
								|| file.getName().contains("MP3")) {
							fingerprint = org.tuwien.swalab2.swazam.util.Fingerprint
									.fingerprint(file);
							
							Fingerprint[] fingerprints = fingerprint.split();
							System.out.println("split fingerprint count");
							System.out.println(fingerprints.length);
							System.out.println();
//							System.out.println(fingerprints[0]);
//							System.out.println();
							
							SubFingerprint[] sfingerprints = fingerprints[0].getSubFingerprints();
							System.out.println("subfingerprint count");
							System.out.println(sfingerprints.length);
							System.out.println();

							Iterator<Entry<Fingerprint, File>> it = library
									.entrySet().iterator();
							while (it.hasNext()) {

								
								time = it.next().getKey().match(fingerprint);
								System.out.print(time + "\n");
							}
						} else {
							System.out
									.println("The file must be of type mp3 \n");
						}
					} catch (Exception e) {
						System.out.println("Couldn't find file \n");
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

		System.out.println("Saving library");
		File file = new File(id + ".lib");

		try {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(library);
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Shutting down Peer");

	}

	private void usage() {
		System.out.println("\n Interactive commands:"
				+ "\n - add <path to mp3>" + "\n - add <id> <path to mp3>"
				+ "\n - match <path to mp3>" + "\n - list" + "\n - quit"
				+ "\n - usage" + "\n");
	}

}
