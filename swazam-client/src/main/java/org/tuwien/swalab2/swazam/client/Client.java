package org.tuwien.swalab2.swazam.client;

import ac.at.tuwien.infosys.swa.audio.*;

import java.awt.color.CMMException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Hello world!
 * 
 */
public class Client {

	public static void main(String[] args) {
		Client client = new Client();
		client.setUp();
		client.startSwingUI();

		client.startCLI(args);

	}

	private void setUp() {
		System.out.println("Welcome to the SWAzam client.");

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
				
				if (cmd.equals("query")) {

					filename = s.next();

					File file = new File(filename);
					try {
						if (file.getName().contains("mp3")
								|| file.getName().contains("MP3")) {
							fingerprint = fingerprint(file);
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

		System.out.println("Shutting down Client");

	}

	private void usage() {
		System.out.println("\n Interactive commands:"
				+ "\n - query <path to mp3>" 
				+ "\n - quit" 
				+ "\n - usage"
				+ "\n");
	}

	private Fingerprint fingerprint(File file) {

		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(file);

			System.out.println("Trying to fingerprint " + file.getName());

			Fingerprint resultFingerpring = FingerprintSystem
					.fingerprint(audioInputStream);

			System.out.println("Fingerprinting of file " + file.getName()
					+ " is: " + resultFingerpring.toString());

			return resultFingerpring;
		} catch (Exception e) {
			System.out.println("Somewhere something went horribly wrong...");
			System.out.println(e.toString());
		}

		return null;
	}
}
