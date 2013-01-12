/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.swazam.util;

import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author smolle
 */
public class Fingerprint {
    
    	public static ac.at.tuwien.infosys.swa.audio.Fingerprint fingerprint(File file) {

		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(file);

			System.out.println("Trying to fingerprint " + file.getName());

			ac.at.tuwien.infosys.swa.audio.Fingerprint resultFingerpring = FingerprintSystem
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
