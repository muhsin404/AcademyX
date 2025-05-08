package com.academyx.attendance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.academyx.batch.model.BatchDetails;
import com.academyx.timetable.model.TimetableEntry;
import com.academyx.user.model.UserCredentials;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class AttendanceSession {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batchId")
    private BatchDetails batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetableEntryId")
    private TimetableEntry timetableEntry;

    private LocalDate sessionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markedBy")
    private UserCredentials markedBy;

    private LocalDateTime markedAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<AttendanceRecord> attendanceRecords;

    
	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public BatchDetails getBatch() {
		return batch;
	}

	public void setBatch(BatchDetails batch) {
		this.batch = batch;
	}

	public TimetableEntry getTimetableEntry() {
		return timetableEntry;
	}

	public void setTimetableEntry(TimetableEntry timetableEntry) {
		this.timetableEntry = timetableEntry;
	}

	public LocalDate getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(LocalDate sessionDate) {
		this.sessionDate = sessionDate;
	}

	public UserCredentials getMarkedBy() {
		return markedBy;
	}

	public void setMarkedBy(UserCredentials markedBy) {
		this.markedBy = markedBy;
	}

	public LocalDateTime getMarkedAt() {
		return markedAt;
	}

	public void setMarkedAt(LocalDateTime markedAt) {
		this.markedAt = markedAt;
	}

	public List<AttendanceRecord> getAttendanceRecords() {
		return attendanceRecords;
	}

	public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
		this.attendanceRecords = attendanceRecords;
	}

	
}
