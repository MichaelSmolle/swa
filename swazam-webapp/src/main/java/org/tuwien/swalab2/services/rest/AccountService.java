/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.services.rest;

import java.io.Serializable;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.tuwien.swalab2.ejb.AccountFacade;
import org.tuwien.swalab2.ejb.PersonFacade;
import org.tuwien.swalab2.interfaces.IAccountService;
import org.tuwien.swalab2.swazam.util.model.entities.Person;
import org.tuwien.swalab2.swazam.util.model.entities.SwaZamTransaction;

/**
 *
 * @author gh
 */
@Path("/account")
public class AccountService implements IAccountService,Serializable{
  
   AccountFacade accountFacade = new AccountFacade();
  
  
   @Inject 
    private PersonFacade personFacade;

    public AccountService() {
    }
    
    
    @Override
    @GET
    @Path("/login/{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public Person login(@PathParam("id") String userId){
        Person res = personFacade.find( Long.valueOf(userId));
        return res;
    }
    
    @Override
    public int getAccountBalanceForUser(@PathParam("id") String userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<SwaZamTransaction> getSearchHistoryForUser(String userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
  
  
    
}
