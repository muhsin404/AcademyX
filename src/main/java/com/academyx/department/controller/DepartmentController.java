package com.academyx.department.controller;

import org.springframework.web.bind.annotation.RestController;

import com.academyx.common.util.Utils;
import com.academyx.department.service.DepartmentService;
import com.academyx.user.model.UserCredentials;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class DepartmentController {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private DepartmentService departmentService;

	// ✅ [MODIFIED METHOD NAME TO: createOrUpdateDepartment]
	@PostMapping("/createDepartment")
	public ResponseEntity<Map<String, Object>> createOrUpdateDepartment(@RequestHeader("userToken") String userToken, @RequestBody HashMap<String,Object> data) {
	    Map<String, Object> response = new HashMap<>();

	    // ✅ Validate user from token
	    Map<String,Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    UserCredentials user = (UserCredentials) verifiedUser.get("user");

	    // ✅ [NEW] Check user role (1 = Admin, 2 = sub-admin)
	    int role = user.getRole(); // Assuming getRole() returns an integer or can be parsed as one
	    if (role != 1 && role != 2) {
	        response = utils.createErrorResponse("Unauthorized: You are not allowed to perform this action");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	    }

	    // ✅ Validate organizationId
	    if (data.get("organizationId") == null || data.get("organizationId").toString().isEmpty()) {
	        response = utils.createErrorResponse("Missing organizationId");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    // ✅ Validate departmentName
	    if (data.get("departmentName") == null || data.get("departmentName").toString().isEmpty()) {
	        response = utils.createErrorResponse("department name is required");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    // ✅ Delegating to service for both create/update
	    response = departmentService.createOrUpdateDepartmentDetails(user, data);

	    if (response.get("status").toString().equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getDepartments")
	public ResponseEntity<Map<String, Object>> getDepartments(@RequestHeader("userToken") String userToken) {
	    Map<String, Object> response;

	    Map<String,Object> verifiedUser = utils.validateUser(userToken);
	    if(verifiedUser==null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    response = departmentService.getAllDepartments();
	    if (response.get("status").toString().equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}
	
	@PutMapping("/deleteDepartment")
	public ResponseEntity<Map<String, Object>> deleteDepartment(
	        @RequestHeader("userToken") String userToken,
	        @RequestBody HashMap<String, Object> data) {

	    Map<String, Object> response = new HashMap<>();

	    // Validate user
	    Map<String, Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }
	    UserCredentials user = (UserCredentials) verifiedUser.get("user");
	    int role = user.getRole(); // getting the role from userToken and authorizing 
	    if (role != 1 && role != 2) {
	        response = utils.createErrorResponse("Unauthorized: You are not allowed to perform this action");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	    }

	    // Validate input
	    if (data.get("departmentId").toString() == null) {
	        response = utils.createErrorResponse("Missing departmentId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // Perform soft delete
	    Long departmentId=Long.parseLong(data.get("departmentId").toString());
	    response = departmentService.deleteDepartment(departmentId);
	    if (response.get("status").equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    return ResponseEntity.ok(response);
	}
	
}
