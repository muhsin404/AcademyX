package com.academyx.subject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.batch.model.BatchDetails;
import com.academyx.common.util.Utils;
import com.academyx.department.model.DepartmentDetails;
import com.academyx.department.repository.DepartmentDetailsRepository;
import com.academyx.organization.model.Organizations;
import com.academyx.organization.repository.OrganizationRepository;
import com.academyx.subject.dto.SubjectDTO;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.subject.repository.SubjectDetailsRepository;
import com.academyx.user.model.UserCredentials;

@Service
public class SubjectServices {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	private SubjectDetailsRepository subjectDetailsRepository;
	
	@Autowired
	private DepartmentDetailsRepository departmentDetailsRepository;

	public Map<String, Object> createSubjectDetails(UserCredentials user, HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		
		try {
			Long orgId = Long.parseLong(data.get("organizationId").toString());
			Organizations organization = organizationRepository.getOrganizationByOrganizationId(orgId);
			if (organization == null) {
				return utils.createErrorResponse("Invalid organization ID");
			}
			
			Long departmentId = Long.parseLong(data.get("departmentId").toString());
			DepartmentDetails department = departmentDetailsRepository.getDepartmentById(departmentId);
			if (department == null) {
			    return utils.createErrorResponse("Invalid department ID");
			}

		String subjectName=data.get("subjectName").toString().trim();
		
		boolean isSubjectExist = subjectDetailsRepository.findSubjectExistOrNot(subjectName);
		if(isSubjectExist)
		{
			return utils.createErrorResponse("subject already exist");
		}
		else
		{
			SubjectDetails subject=new SubjectDetails();
			subject.setSubjectName(subjectName);
			subject.setSubjectDescription(data.get("subjectDescription").toString());
			subject.setCreatedBy(user);
			subject.setStatus(1);//active status(1)
			subject.setOrganization(organization);
			subject.setDepartment(department); // <-- set department
			subjectDetailsRepository.save(subject);
			return utils.createSuccessResponse("successfully created subject");
			
		}
		}catch (Exception e) {
			response.put("status", "Error" + e);
			return response;
		}
		
	}
	
	public Map<String, Object> getAllSubjects() {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	 List<SubjectDTO> activeBatches = subjectDetailsRepository.findAllSubjectDTOs();
	        response.put("status", "Success");
	        response.put("subjects", activeBatches);
	        return response;
	    } catch (Exception e) {
	        return utils.createErrorResponse("Failed to fetch subjects: " + e.getMessage());
	    }
	}
	
	
	public Map<String, Object> deleteSubject(Long subjectId) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        SubjectDetails subject = subjectDetailsRepository.getSubjectById(subjectId);

	        if (subject == null) {
	            return utils.createErrorResponse("Subject not found or already deleted");
	        }

	        subject.setStatus(0); // soft delete
	        subjectDetailsRepository.save(subject);

	        return utils.createSuccessResponse("Subject deleted successfully");
	    } catch (Exception e) {
	        response.put("status", "Error" + e);
	        return response;
	    }
	}


}
