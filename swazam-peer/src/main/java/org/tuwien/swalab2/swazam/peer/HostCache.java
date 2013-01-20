package org.tuwien.swalab2.swazam.peer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.tuwien.swalab2.swazam.peer.musiclibrary.Library;


public class HostCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6511865572072288189L;
	private ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> hostCache = new ConcurrentHashMap< HostCacheKeyEntry, HostCacheEntry>();

	public ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> getHostCache() {
		return hostCache;
	}

	public void ConcurrentHashMap(ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> hostCache) {
		this.hostCache = hostCache;
	}
	
	public void load(){
		File file = new File("host_cache.bin");
		HostCache lib = new HostCache();
		try {
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(f);
			lib  = ((HostCache) s
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
		//System.out.println("Saving library");
		File file = new File("host_cache.bin");

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
	
	
	public HostCacheEntry search(HostCacheKeyEntry key){
		HostCacheEntry entry = null;
		entry = hostCache.get(key);
		return entry;
	}
	
	public void remove(HostCacheKeyEntry key){
		hostCache.remove(key);
	}
	
	public HostCacheEntry removeGet(HostCacheKeyEntry key){
		HostCacheEntry entry = null;
		entry = hostCache.get(key);
		remove(key);
		return entry;
	}
	
	public void add(HostCacheEntry entry){
		HostCacheKeyEntry key = new HostCacheKeyEntry(entry.getAdr(), entry.getPort());
		hostCache.put(key, entry);
	}
	
	public void merge(HostCache a){
                ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> cm = a.getHostCache();
                hostCache.putAll(cm);            
	}
}

