/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Singleton;

/**
 *
 * @author gh
 */

public final class PeerStorage implements Serializable{
    
    private static PeerStorage peerStorage;
    private ArrayList<String> peers = new ArrayList<String>();
    
    private PeerStorage(){
       
    }
    
    public synchronized static PeerStorage getInstance() 
    {
        if (peerStorage == null) 
        {
            peerStorage = new PeerStorage();
        }
        return peerStorage;
    }
    
    
    public void addPeerToStorage(String address){
       boolean duplicate = false;
        for(String a : peers ){
            if(a.equals(address)){
                duplicate = true;
                System.out.println("Duplicate peer "+ address);
                
                break;
                
            }
        }
        
        if(!duplicate){
            peers.add(address);
                System.out.println("Added new peer to list "+ address);
        }
        
        
    
    }
    
    public ArrayList<String> getRegisteredPeerList(){
        return peers;
    }
    
    public String getRegisteredPeerListAsString(){
        String res = "";
        for(String x : peers){
            res += res.concat(x).concat("-");
        }        
        return res;
    }
    
}
