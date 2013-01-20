/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.controller;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author gh
 */
@SessionScoped
@ManagedBean
public class JsfHelper implements Serializable{
    
    
    
    public void postStatusMessage(String message){
        FacesContext context  = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(message));
    }
    
}
