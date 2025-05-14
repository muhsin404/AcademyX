package com.academyx.timetable.dto;

import java.util.List;

public class TimetableCreateRequest {
    private Long batchId;
    private String dayOfWeek;
    private List<TimetablePeriodDTO> periods;

    // Getters and Setters
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

    public List<TimetablePeriodDTO> getPeriods() {
        return periods;
    }

    public void setPeriods(List<TimetablePeriodDTO> periods) {
        this.periods = periods;
    }
}
