package com.academyx.attendance.dto;

import java.time.LocalDate;
import java.util.List;

public class AttendanceRequestDTO {

	private Long batchId;
    private Long timetableEntryId;
    private Long markedBy;
    private LocalDate sessionDate;
    public LocalDate getSessionDate() {
		return sessionDate;
	}
	public void setSessionDate(LocalDate sessionDate) {
		this.sessionDate = sessionDate;
	}
	private List<StudentsDTO> studentsList;
    
    
    // Getters and setters
    
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public Long getTimetableEntryId() {
		return timetableEntryId;
	}
	public void setTimetableEntryId(Long timetableEntryId) {
		this.timetableEntryId = timetableEntryId;
	}
	public Long getMarkedBy() {
		return markedBy;
	}
	public void setMarkedBy(Long markedBy) {
		this.markedBy = markedBy;
	}
	public List<StudentsDTO> getStudentsList() {
		return studentsList;
	}
	public void setStudentsList(List<StudentsDTO> studentsList) {
		this.studentsList = studentsList;
	}
    
}
