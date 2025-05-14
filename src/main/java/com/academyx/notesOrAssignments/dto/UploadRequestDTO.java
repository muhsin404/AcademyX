package com.academyx.notesOrAssignments.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;



public class UploadRequestDTO {
    private String title;
    private String description;
    private int uploadType;
    private Long subjectId;
    private Long batchId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private boolean submissionAllowed;
    private List<MultipartFile> files;
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
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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
	public List<MultipartFile> getFiles() {
		return files;
	}
	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
    
    
}
