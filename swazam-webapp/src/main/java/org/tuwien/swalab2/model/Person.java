package org.tuwien.swalab2.model;
 
import java.io.Serializable;
import java.util.Date;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity 
public class Person implements Serializable{

	private static final long	serialVersionUID	= 1L;
 
	@Id
	@GeneratedValue
	private Long				id;
	
	public Person(){}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getId(){
		return id;
	}
	

}