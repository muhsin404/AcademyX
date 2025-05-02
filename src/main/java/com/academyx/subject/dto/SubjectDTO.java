package com.academyx.subject.dto;


import com.academyx.subject.model.SubjectDetails;

public class SubjectDTO {
	private Long subjectId;
    private String subjectName;
    private String subjectDescription;
    private String departmentName;

    // Constructor
    public SubjectDTO(SubjectDetails subject) {
        this.subjectId = subject.getSubjectId();
        this.subjectName = subject.getSubjectName();
        this.subjectDescription = subject.getSubjectDescription();
        this.departmentName =subject.getDepartment()!=null?subject.getDepartment().getDepartmentName():null;
    }

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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

	public String getSubjectDescription() {
		return subjectDescription;
	}

	public void setSubjectDescription(String subjectDescription) {
		this.subjectDescription = subjectDescription;
	}

}
