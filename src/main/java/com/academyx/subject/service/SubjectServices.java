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

	public Map<String, Object> createOrUpdateSubject(UserCredentials user, HashMap<String, Object> data) {
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

	        String subjectName = data.get("subjectName").toString().trim();
	        String subjectDescription = data.getOrDefault("subjectDescription", "").toString();

	        // ✨ Check if it's update or create
	        if (data.containsKey("subjectId") && data.get("subjectId") != null) {
	        	
	            Long subjectId = Long.parseLong(data.get("subjectId").toString());
	            SubjectDetails subjectExist = subjectDetailsRepository.getSubjectById(subjectId);
	            
	            if (subjectExist==null) {
	                return utils.createErrorResponse("Subject not found with provided ID");
	            }

	            SubjectDetails subject = subjectExist;

	            //  Check if the updated name is different and already taken
	            if (!subject.getSubjectName().equalsIgnoreCase(subjectName)) {
	                boolean isNameExists = subjectDetailsRepository.findSubjectExistOrNot(subjectName);
	                if (isNameExists) {
	                    return utils.createErrorResponse("Another subject with the same name already exists");
	                }
	            }
	            	//set updated data in the table
	                subject.setSubjectName(subjectName);
	                subject.setSubjectDescription(subjectDescription);
	                subject.setOrganization(organization);
	                subject.setDepartment(department);
	                subject.setUpdatedBy(user);
	                subjectDetailsRepository.save(subject);
	                return utils.createSuccessResponse("Successfully updated subject");
	      
	        } else {
	            // ✨ Check if subject with same name already exists
	            boolean isSubjectExist = subjectDetailsRepository.findSubjectExistOrNot(subjectName);
	            if (isSubjectExist) {
	                return utils.createErrorResponse("Subject already exists");
	            }

	            SubjectDetails subject = new SubjectDetails();
	            subject.setSubjectName(subjectName);
	            subject.setSubjectDescription(subjectDescription);
	            subject.setCreatedBy(user);
	            subject.setStatus(1);
	            subject.setOrganization(organization);
	            subject.setDepartment(department);
	            subjectDetailsRepository.save(subject);
	            return utils.createSuccessResponse("Successfully created subject");
	        }

	    } catch (Exception e) {
	        response.put("status", "Error");
	        response.put("message", e.getMessage());
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
