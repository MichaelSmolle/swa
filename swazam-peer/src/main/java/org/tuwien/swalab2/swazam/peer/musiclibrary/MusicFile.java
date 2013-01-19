package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;

import org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class MusicFile {
	private Fingerprint fingerprint;
	private File 		file;
	
	
	
	
	public MusicFile(File file) {
		super();
		this.file = file;
		
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
