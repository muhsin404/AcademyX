package com.academyx.feedbackAndComplaints.dto;

import com.academyx.feedbackAndComplaints.model.FeedbackOrComplaints;

public class FeedbackOrComplaintsDTO {
	
	private Long feedbackOrComplaintId;
	private int type;
	private String subject;
	private String message;
	private String submittedBy;
	
	public FeedbackOrComplaintsDTO(FeedbackOrComplaints feedbackOrComplaints)
	{
		this.feedbackOrComplaintId=feedbackOrComplaints.getFeedbackOrComplaintId();
		this.type=feedbackOrComplaints.getType();
		this.subject=feedbackOrComplaints.getSubject();
		this.message=feedbackOrComplaints.getMessage();
		this.submittedBy=feedbackOrComplaints.getSubmittedBy().getEmail();
	}

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

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}
	
	

}
