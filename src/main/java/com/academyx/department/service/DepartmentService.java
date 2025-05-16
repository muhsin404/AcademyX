package com.academyx.department.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.academyx.common.util.Utils;
import com.academyx.department.dto.DepartmentDTO;
import com.academyx.department.model.DepartmentDetails;
import com.academyx.department.repository.DepartmentDetailsRepository;
import com.academyx.organization.model.Organizations;
import com.academyx.organization.repository.OrganizationRepository;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.user.model.UserCredentials;

@Service
public class DepartmentService {
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private DepartmentDetailsRepository departmentDetailsRepository;

	// METHOD
	public Map<String, Object> createOrUpdateDepartmentDetails(UserCredentials user, HashMap<String, Object> data) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        Long orgId = Long.parseLong(data.get("organizationId").toString());
	        Organizations organization = organizationRepository.getOrganizationByOrganizationId(orgId);
	        if (organization == null) {
	            return utils.createErrorResponse("Invalid organization ID");
	        }

	        String departmentName = data.get("departmentName").toString().trim();
	        String departmentDescription = data.get("departmentDescription") != null ? data.get("departmentDescription").toString() : "";

	        //  Check if it's an update or a create request
	        if (data.containsKey("departmentId") && data.get("departmentId") != null && !data.get("departmentId").toString().isEmpty()) {
	            
	        	
	        	//  UPDATE DEPARTMENT LOGIC
	            Long departmentId = Long.parseLong(data.get("departmentId").toString());
	            DepartmentDetails existingOpt = departmentDetailsRepository.getDepartmentById(departmentId);

	            if (existingOpt==null) {
	                return utils.createErrorResponse("Department not found with provided ID");
	            }

	            DepartmentDetails department = existingOpt;

	            //  Check if the updated name is different and already taken
	            if (!department.getDepartmentName().equalsIgnoreCase(departmentName)) {
	                boolean isNameExists = departmentDetailsRepository.findDepartmentExistOrNot(departmentName);
	                if (isNameExists) {
	                    return utils.createErrorResponse("Another department with the same name already exists");
	                }
	            }

	            //  Perform update
	            department.setDepartmentName(departmentName);
	            department.setDepartmentDescription(departmentDescription);
	            department.setOrganization(organization);
	            department.setUpdatedBy(user); 
	            departmentDetailsRepository.save(department);

	            return utils.createSuccessResponse("Department updated successfully");
	        } else {
	            //  CREATE DEPARTMENT LOGIC
	            boolean isDepartmentExist = departmentDetailsRepository.findDepartmentExistOrNot(departmentName);
	            if (isDepartmentExist) {
	                return utils.createErrorResponse("Department already exists");
	            }

	            DepartmentDetails department = new DepartmentDetails();
	            department.setDepartmentName(departmentName);
	            department.setDepartmentDescription(departmentDescription);
	            department.setCreatedBy(user);
	            department.setOrganization(organization);
	            department.setStatus(1); // active
	            departmentDetailsRepository.save(department);

	            return utils.createSuccessResponse("Successfully created department");
	        }

	    } catch (Exception e) {
	        response.put("status", "Error");
	        response.put("message", e.getMessage());
	        return response;
	    }
	}

	
	public Map<String, Object> getAllDepartments() {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	 List<DepartmentDTO> activeDepartments = departmentDetailsRepository.findAllDepartmentDTOs();
	        response.put("status", "Success");
	        response.put("departments", activeDepartments);
	        return response;
	    } catch (Exception e) {
	        return utils.createErrorResponse("Failed to fetch departments: " + e.getMessage());
	    }
	}
	
	public Map<String, Object> deleteDepartment(Long departmentId) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DepartmentDetails department = departmentDetailsRepository.getDepartmentById(departmentId);

	        if (department == null) {
	            return utils.createErrorResponse("Department not found or already deleted");
	        }

	        department.setStatus(0); // soft delete
	        departmentDetailsRepository.save(department);

	        return utils.createSuccessResponse("Department deleted successfully");
	    } catch (Exception e) {
	        response.put("status", "Error" + e);
	        return response;
	    }
	}

}
