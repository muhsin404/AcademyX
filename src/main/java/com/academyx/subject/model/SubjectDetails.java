package com.academyx.subject.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.academyx.department.model.DepartmentDetails;
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
public class SubjectDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subjectId;
	
	@Column
	private String subjectName;
	
	@Column
	private String subjectDescription;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    private Organizations organization;
	
	public Organizations getOrganization() {
		return organization;
	}

	public void setOrganization(Organizations organization) {
		this.organization = organization;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "departmentId", referencedColumnName = "departmentId")
	private DepartmentDetails department;

	public DepartmentDetails getDepartment() {
	    return department;
	}

	public void setDepartment(DepartmentDetails department) {
	    this.department = department;
	}


	@ManyToOne
	@JoinColumn(name="createdBy")
	private UserCredentials createdBy;
	
	@ManyToOne
	@JoinColumn(name="updatedBy")
	private UserCredentials updatedBy;
	
	@Column
	private int status;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public UserCredentials getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserCredentials createdBy) {
		this.createdBy = createdBy;
	}

	public UserCredentials getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserCredentials updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectDescription() {
		return subjectDescription;
	}

	public void setSubjectDescription(String subjectDescription) {
		this.subjectDescription = subjectDescription;
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

	
	
	@CreationTimestamp
	@Column( name ="created_time", updatable = false)
	private LocalDateTime createdTime;
	
	@UpdateTimestamp
	@Column( name ="updated_time", updatable = true)
	private LocalDateTime updatedTime;

}
