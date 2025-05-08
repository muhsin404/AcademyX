package com.academyx.attendance.dto;

public class StudentsDTO {
	
	private Long studentId;
    private String status; // PRESENT, ABSENT, LEAVE
    
    //getters and setters
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
