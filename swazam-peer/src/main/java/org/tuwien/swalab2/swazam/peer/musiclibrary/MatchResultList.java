package org.tuwien.swalab2.swazam.peer.musiclibrary;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MatchResultList implements Serializable{	

	private List<MatchResult> resultList = new LinkedList<MatchResult>();
	

	public void add(MatchResult result){
	
		resultList.add(result);
	}

	public void remove(MatchResult result){
		resultList.remove(result);
	}
	
	public List<MatchResult> getList() {
		return resultList;
	}
}