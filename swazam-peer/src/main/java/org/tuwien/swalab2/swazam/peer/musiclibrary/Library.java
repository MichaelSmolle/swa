package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.log4j.Logger;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public class Library implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 530694478778623175L;
	//	private Logger log = Logger.getLogger(getClass());
	private HashSet<Mp3File> library = new HashSet<Mp3File>();
	private Integer id;
//	ConcurrentHashMap<File, MusicFile> library = new ConcurrentHashMap<File, MusicFile>();
	
	public Library(Integer id) {
		super();
		this.id = id;
	}

	public void add(File file){
		try {
			Mp3File mp3 = new Mp3File(file);
			library.add(mp3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void remove(File file){
//		library.remove(mfile);
		for (Mp3File mF : library){
			if (mF.getFile().equals(file)){
				library.remove(mF);
				this.persist();
			}
		}
	}
	
	public MatchResult match(Fingerprint fprint){
		MatchResult mr;
		for (Mp3File mFile : library){
			
			Double match = mFile.getFingerprint().match(fprint);
			if (match != -1){
				mr = new MatchResult(mFile.getFile());
			    return mr;	
			}
		}
		return null;
	}
	
	public List<String> list(){
		LinkedList<String> list = new LinkedList<String>();
//		Integer i = library.size();
		
//		while (i > 0) {
//			Mp3File mp3 = library.
//		}
//		
		Iterator<Mp3File> it = library.iterator();
		
		while (it.hasNext()) {
			Mp3File mp3 =  it.next();
			list.add(mp3.getPath());
		
			
		}
		
		return list;
	}
	
	public void merge( Library library ) {
		this.library.addAll(library.getAll());
	}
	
	public HashSet<Mp3File> getAll() {
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
		System.out.println("Saving library");
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


