package com.academyx.organization.model;

import com.academyx.user.model.UserCredentials;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_organization")
public class UserOrganizationRelation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long relationId;

	@ManyToOne(fetch = FetchType.LAZY) // many user can have one company
	@JoinColumn(name = "user_id", nullable = false)
	private UserCredentials user;

	@ManyToOne(fetch = FetchType.LAZY) // one user can have multiple company
	@JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
	private Organizations organization;

	@Column
	private int role; // 1- org Admin 2- orgSubAdmin

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public UserCredentials getUser() {
		return user;
	}

	public void setUser(UserCredentials user) {
		this.user = user;
	}

	public Organizations getOrganization() {
		return organization;
	}

	public void setOrganization(Organizations organizations) {
		this.organization = organizations;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

}
