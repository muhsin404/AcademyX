package com.academyx.timetable.dto;

import java.time.LocalTime;

import com.academyx.timetable.model.TimetableEntry;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimetableEntryDTO {
    private int period;
    private LocalTime startTime;
    private LocalTime endTime;
    private String subjectName;
    private String staffName;
    private Long batchId;
    private String batchName;

    

	public static TimetableEntryDTO fromBatchView(TimetableEntry entry) {
        TimetableEntryDTO dto = new TimetableEntryDTO();
        dto.period = entry.getPeriods().getPeriodNumber();
        dto.startTime = entry.getPeriods().getStartTime();
        dto.endTime = entry.getPeriods().getEndTime();
        dto.subjectName = entry.getSubject().getSubjectName();
        dto.staffName = entry.getStaff().getUserPersonalDetails().getFullName();
        return dto;
    }
    
    public static TimetableEntryDTO fromStaffView(TimetableEntry entry) {
        TimetableEntryDTO dto = new TimetableEntryDTO();
        dto.period = entry.getPeriods().getPeriodNumber();
        dto.startTime = entry.getPeriods().getStartTime();
        dto.endTime = entry.getPeriods().getEndTime();
        dto.subjectName = entry.getSubject().getSubjectName();
        dto.batchId = entry.getBatch().getBatchId();
        dto.batchName = entry.getBatch().getBatchName();
        return dto;
    }

    public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
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

	

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	

	public int getPeriod() {
		return period;
	}



	public void setPeriod(int period) {
		this.period = period;
	}



	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
    
    
}
