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
public class ClientRestClient {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/swazam-webapp/api";

    public ClientRestClient() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        config.getClasses().add(StringProvider.class);

        client = Client.create(config);

        webResource = client.resource(BASE_URI).path("account");
    }

    public String login(String userName, String passWord) throws UniformInterfaceException {
        WebResource resource = webResource;
        resource = resource.path(java.text.MessageFormat.format("login/{0}/{1}", new Object[]{userName, passWord}));
        String userId = (String) resource.accept(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Got userId " + userId);
        return userId;
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
