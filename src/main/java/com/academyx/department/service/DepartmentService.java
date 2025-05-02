package com.academyx.department.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Map<String, Object> createDepartmentDetails(UserCredentials user ,  HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		
		try {
			Long orgId = Long.parseLong(data.get("organizationId").toString());
			Organizations organization = organizationRepository.getOrganizationByOrganizationId(orgId);
			if (organization == null) {
				return utils.createErrorResponse("Invalid organization ID");
			}
			
		String departmentName=data.get("departmentName").toString().trim();
		
		boolean isDepartmentExist = departmentDetailsRepository.findDepartmentExistOrNot(departmentName);
		if(isDepartmentExist)
		{
			return utils.createErrorResponse("department already exist");
		}
		else
		{
			DepartmentDetails department=new DepartmentDetails();
			department.setDepartmentName(departmentName);
			department.setDepartmentDescription(data.get("departmentDescription").toString());
			department.setCreatedBy(user);
			department.setOrganization(organization);
			department.setStatus(1);//active status
			departmentDetailsRepository.save(department);
			
			return utils.createSuccessResponse("successfully created department");
			
		}
		}catch (Exception e) {
			response.put("status", "Error" + e);
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
