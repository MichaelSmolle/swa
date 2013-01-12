/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.controller;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.tuwien.swalab2.ejb.AccountFacade;

/**
 *
 * @author gh
 */
@Named("accountDetails")
@RequestScoped
public class AccountDetailsBean {
    
    
    @Inject
    private AccountFacade accountFacade;
    
    public AccountDetailsBean(){
        
    }
    
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
