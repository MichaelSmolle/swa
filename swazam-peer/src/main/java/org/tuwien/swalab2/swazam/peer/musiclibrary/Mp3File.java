package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;
import java.io.Serializable;

import org.tuwien.swalab2.swazam.util.fingerprint.FingerprintFile;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Mp3File implements Serializable{


	private Fingerprint fingerprint;
	private File 		file;
	
	
	public Mp3File(File mp3) throws Exception {
		super();
		if (mp3.getName().contains("mp3") || mp3.getName().contains("MP3")) {

			this.file = mp3;
		} else {
			throw new Exception("The file must be of type mp3");
		}
		this.fingerprint = FingerprintFile.getFingerprint(mp3);
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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result
				+ ((fingerprint == null) ? 0 : fingerprint.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mp3File other = (Mp3File) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (fingerprint == null) {
			if (other.fingerprint != null)
				return false;
		} else if (!fingerprint.equals(other.fingerprint))
			return false;
		return true;
	}






	
}
