package com.academyx.course.model;

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
public class CourseDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseId;
	
	@Column
	private String courseName;
	
	@Column
	private String courseDescription;
	
	@Column
	private String duration;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    private Organizations organization;
	
	public Organizations getOrganization() {
		return organization;
	}

	public void setOrganization(Organizations organization) {
		this.organization = organization;
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

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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



	@CreationTimestamp
	@Column( name ="created_time", updatable = false)
	private LocalDateTime createdTime;
	
	@UpdateTimestamp
	@Column( name ="updated_time", updatable = true)
	private LocalDateTime updatedTime;

}
