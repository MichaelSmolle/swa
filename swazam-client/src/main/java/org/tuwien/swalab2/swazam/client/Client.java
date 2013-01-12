package org.tuwien.swalab2.swazam.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.tuwien.swalab2.swazam.client.clientUI.SwingUI;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

/**
 * Hello world!
 * 
 */
public class Client {
private SwingUI swingUI;
    
	public static void main(String[] args) {
		Client client = new Client();
		client.setUp();
		client.startSwingUI(args);

		client.startCLI();

	}

	private void setUp() {
		System.out.println("Welcome to the SWAzam client.");

		// ToDo: somehow bootstrap
	}

	private void startSwingUI(String[] args) {
		swingUI = new SwingUI();
		//TODO: better
                swingUI.main(args);
	}

	private void startCLI() {
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
				
				if (cmd.equals("query")) {

					filename = s.next();

					File file = new File(filename);
					try {
						if (file.getName().contains("mp3")
								|| file.getName().contains("MP3")) {
							fingerprint = org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile.fingerprint(file);
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
                swingUI.close();
		System.out.println("Shutting down Client");
                
	}

	private void usage() {
		System.out.println("\n Interactive commands:"
				+ "\n - query <path to mp3>" 
				+ "\n - quit" 
				+ "\n - usage"
				+ "\n");
	}

}
