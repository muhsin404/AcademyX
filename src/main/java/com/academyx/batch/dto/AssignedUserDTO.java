package com.academyx.batch.dto;

public class AssignedUserDTO {

	private Long userId;
    private String assignmentStatus;   // "alreadyAssigned" or "newlyAssigned"

    public AssignedUserDTO(Long userId, String assignmentStatus) {
        this.userId = userId;
        this.assignmentStatus = assignmentStatus;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}
}
