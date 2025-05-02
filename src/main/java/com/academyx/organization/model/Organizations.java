package com.academyx.organization.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.academyx.batch.model.BatchDetails;
import com.academyx.course.model.CourseDetails;
import com.academyx.department.model.DepartmentDetails;
import com.academyx.subject.model.SubjectDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Organizations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long organizationId;
	@Column
	private String organizationName;
	@Column
	private String website;
	@Column
	private String organizationEmail;

	@Column
	private String industry;
	
	@Column
	private String organizationType;
	
	@Column
	private String tagline;

	@CreationTimestamp
	@Column(name = "created_time", updatable = false)
	private LocalDateTime createdTime;
	
	@OneToMany(
		      mappedBy = "organization",
		      cascade   = CascadeType.ALL,
		      fetch     = FetchType.LAZY
		    )
		    private List<DepartmentDetails> departments;
	
	@OneToMany(
		      mappedBy = "organization",
		      cascade   = CascadeType.ALL,
		      fetch     = FetchType.LAZY
		    )
		    private List<CourseDetails> courses;
	
	@OneToMany(
		      mappedBy = "organization",
		      cascade   = CascadeType.ALL,
		      fetch     = FetchType.LAZY
		    )
		    private List<SubjectDetails> subjects;
	
	@OneToMany(
		      mappedBy = "organization",
		      cascade   = CascadeType.ALL,
		      fetch     = FetchType.LAZY
		    )
		    private List<BatchDetails> batches;

	public List<CourseDetails> getCourses() {
		return courses;
	}

	public void setCourses(List<CourseDetails> courses) {
		this.courses = courses;
	}

	public List<SubjectDetails> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectDetails> subjects) {
		this.subjects = subjects;
	}

	public List<BatchDetails> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchDetails> batches) {
		this.batches = batches;
	}

	public String getOrganizationEmail() {
		return organizationEmail;
	}

	public void setOrganizationEmail(String organizationEmail) {
		this.organizationEmail = organizationEmail;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<DepartmentDetails> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentDetails> departments) {
		this.departments = departments;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

}
