package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Library {
	Logger log = Logger.getLogger(getClass());
	ConcurrentSkipListSet<Mp3File> library = new ConcurrentSkipListSet<Mp3File>();
//	ConcurrentHashMap<File, MusicFile> library = new ConcurrentHashMap<File, MusicFile>();
	
	public void add(File file){
		try {
			library.add(new Mp3File(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info(e.getMessage());
		}
	}
	
	public void remove(File file){
//		library.remove(mfile);
		for (Mp3File mF : library){
			if (mF.getFile().equals(file)){
				library.remove(mF);
			}
		}
	}
	
	public MatchResultList match(Fingerprint fprint){
		MatchResultList resultList = new MatchResultList();
		
		for (Mp3File mFile : library){
			
			Double match = mFile.getFingerprint().match(fprint);
			if (match != -1){
				resultList.add(new MatchResult(match, mFile.getFile() ));
			}
		}
		
		return resultList;
	}
	
	public List<String> list(){
		LinkedList<String> list = new LinkedList<String>();
		
		for (Mp3File mp3 : library){
			list.add(mp3.getPath().toString());
		}
		
		return list;
	}
}


