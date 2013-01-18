/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.interfaces;

import java.util.List;

/**
 *
 * @author gh
 */
public interface IPeerServices {

    
    public void registerPeer(String address);
    public List<String> getPeerList();
    
}
