/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.services;

import java.util.Collection;
import javax.inject.Inject;
import org.tuwien.swalab2.ejb.AccountFacade;
import org.tuwien.swalab2.interfaces.IAccountService;
import org.tuwien.swalab2.swazam.util.model.entities.SwaZamTransaction;

/**
 *
 * @author gh
 */
public class AccountService implements IAccountService{
  @Inject
  private AccountFacade accountFacade;

    public AccountService() {
    }

    @Override
    public int getAccountBalanceForUser(String userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<SwaZamTransaction> getSearchHistoryForUser(String userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
  
  
    
}
