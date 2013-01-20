/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.swazam.client.clientUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.tuwien.swalab2.swazam.client.Client;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Cli extends Thread{
	private Client client;
	private String cmd = "";
	private String filename = "";
	private Fingerprint fingerprint;
	private boolean run = true;


	public Cli(Client client) {
		this.client = client;
		this.start();
	}


	public void run() {

		usage();

		
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
						if (file.getName().contains("mp3") || file.getName().contains("MP3")) {

							if (file.exists()) {
								fingerprint = org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile.getFingerprint(file);
								client.submitRequest(fingerprint);

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
					close();
				} else {
					usage();
				}
			}
		}
	}

	private void usage() {
		System.out.println("\n Interactive commands:"
				+ "\n - query <path to mp3>"
				+ "\n - quit"
				+ "\n - usage"
				+ "\n");
	}
	
	public void close() {
		run = false;
//		client.shutdown();
	}
}
