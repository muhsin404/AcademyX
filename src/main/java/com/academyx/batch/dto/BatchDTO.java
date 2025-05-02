package com.academyx.batch.dto;

import java.time.LocalDate;

import com.academyx.batch.model.BatchDetails;

public class BatchDTO {
    private Long batchId;
    private String batchName;
    private String batchDescription;
    private LocalDate startingDate;
    private LocalDate endingDate;

    // Constructor
    public BatchDTO(BatchDetails batch) {
        this.batchId = batch.getBatchId();
        this.batchName = batch.getBatchName();
        this.batchDescription = batch.getBatchDescription();
        this.startingDate = batch.getStartingDate();
        this.endingDate = batch.getEndingDate();
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

	public String getBatchDescription() {
		return batchDescription;
	}

	public void setBatchDescription(String batchDescription) {
		this.batchDescription = batchDescription;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public LocalDate getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
	}

    
}

