/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.tuwien.swalab2.swazam.util.model.entities.Account;

/**
 *
 * @author gh
 */
public class AccountFacade extends AbstractFacade<Account> {
    @PersistenceContext(unitName = "myPu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
    
}
