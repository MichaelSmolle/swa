/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.interfaces;

/**
 *
 * @author gh
 */
public interface IPeerServices {


    /**
     * Register an peer with its address , use something like ip:port:peerid
     * @param address the address to identify the peer  ip:port:peerid
     */
    public void registerPeer(String address);

    /**
     * Get all registerd peers.
     * @return A string containgin all peers ip:port:peerid-ip:port:peerid  "-" is delimiter
     */
    public String getPeerList();
    
}
