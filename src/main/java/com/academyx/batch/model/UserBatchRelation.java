package com.academyx.batch.model;

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
public class UserBatchRelation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userBatchRelationId;
	

    @ManyToOne
    @JoinColumn(name = "batchId", referencedColumnName = "batchId")
    private BatchDetails batchDetails;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserCredentials userCredentials;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    private Organizations organization;
	
	public Organizations getOrganization() {
		return organization;
	}

	public void setOrganization(Organizations organization) {
		this.organization = organization;
	}
    
    @Column
    private int status;
    
    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUserBatchRelationId() {
		return userBatchRelationId;
	}

	public void setUserBatchRelationId(Long userBatchRelationId) {
		this.userBatchRelationId = userBatchRelationId;
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
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

	@UpdateTimestamp
	@Column(name = "updated_time")
	private LocalDateTime updatedTime;


	public BatchDetails getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(BatchDetails batchDetails) {
		this.batchDetails = batchDetails;
	}

	public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}
	
	
	
}