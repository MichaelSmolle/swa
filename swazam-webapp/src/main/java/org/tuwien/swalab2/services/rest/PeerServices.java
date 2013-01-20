/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.services.rest;

import org.tuwien.swalab2.dataaccess.PeerStorage;
import java.io.Serializable;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.tuwien.swalab2.interfaces.IPeerServices;

/**
 *
 * @author gh
 */
@Path("/peer")
public class PeerServices implements IPeerServices,Serializable{    
    
    @Override
    @GET
    @Path("/add/{address}")
    public String registerPeer(@PathParam("address") String address) {
        PeerStorage.getInstance().addPeerToStorage(address);
        return "OK";
    }

    @Override
    @GET
    @Path("/peerlist")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPeerList() {
        return PeerStorage.getInstance().getRegisteredPeerListAsString();
    }
}