package com.academyx.timetable.model;

import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
public class Periods {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long periodId;

	    @Column(nullable = false)
	    private LocalTime startTime;

	    @Column(nullable = false)
	    private LocalTime endTime;

	    public Long getPeriodId() {
			return periodId;
		}

		public void setPeriodId(Long periodId) {
			this.periodId = periodId;
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

		public int getPeriodNumber() {
			return periodNumber;
		}

		public void setPeriodNumber(int periodNumber) {
			this.periodNumber = periodNumber;
		}

		@Column(nullable = false)
	    private int periodNumber; 
}
