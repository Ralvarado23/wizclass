package com.wizclass.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author Raul Alvarado
 *
 */
@Entity
public class Ensenanza {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ensenanza;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEnsenanza() {
		return ensenanza;
	}
	
	public void setEnsenanza(String ensenanza) {
		this.ensenanza = ensenanza;
	}
}