package com.academyx.timetable.dto;

import java.util.List;

public class TimetableDayRequest {

	 private Long batchId;
     private String dayOfWeek;
     private List<PeriodDTO> periods;
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public List<PeriodDTO> getPeriods() {
		return periods;
	}
	public void setPeriods(List<PeriodDTO> periods) {
		this.periods = periods;
	}
     
     
}
