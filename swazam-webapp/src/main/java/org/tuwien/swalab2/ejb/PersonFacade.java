/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.ejb;

import java.sql.PreparedStatement;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.OverridesAttribute;
import org.tuwien.swalab2.controller.JsfHelper;
import org.tuwien.swalab2.dataaccess.PersonStorage;
import org.tuwien.swalab2.swazam.util.model.entities.Account;
import org.tuwien.swalab2.swazam.util.model.entities.Person;
import org.tuwien.swalab2.swazam.util.model.entities.SwazamAccount;

/**
 *
 * @author gh
 */
public class PersonFacade extends AbstractFacade<Person> {
   
    
    

    
    @Override
    public void create(Person person) {
    
        System.out.println("Persisting entity:" + person.toString());
        SwazamAccount newAccount = new SwazamAccount();
        //getEntityManager().persist(person);
        
        //getEntityManager().persist(newAccount);
        
        newAccount.setBalance(10);
        person.setAccount(newAccount);
        
        
        
        
        //getEntityManager().persist(person);
        
        PersonStorage.getInstance().addPerson(person);
     

    }
    
    public PersonFacade() {
        super(Person.class);
    }
    
    public Person authenticatePerson(String userName,String passWord){
       
        return PersonStorage.getInstance().authenticatePerson(userName, passWord);        
    }
    
    @Override
    public Person find(Object id) {
        return PersonStorage.getInstance().getPerson(String.valueOf(id));
        
    }

    @Override
    protected EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
