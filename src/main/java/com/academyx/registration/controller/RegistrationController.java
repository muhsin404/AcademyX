package com.academyx.registration.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.registration.service.RegistrationService;

@RestController
public class RegistrationController {

	@Autowired
	public RegistrationService registrationService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerUser(@RequestBody HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		if (data.get("firstName") == null || data.get("firstName").toString().isEmpty() || data.get("lastName") == null
				|| data.get("lastName").toString().isEmpty() || data.get("email") == null
				|| data.get("email").toString().isEmpty() || data.get("phoneNumber") == null
				|| data.get("phoneNumber").toString().isEmpty() || data.get("password") == null
				|| data.get("password").toString().isEmpty()) {

			response.put("status", "error");
			response.put("message", "All Feilds Required");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			response = registrationService.saveRegistrationDetails(data);
			if (response.get("status").toString().equals("error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
		}

	}
	
	@PostMapping("/createUser")
	public ResponseEntity<Map<String, Object>> creatingUser(@RequestBody HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
			if (   data.get("email") == null
				|| data.get("email").toString().isEmpty() || data.get("phoneNumber") == null
				|| data.get("phoneNumber").toString().isEmpty() || data.get("password") == null
				|| data.get("password").toString().isEmpty() || data.get("role")==null
				|| data.get("role").toString().isEmpty()) {

			response.put("status", "error");
			response.put("message", "All Fields Required");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			response = registrationService.createTheUser(data);
			if (response.get("status").toString().equals("error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
		}
	}
	

}