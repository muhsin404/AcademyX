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
	
	@PostMapping("/createSubject")
	public ResponseEntity<Map<String, Object>> createSubject(@RequestHeader("userToken") String userToken, @RequestBody HashMap<String,Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		Map<String,Object> verifiedUser = utils.validateUser(userToken);
		if(verifiedUser==null || verifiedUser.get("status").equals("Error"))
		{
			response= utils.createErrorResponse("User not verified");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		
		UserCredentials user = (UserCredentials) verifiedUser.get("user");
		
		if(data.get("organizationId").toString()==null || data.get("organizationId").toString().isEmpty())
		{
			response=utils.createErrorResponse("Missing organizationId");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		if (data.get("departmentId") == null || data.get("departmentId").toString().isEmpty()) {
		    response = utils.createErrorResponse("Missing departmentId");
		    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}

       if (data.get("subjectName") == null || data.get("subjectName").toString().isEmpty()) {

			response= utils.createErrorResponse("subject name is required");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			response = subjectServices.createSubjectDetails(user,data);
			if (response.get("status").toString().equals("Error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
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
