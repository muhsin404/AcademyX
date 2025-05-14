package com.academyx.attendance.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class StudentAttendanceDTO {
	
	private Long sessionId;
    private LocalDate sessionDate;
    private String status;
    private String subjectName;
    private LocalTime startTime;
    private LocalTime endTime;
    private int period;

    // Constructor
    public StudentAttendanceDTO(Long sessionId, LocalDate sessionDate, String status,
                                String subjectName, LocalTime startTime, LocalTime endTime,int period) {
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.status = status;
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.period =period;
    }
    
    // getters and setters

    
    
	public Long getSessionId() {
		return sessionId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public LocalDate getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(LocalDate sessionDate) {
		this.sessionDate = sessionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
    
    
    

}
