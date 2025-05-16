package com.academyx.subject.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.academyx.common.util.Utils;
import com.academyx.subject.service.SubjectServices;
import com.academyx.user.model.UserCredentials;

@RestController
public class SubjectController {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private SubjectServices subjectServices;
	
	@PostMapping("/createSubject") // Consider renaming this to /saveSubject if you want clarity
	public ResponseEntity<Map<String, Object>> createOrUpdateSubject(
	        @RequestHeader("userToken") String userToken,
	        @RequestBody HashMap<String, Object> data) {

	    Map<String, Object> response = new HashMap<>();

	    Map<String,Object> verifiedUser = utils.validateUser(userToken);
	    if(verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }

	    UserCredentials user = (UserCredentials) verifiedUser.get("user");

	    // ✨ ADDED: Check user role (1=Admin, 2=Manager)
	    if (user.getRole() != 1 && user.getRole() != 2) {
	        response = utils.createErrorResponse("Unauthorized user");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	    }

	    if (data.get("organizationId") == null || data.get("organizationId").toString().isEmpty()) {
	        response = utils.createErrorResponse("Missing organizationId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    if (data.get("departmentId") == null || data.get("departmentId").toString().isEmpty()) {
	        response = utils.createErrorResponse("Missing departmentId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    if (data.get("subjectName") == null || data.get("subjectName").toString().isEmpty()) {
	        response = utils.createErrorResponse("Subject name is required");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // ✨ MODIFIED: Common handler for create or update
	    response = subjectServices.createOrUpdateSubject(user, data);

	    if ("Error".equals(response.get("status"))) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getSubjects")
	public ResponseEntity<Map<String, Object>> getSubjects(@RequestHeader("userToken") String userToken) {
	    Map<String, Object> response;

	    Map<String,Object> verifiedUser = utils.validateUser(userToken);
	    if(verifiedUser==null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    response = subjectServices.getAllSubjects();
	    if (response.get("status").toString().equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}
	
	@PutMapping("/deleteSubject")
	public ResponseEntity<Map<String, Object>> deleteSubject(
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
	    if (data.get("subjectId").toString() == null) {
	        response = utils.createErrorResponse("Missing subjectId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // Perform soft delete
	    Long subjectId=Long.parseLong(data.get("subjectId").toString());
	    response = subjectServices.deleteSubject(subjectId);
	    if (response.get("status").equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    return ResponseEntity.ok(response);
	}


}
