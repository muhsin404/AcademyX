package com.academyx.attendance.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.attendance.dto.AttendanceRequestDTO;
import com.academyx.attendance.service.AttendanceService;
import com.academyx.common.util.Utils;
import com.academyx.user.model.UserCredentials;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


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

//	@PostMapping("/getAttendance")
//	public ResponseEntity<Map<String, Object>> getAttendance(@RequestHeader("userToken") String userToken,
//	                                                         @RequestBody AttendanceRequestDTO request) {
//	    Map<String, Object> response = new HashMap<>();
//	    Map<String, Object> verifiedUser = utils.validateUser(userToken);
//
//	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//	                .body(utils.createErrorResponse("User not verified"));
//	    }
//
//	    UserCredentials user = (UserCredentials) verifiedUser.get("user");
//
//	    try {
//	        response = attendanceService.getAttendance(request, user);
//	        return ResponseEntity.ok(response);
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body(utils.createErrorResponse("Error fetching attendance: " + e.getMessage()));
//	    }
//	}

}
