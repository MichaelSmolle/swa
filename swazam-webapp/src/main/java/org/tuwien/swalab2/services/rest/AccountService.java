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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.tuwien.swalab2.dataaccess.PersonStorage;
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
  


    public AccountService() {
    }
    
    
    @Override
    @GET
    @Path("/login/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person login(@PathParam("id") String userId){
        Person res = PersonStorage.getInstance().getPerson(userId);
        return res;
    }

    @GET
    @Path("/balance/{userId}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccountBalanceForUser(@PathParam("id") String userId) {

        Person res = PersonStorage.getInstance().getPerson(userId);

        return String.valueOf(res.getAccount().getBalance());
    }

    @GET
    @Path("/update/{search}/{success}/{userId}")    
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public String updateAccountAfterSearch(@PathParam("search")String search,@PathParam("success")String success,@PathParam("userId")String userId) {
        
        Person p = PersonStorage.getInstance().getPerson(userId);
        if(p == null){
            return "Failed";
        }
      
        
        SwaZamTransaction t = new SwaZamTransaction();
        t.setSearchString(search);
        if("0".equals(success)){
            t.setSuccess(false);
        }else {
            t.setSuccess(true);
            p.getAccount().setBalance(p.getAccount().getBalance() - 1);
        }
        
        p.getAccount().getTransactions().add(t);
        
        
        
        return "OK";
    }
    
    
    @GET
    @Path("/update/{userId}")    
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public String updateAccountBalance(@PathParam("userId")String userId) {
        
        Person p = PersonStorage.getInstance().getPerson(userId);
        if(p == null){
            return "Failed";
        } 
       
        p.getAccount().setBalance(p.getAccount().getBalance() + 1);
        return "OK";
    }
    
    
  
  
    
}
