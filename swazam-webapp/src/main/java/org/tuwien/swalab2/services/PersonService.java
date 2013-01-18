/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.services;

import java.io.Serializable;
import javax.inject.Inject;
import org.tuwien.swalab2.ejb.PersonFacade;
import org.tuwien.swalab2.swazam.util.model.entities.Person;

/**
 *
 * @author gh
 */
public class PersonService implements Serializable{
    @Inject
    private PersonFacade personFacade;
    
    
    public Person login(String userName,String passWord){
        return personFacade.authenticatePerson(userName, passWord);
    }
    
    public Person saveChanges(Person user){
        personFacade.edit(user);
        return user;
    }
    
    
    
}
