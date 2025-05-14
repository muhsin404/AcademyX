package com.academyx.notesOrAssignments.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.academyx.batch.model.BatchDetails;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.user.model.UserCredentials;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class UploadMaterials {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int uploadType;  // 1- notes  and 2- assignments 

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uploadedBy", referencedColumnName = "userId")
	private UserCredentials staff;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectId", referencedColumnName = "subjectId")
	private SubjectDetails subject;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batchId", referencedColumnName = "batchId")
	private BatchDetails batch;

    private String filePath;

    private LocalDateTime uploadedAt;

    private LocalDate dueDate; // Only for assignments

    private boolean submissionAllowed; // Only for assignments

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUploadType() {
		return uploadType;
	}

	public void setUploadType(int uploadType) {
		this.uploadType = uploadType;
	}

	public UserCredentials getStaff() {
		return staff;
	}

	public void setStaff(UserCredentials staff) {
		this.staff = staff;
	}

	public SubjectDetails getSubject() {
		return subject;
	}

	public void setSubject(SubjectDetails subject) {
		this.subject = subject;
	}

	public BatchDetails getBatch() {
		return batch;
	}

	public void setBatch(BatchDetails batch) {
		this.batch = batch;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isSubmissionAllowed() {
		return submissionAllowed;
	}

	public void setSubmissionAllowed(boolean submissionAllowed) {
		this.submissionAllowed = submissionAllowed;
	}

    
}
