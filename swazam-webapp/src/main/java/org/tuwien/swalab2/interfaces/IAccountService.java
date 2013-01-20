/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.interfaces;

import java.util.Collection;
import org.tuwien.swalab2.swazam.util.model.entities.Person;
import org.tuwien.swalab2.swazam.util.model.entities.SwaZamTransaction;

/**
 *
 * @author gh
 */
public interface IAccountService {
    
    public Person login(String userId);
    public int getAccountBalanceForUser(String userId);
    public Collection<SwaZamTransaction> getSearchHistoryForUser(String userId);
}
