package com.academyx.registration.model;


import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class UserCredentials {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@Column
	private int role;
	
	@Column
	private String userToken;
	
	@Column
	private int status;
	
	
	
	@OneToOne(mappedBy = "userCredentials")
	private UserPersonelDetails userPersonelDetails;
	
	public UserPersonelDetails getUserPersonelDetails() {
		return userPersonelDetails;
	}
	public void setUserPersonelDetails(UserPersonelDetails userPersonelDetails) {
		this.userPersonelDetails = userPersonelDetails;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
