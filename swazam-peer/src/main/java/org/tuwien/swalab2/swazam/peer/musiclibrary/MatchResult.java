package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;
import java.io.Serializable;

public class MatchResult implements Serializable{	

	private String filename = "";
	private String artist = "";
	private String title = "";
	private String album = "";
	
	
	
	public MatchResult(File file) {
		this.filename = file.getName();
	}



	public String getFilename() {
		return filename;
	}
	public String getArtist() {
		return artist;
	}
	public String getTitle() {
		return title;
	}
	public String getAlbum() {
		return album;
	}
}
