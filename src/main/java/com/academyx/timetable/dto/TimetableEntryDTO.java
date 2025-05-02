package com.academyx.timetable.dto;

import java.time.LocalTime;

import com.academyx.timetable.model.TimetableEntry;

public class TimetableEntryDTO {
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long subjectId;
    private String subjectName;
    private Long staffId;
    private String staffName;

    public TimetableEntryDTO(TimetableEntry timetable) {
        this.dayOfWeek   = timetable.getDayOfWeek();
        this.startTime   = timetable.getStartTime();
        this.endTime     = timetable.getEndTime();
        this.subjectId   = timetable.getSubject().getSubjectId();
        this.subjectName = timetable.getSubject().getSubjectName();
        this.staffId     = timetable.getStaff().getuserId();
        this.staffName   = timetable.getStaff().getUserPersonalDetails().getFullName();
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

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
    
    
}
