package com.academyx.department.dto;


import com.academyx.department.model.DepartmentDetails;

public class DepartmentDTO {
	
	private Long departmentId;
	
    private String departmentName;
    private String departmentDescription;

    // Constructor
    public DepartmentDTO(DepartmentDetails department) {
    	this.departmentId=department.getDepartmentId();
    	this.departmentName=department.getDepartmentName();
    	this.departmentDescription=department.getDepartmentDescription();
    }

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}

}
