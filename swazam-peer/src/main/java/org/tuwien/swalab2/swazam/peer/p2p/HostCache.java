package org.tuwien.swalab2.swazam.peer.p2p;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;



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
	
	public LinkedList<HostCacheEntry> toLinkedList() {
		Enumeration<HostCacheEntry> e = this.hostCache.elements();
		HostCacheEntry cur = null;
		LinkedList<HostCacheEntry> l = new LinkedList<HostCacheEntry>();
		while(e.hasMoreElements()) {
			l.add(e.nextElement());
		}
		return l;
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
		HostCacheKeyEntry key = new HostCacheKeyEntry(entry.getUid());
		hostCache.put(key, entry);
	}
	
	public void merge(LinkedList<HostCacheEntry> a){
		boolean found;
		HostCacheEntry cur = null;
		for(int i = 0; i< a.size(); i++) {
			found = false;
			cur = a.get(i);
			Enumeration<HostCacheEntry> b = this.hostCache.elements();
			HostCacheEntry old = null;
			while(b.hasMoreElements()) {
				old = b.nextElement();
				if(old.getUid().compareTo(cur.getUid()) == 0) {
					found = true;
					break;
				}
			}
			if(!found) {
				this.hostCache.put(new HostCacheKeyEntry(cur.getUid()), cur);
			}
		} 
	}
	
	public void merge(HostCache a){
		Enumeration<HostCacheEntry> e = a.getHostCache().elements();
		HostCacheEntry cur = null;
		boolean found;
		while(e.hasMoreElements()) {
			found = false;
			cur = e.nextElement();
			Enumeration<HostCacheEntry> b = this.hostCache.elements();
			HostCacheEntry old = null;
			while(b.hasMoreElements()) {
				old = b.nextElement();
				if(old.getUid().compareTo(cur.getUid()) == 0) {
					found = true;
					break;
				}
			}
			if(!found) {
				this.hostCache.put(new HostCacheKeyEntry(cur.getUid()), cur);
			}
		} 
	}	
}

