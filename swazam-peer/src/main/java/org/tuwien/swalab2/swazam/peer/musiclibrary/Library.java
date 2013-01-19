package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;
import java.util.concurrent.ConcurrentSkipListSet;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Library {
	ConcurrentSkipListSet<MusicFile> library = new ConcurrentSkipListSet<MusicFile>();
//	ConcurrentHashMap<File, MusicFile> library = new ConcurrentHashMap<File, MusicFile>();
	
	public void add(MusicFile mFile){
		library.add(mFile);
	}
	
	public void remove(File file){
//		library.remove(mfile);
		for (MusicFile mF : library){
			if (mF.getFile().equals(file)){
				library.remove(mF);
			}
		}
	}
	
	public MatchResultList match(Fingerprint fprint){
		MatchResultList resultList = new MatchResultList();
		
		for (MusicFile mFile : library){
			
			Double match = mFile.getFingerprint().match(fprint);
			if (match != -1){
				resultList.add(new MatchResult(match, mFile.getFile() ));
			}
		}
		
		return resultList;
	}
}


