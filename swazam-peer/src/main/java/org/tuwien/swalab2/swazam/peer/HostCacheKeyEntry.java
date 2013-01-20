package org.tuwien.swalab2.swazam.peer;

import java.io.Serializable;

public class HostCacheKeyEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6951132774557785036L;
	private String uid;

	
	public HostCacheKeyEntry(String uid) {
		this.uid = uid;
	}
	
	public boolean equals(HostCacheKeyEntry k) {
		return this.uid.compareTo(k.getUid()) == 0;
	}
	
	public String getUid() {
		return this.uid;
	}

}
