package com.academyx.feedbackAndComplaints.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.academyx.user.model.UserCredentials;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FeedbackOrComplaints {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackOrComplaintId;

    private int type; // 1-feedback, 2- complaints

    private String subject;

    private String message;

	@ManyToOne
	@JoinColumn(name="submittedBy")
	private UserCredentials submittedBy;

    public Long getFeedbackOrComplaintId() {
		return feedbackOrComplaintId;
	}

	public void setFeedbackOrComplaintId(Long feedbackOrComplaintId) {
		this.feedbackOrComplaintId = feedbackOrComplaintId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserCredentials getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(UserCredentials submittedBy) {
		this.submittedBy = submittedBy;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	@CreationTimestamp
	@Column( name ="created_time", updatable = false)
	private LocalDateTime createdTime;
}
