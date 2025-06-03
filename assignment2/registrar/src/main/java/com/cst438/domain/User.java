package com.cst438.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user_table")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	//TODO create fields for name, email, password, type
	// get/set methods

}
