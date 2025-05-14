package com.academyx.attendance.model;

import com.academyx.user.model.UserCredentials;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AttendanceRecord {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessionId")
    private AttendanceSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private UserCredentials student;

   
    private String status;  //present / absent / leave

    // Getters and setters

	public Long getRecordId() {
		return recordId;
	}


	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}


	public AttendanceSession getSession() {
		return session;
	}


	public void setSession(AttendanceSession session) {
		this.session = session;
	}


	public UserCredentials getStudent() {
		return student;
	}


	public void setStudent(UserCredentials student) {
		this.student = student;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

   
    
    
}