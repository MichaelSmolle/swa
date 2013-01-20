/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.swazam.peer;

import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.impl.provider.entity.StringProvider;
import javax.ws.rs.core.MediaType;

/**
 * Jersey REST client generated for REST resource:PeerServices [/peer]<br>
 *  USAGE:
 * <pre>
 *        PeerClient client = new PeerClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author smolle
 */
public class PeerRestClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/swazam-webapp/api";

    public PeerRestClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        config.getClasses().add(StringProvider.class);
        
			client = Client.create(config);

        webResource = client.resource(BASE_URI).path("peer");
    }

    public void registerPeer(String address) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path(java.text.MessageFormat.format("add/{0}", new Object[]{address}));
    }

    public String getPeerList() throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path("peerlist");
        return resource.accept(MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.destroy();
    }
    
}
