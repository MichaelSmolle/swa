/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.ejb;

import java.sql.PreparedStatement;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.tuwien.swalab2.swazam.util.model.entities.Account;
import org.tuwien.swalab2.swazam.util.model.entities.Person;
import org.tuwien.swalab2.swazam.util.model.entities.SwazamAccount;

/**
 *
 * @author gh
 */
@Stateless
public class PersonFacade extends AbstractFacade<Person> {
    
    @Inject
    AccountFacade accountFacade;
    
    
    @PersistenceContext(unitName= "myPu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(Person person) {
    
        System.out.println("Persisting entity:" + person.toString());
        SwazamAccount newAccount = new SwazamAccount();
        
        getEntityManager().persist(person);
        
        getEntityManager().persist(newAccount);
        
        person.setAccount(newAccount);
        
        
        getEntityManager().persist(person);
        
    }
    
    public PersonFacade() {
        super(Person.class);
    }
    
    public Person authenticatePerson(String userName,String passWord){
        PreparedStatement pStmt;
        List<Person> pList = em.createQuery("select p from Person p where p.userName =:user and p.passWord =:pass ").setParameter("user", userName).setParameter("pass",passWord).getResultList();
        
        return pList.get(0);
    }
    
}
