package com.academyx.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Invitations {

	@Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private UUID id;

	  @Column(nullable = false)
	  private String email;

	  @Column(nullable = false)
	  private int role;

	  @Column(nullable = false)
	  private String status;   // e.g. "SENT", "ACCEPTED", "EXPIRED"

	  @Column(nullable = false, unique = true)
	  private String token;    // secure random token

	  @CreationTimestamp
	  private LocalDateTime createdAt;

	  @Column(nullable = false)
	  private LocalDateTime expiresAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	  // getters and setters 
	  
	  
	
}
