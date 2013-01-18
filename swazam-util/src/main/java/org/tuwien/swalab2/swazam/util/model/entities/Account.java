/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tuwien.swalab2.swazam.util.model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gh
 */
@Entity
@XmlRootElement
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    @Id
    @GeneratedValue
    private Long id;  
    
    @ManyToOne
    private List<SwaZamTransaction> performedSearches;
        
    private int coinBalance = 0;

    public Account() {
    }
    
    
    

    public List<SwaZamTransaction> getPerformedSearches() {
        return performedSearches;
    }

    public void setPerformedSearches(List<SwaZamTransaction> performedSearches) {
        this.performedSearches = performedSearches;
    } 


    public int getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(int coinBalance) {
        this.coinBalance = coinBalance;
    }
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tuwien.swalab2.model.Account[ id=" + id + " ]";
    }
    
}
