package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;

import org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Mp3File {
	private Fingerprint fingerprint;
	private File 		file;
	
	
	
	
	public Mp3File(File mp3) throws Exception {
		super();
		if (file.getName().contains("mp3") || file.getName().contains("MP3")) {
			this.file = mp3;
		} else {
			throw new Exception("The file must be of type mp3");
		}
		
		this.fingerprint = FingerprintFile.getFingerprint(file);
	}
	
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public String getName(){
		return file.getName();
	}
	
	public String getPath(){
		return file.getAbsolutePath();
	}
	
	public Fingerprint getFingerprint(){
		return fingerprint;
	}
	
}
