package com.academyx.timetable.model;

import java.time.LocalTime;

import com.academyx.batch.model.BatchDetails;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.user.model.UserCredentials;

import jakarta.persistence.*;

@Entity
public class TimetableEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetableEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="batchId", referencedColumnName="batchId")
    private BatchDetails batch;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subjectId", referencedColumnName="subjectId")
    private SubjectDetails subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="staffId", referencedColumnName="userId")
    private UserCredentials staff;

    @Column(nullable = false)
    private int status;

	public Long getTimetableEntryId() {
		return timetableEntryId;
	}

	public void setTimetableEntryId(Long timetableEntryId) {
		this.timetableEntryId = timetableEntryId;
	}

	public BatchDetails getBatch() {
		return batch;
	}

	public void setBatch(BatchDetails batch) {
		this.batch = batch;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	public SubjectDetails getSubject() {
		return subject;
	}

	public void setSubject(SubjectDetails subject) {
		this.subject = subject;
	}

	public UserCredentials getStaff() {
		return staff;
	}

	public void setStaff(UserCredentials staff) {
		this.staff = staff;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    // getters and setters…
}
