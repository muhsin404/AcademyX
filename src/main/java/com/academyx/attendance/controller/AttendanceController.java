package com.academyx.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.attendance.dto.AttendanceRequestDTO;
import com.academyx.attendance.dto.StudentAttendanceDTO;
import com.academyx.attendance.service.AttendanceService;
import com.academyx.common.util.Utils;
import com.academyx.user.model.UserCredentials;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class AttendanceController {
	
	@Autowired
	private AttendanceService attendanceService;
	
	@Autowired
	private Utils utils;
	
	@PostMapping("/markAttendance")
	public ResponseEntity<Map<String, Object>> markAttendance(@RequestHeader("userToken") String userToken,
	                                                          @RequestBody AttendanceRequestDTO attendanceRequest) {
	    Map<String, Object> response = new HashMap<>();

	    Map<String, Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }

	    UserCredentials user = (UserCredentials) verifiedUser.get("user");

	    try {
	        response=attendanceService.markAttendance(attendanceRequest, user);
	        if (response.get("status").toString().equals("Error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
	    } catch (Exception e) {
	        response = utils.createErrorResponse("Error while marking attendance: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	@GetMapping("/getAttendance")
	public ResponseEntity<Map<String, Object>> getAttendance(@RequestHeader("userToken") String userToken ,@RequestParam("studentId") Long studentId,
							@RequestParam(value="sessionDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionDate) {
		
		Map<String, Object> response = new HashMap<>();

	    Map<String, Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	    UserCredentials user = (UserCredentials) verifiedUser.get("user");
	    
	    if (sessionDate == null) {
	        sessionDate = LocalDate.now(); // default to today if no date is provided in request 
	    }
	    
	    try {
	        response=attendanceService.getStudentAttendanceByDate(studentId,sessionDate);
	        if (response.get("status").toString().equals("Error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
	    } catch (Exception e) {
	        response = utils.createErrorResponse("Error while marking attendance: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	
	}
	
}
