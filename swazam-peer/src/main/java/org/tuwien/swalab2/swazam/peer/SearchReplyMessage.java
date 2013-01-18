package org.tuwien.swalab2.swazam.peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SearchReplyMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8583493611380121826L;
	private String artist;
	private String title;
	private String album;
	
	public SearchReplyMessage(String ip, int port, String artist, String title, String album) throws UnknownHostException {
		super(ip,port);
		this.artist = artist;
		this.title = title;
		this.album = album;
	}
	
	private String getArtist() {
		return this.artist;
	}
	
	private String getTitle() {
		return this.title;
	}
	
	private String getAlbum() {
		return this.album;
	}

}
