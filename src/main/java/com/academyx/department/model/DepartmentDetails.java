package com.academyx.department.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.academyx.organization.model.Organizations;
import com.academyx.user.model.UserCredentials;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DepartmentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long departmentId;
	
	@Column
	private String departmentName;
	
	@Column
	private String departmentDescription;
	
	@Column
	private int status;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    private Organizations organization;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Organizations getOrganization() {
		return organization;
	}

	public void setOrganization(Organizations organization) {
		this.organization = organization;
	}

	public UserCredentials getCreatedBy() {
		return createdBy;
	}

	public UserCredentials getUpdatedBy() {
		return updatedBy;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}


	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	@ManyToOne
	@JoinColumn(name="createdBy")
	private UserCredentials createdBy;
	
	@ManyToOne
	@JoinColumn(name="updatedBy")
	private UserCredentials updatedBy;
	
	
	public void setCreatedBy(UserCredentials createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedBy(UserCredentials updatedBy) {
		this.updatedBy = updatedBy;
	}

	@CreationTimestamp
	@Column( name ="created_time", updatable = false)
	private LocalDateTime createdTime;
	
	@UpdateTimestamp
	@Column(name ="updated_time", updatable = true)
	private LocalDateTime updatedTime;

}
