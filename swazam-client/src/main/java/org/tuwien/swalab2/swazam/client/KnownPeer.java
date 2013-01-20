package org.tuwien.swalab2.swazam.client;

import java.net.InetAddress;

/**
 *
 * @author Christoph Derndorfer
 */
public class KnownPeer {
    
    private InetAddress ip;
    private Integer port;
    private String id;

    public InetAddress getIp() {
        return ip;
    }

    public KnownPeer(InetAddress ip, Integer port, String id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
