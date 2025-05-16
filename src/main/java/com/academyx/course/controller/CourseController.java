package com.academyx.course.controller;

import org.springframework.web.bind.annotation.RestController;

import com.academyx.common.util.Utils;
import com.academyx.course.service.CourseService;
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
public class CourseController {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private CourseService courseService;

	@PostMapping("/createOrUpdateCourse") // <-- modified (renamed for clarity)
	public ResponseEntity<Map<String, Object>> createOrUpdateCourse(@RequestHeader("userToken") String userToken, @RequestBody HashMap<String, Object> data) {
	    Map<String, Object> response = new HashMap<>();

	    Map<String, Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    UserCredentials user = (UserCredentials) verifiedUser.get("user");

	    // Authorization check based on user role
	    int userRole = user.getRole(); // <-- new
	    if (userRole != 1 && userRole != 2) { // <-- new
	        response = utils.createErrorResponse("Unauthorized to perform this action"); // <-- new
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // <-- new
	    }

	    if (data.get("organizationId") == null || data.get("organizationId").toString().isEmpty()) {
	        response = utils.createErrorResponse("Missing organizationId");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    if (data.get("courseName") == null || data.get("courseName").toString().isEmpty() ||
	        data.get("courseDuration") == null || data.get("courseDuration").toString().isEmpty()) {

	        response = utils.createErrorResponse("course name is required");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    // Unified service method for create/update
	    response = courseService.createOrUpdateCourseDetails(user, data); // <-- modified method name

	    if (response.get("status").toString().equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getCourses")
	public ResponseEntity<Map<String, Object>> getCourses(@RequestHeader("userToken") String userToken) {
	    Map<String, Object> response;

	    Map<String,Object> verifiedUser = utils.validateUser(userToken);
	    if(verifiedUser==null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    response = courseService.getAllCourses();
	    if (response.get("status").toString().equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}
	
	@PutMapping("/deleteCourse")
	public ResponseEntity<Map<String, Object>> deleteCourse(
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
	    if (data.get("courseId").toString() == null) {
	        response = utils.createErrorResponse("Missing courseId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // Perform soft delete
	    Long courseId=Long.parseLong(data.get("courseId").toString());
	    response = courseService.deleteCourse(courseId);
	    if (response.get("status").equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    return ResponseEntity.ok(response);
	}
	
}
