/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.tuwien.swalab2.services.PersonService;
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
    
    private Person user;
    
    private String userName;
    
    private String passWord;

    public AuthenticationBean() {
    }
    
    
    
    public String doLogin(){
        
        this.user = personService.login(userName, passWord);
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
    
    
}
