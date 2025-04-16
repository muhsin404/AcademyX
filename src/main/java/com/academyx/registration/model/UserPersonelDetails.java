package com.academyx.registration.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class UserPersonelDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long personelId;
	
	@Column
	private String fullName;
	
	@Column 
	private String phoneNumber;
	
	@Column
	private String gender;
	
	@Column
	private String address;
	
	@Column
	private LocalDate dob;
	
	@Column
	private String profileImage;
	
	@OneToOne
	@JoinColumn(name="userId", referencedColumnName = "userId")
	private UserCredentials userCredentials;
	
	@CreationTimestamp
	@Column( name ="created_time", updatable = false)
	private LocalDateTime createdTime;

	public Long getPersonelId() {
		return personelId;
	}

	public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	public void setPersonelId(Long personelId) {
		this.personelId = personelId;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	
	
}
