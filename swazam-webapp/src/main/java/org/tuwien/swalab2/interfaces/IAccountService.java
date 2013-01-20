/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.interfaces;

import java.util.Collection;
import javax.ws.rs.PathParam;
import org.tuwien.swalab2.swazam.util.model.entities.Person;
import org.tuwien.swalab2.swazam.util.model.entities.SwaZamTransaction;

/**
 *
 * @author gh
 */
public interface IAccountService {

    /**
     * Login with the users id
     * @param userId user id you can find it in account management when you are logging in
     * @return Person object
     */
    public Person login(String userId);
    
    /**
     * Login with the users username and password
     * @param password password
     * @param userName Username
     * @return String users id
     */
    public String login(String userName,String passWord );

    /**
     * Get current account balance for user
     * @param userId userId
     * @return String account balance
     */
    public String getAccountBalanceForUser(String userId);



    /**
     *
     * @param search   The search term
     * @param success  0 | 1 to specify success or failure this is important otherwise i can't update.
     * @param userId   userId
     * @return String OK or Failed
     */
    public String updateAccountAfterSearch(String search,String success,String userId);

    /**
     * Update users account with +1 to his balance ( eg after serving an succesfull request. )
     * @param userId  String userId
     * @return  String OK or failed
     */
    public String updateAccountBalance(String userId) ;
}
