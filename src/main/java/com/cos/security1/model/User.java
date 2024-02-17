package com.cos.security1.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class User {
	
	public User() {}
	@Builder
	public User(String username, String password, String email, String role, String provider, String providerId) {
		this.username = username;
		this.password =  password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId; 
	}
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private String email;
	private String role;
	@CreationTimestamp
	private Timestamp createDate;
	
	private String provider; //구글 ,페이스북 등
	private String providerId; // oAuth sub값. pk
}
