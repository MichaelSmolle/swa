package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Library {
	private Logger log = Logger.getLogger(getClass());
	private ConcurrentSkipListSet<Mp3File> library = new ConcurrentSkipListSet<Mp3File>();
	private Integer id;
//	ConcurrentHashMap<File, MusicFile> library = new ConcurrentHashMap<File, MusicFile>();
	
	public Library(Integer id) {
		super();
		this.id = id;
	}

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
	
	public void merge( Library library ) {
		this.library.addAll(library.getAll());
	}
	
	public ConcurrentSkipListSet<Mp3File> getAll() {
		return library;
	}
	
	// loads library from hd
	public void load(){
		File file = new File(id + ".lib");
		Library lib = new Library(id);
		try {
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(f);
			lib  = ((Library) s
					.readObject());
			s.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.merge(lib);
	}
	
	public void persist(){
		log.info("Saving library");
		File file = new File(id + ".lib");

		try {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(this);
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}


