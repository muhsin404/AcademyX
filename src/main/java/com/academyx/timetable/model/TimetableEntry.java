package com.academyx.timetable.model;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="periodId", referencedColumnName="periodId")
    private Periods period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subjectId", referencedColumnName="subjectId")
    private SubjectDetails subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="staffId", referencedColumnName="userId")
    private UserCredentials staff;

    @Column(nullable = false)
    private int status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="createdBy", referencedColumnName="userId")
    private UserCredentials createdBy;

	public Long getTimetableEntryId() {
		return timetableEntryId;
	}

	public void setTimetableEntryId(Long timetableEntryId) {
		this.timetableEntryId = timetableEntryId;
	}

	public BatchDetails getBatch() {
		return batch;
	}

	public Periods getPeriod() {
		return period;
	}

	public void setPeriod(Periods period) {
		this.period = period;
	}

	public UserCredentials getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserCredentials createdBy) {
		this.createdBy = createdBy;
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

	public Periods getPeriods() {
		return period;
	}

	public void setPeriods(Periods period) {
		this.period = period;
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
