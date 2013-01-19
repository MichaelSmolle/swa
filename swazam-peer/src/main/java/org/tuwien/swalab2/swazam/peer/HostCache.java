package org.tuwien.swalab2.swazam.peer;
import java.util.concurrent.ConcurrentHashMap;


public class HostCache {

	private ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> hostCache = new ConcurrentHashMap< HostCacheKeyEntry, HostCacheEntry>();

	public ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> getHostCache() {
		return hostCache;
	}

	public void ConcurrentHashMap(ConcurrentHashMap<HostCacheKeyEntry, HostCacheEntry> hostCache) {
		this.hostCache = hostCache;
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
}

