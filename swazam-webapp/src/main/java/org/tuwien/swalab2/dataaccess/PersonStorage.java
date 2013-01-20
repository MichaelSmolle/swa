/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.dataaccess;

import java.io.Serializable;
import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Singleton;
import org.tuwien.swalab2.swazam.util.model.entities.CryptoUtils;
import org.tuwien.swalab2.swazam.util.model.entities.Person;

/**
 *
 * @author gh
 */

public final class PersonStorage implements Serializable{
    
    private static PersonStorage personStorage;
    private HashMap<String,Person> persons = new HashMap<String,Person>();
    
    private PersonStorage(){
       
    }
    
    public synchronized static PersonStorage getInstance() 
    {
        if (personStorage == null) 
        {
            personStorage = new PersonStorage();
        }
        return personStorage;
    }
    
    public void addPerson(Person p){
       p.setId(Long.valueOf(persons.size()+1));
     try{
        p.setPassWord(CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(p.getPassWord())));
     }catch(Exception e) {}
       persons.put(String.valueOf(p.getId()), p);
    }
    
    public Person getPerson(String key){
        if(persons.containsKey(key)){
            return persons.get(key);
        }
        return null;
    }
    
    public Person authenticatePerson(String userName,String passWord){
        for(Person p :persons.values()){
            if(p.getUserName().equals(userName)){               
               String newHash="";
                try{
                newHash = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(passWord));
               }catch(Exception e){
                    
                   return null;
               }
                   if(newHash.equals(p.getPassWord())){
                    return p;                
                   }
            }
        }
        
        return null;
    }
    
}
