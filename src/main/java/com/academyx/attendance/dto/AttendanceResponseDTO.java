package com.academyx.attendance.dto;

import java.time.LocalDate;
import java.util.List;

public class AttendanceResponseDTO {
	
	 private Long sessionId;
	    private LocalDate sessionDate;
	    private Long batchId;
	    private Long timetableEntryId;
	    private List<StudentsDTO> records;
	    
	 // Getters and setters
	    
		public Long getSessionId() {
			return sessionId;
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
		public List<StudentsDTO> getRecords() {
			return records;
		}
		public void setRecords(List<StudentsDTO> records) {
			this.records = records;
		}

	    
	    
	    

}
