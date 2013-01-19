package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;

public class MatchResult {	
	private Double startTime;
	private File file;
	
	
	
	public MatchResult(Double startTime, File file) {
		super();
		this.startTime = startTime;
		this.file = file;
	}
	
	public Double getStartTime() {
		return startTime;
	}

	public File getFile() {
		return file;
	}

}
