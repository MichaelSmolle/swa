/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import org.tuwien.swalab2.services.rest.PersonService;

/**
 *
 * @author gh
 */
@ManagedBean
@RequestScoped
public class EditPersonBean implements Serializable{

    @ManagedProperty("#{authenticationBean}")
    AuthenticationBean authenticationBean;
    
    @Inject
    PersonService personService;
    
    
    
    
    public EditPersonBean() {
        
    }
    
    public String saveChanges(){
        authenticationBean.setUser(personService.saveChanges(authenticationBean.getUser()));        
        return "account";
    }

    public String goToEditPage(){
        return "editPerson?faces-redirect=true";
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    
    
    
    
    
    
    
    
    
}
