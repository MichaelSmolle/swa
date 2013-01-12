/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.services.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author gh
 */
@Path("/notes")
@Produces ({"application/json"})
public class RestTest {

    public RestTest() {
    }
    
     
     @GET 
     public String list() {
            return "Hello World, I still need some work to be useful!";
     }
    
}
