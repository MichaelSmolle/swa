/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.controller;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.tuwien.swalab2.dataaccess.PersonService;
import org.tuwien.swalab2.swazam.util.model.entities.Person;

/**
 *
 * @author gh
 */
@ManagedBean
@SessionScoped
public class AuthenticationBean implements Serializable{
   
    @Inject
    private PersonService personService;
    
    @ManagedProperty("#{jsfHelper}")
    JsfHelper jsfHelper;
    
    private Person user;
    
    private String userName;
    
    private String passWord;

    public AuthenticationBean() {
    }
    
    
    
    public String doLogin(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        
        this.user = personService.login(userName, passWord);
        if(user == null){
            jsfHelper.postStatusMessage("Login Failed");
        }
        else{
            jsfHelper.postStatusMessage("Login Success");
        }
        return "account";
    }
    
    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }
    
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public JsfHelper getJsfHelper() {
        return jsfHelper;
    }

    public void setJsfHelper(JsfHelper jsfHelper) {
        this.jsfHelper = jsfHelper;
    }
    
    
    
}
