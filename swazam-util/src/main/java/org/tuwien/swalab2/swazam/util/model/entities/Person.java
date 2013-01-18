package org.tuwien.swalab2.swazam.util.model.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    
    private Long id;
    
    private String firstName;
    
    private String lastName;
    
    private String userName;
    
    private String passWord;   
    
    private SwazamAccount account;
    

    public Person() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    } 

    public SwazamAccount getAccount() {
        return account;
    }

    public void setAccount(SwazamAccount account) {
        this.account = account;
    }
    
    
    
    
    
}