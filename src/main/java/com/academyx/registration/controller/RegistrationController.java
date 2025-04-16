package com.academyx.registration.controller;

import org.springframework.web.bind.annotation.RestController;

import com.academyx.registration.service.RegistrationService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;
	
	@PostMapping("/userRegistration")
	public ResponseEntity<Map<String, Object>> registerUser(@RequestBody HashMap<String, Object> data) {
		Map<String, Object> response= new HashMap<>();
		
		
		//checking if the data came from front-end is empty or null
		
		if(data.get("firstName")==null || data.get("firstName").toString().isEmpty()||
		   data.get("lastName")==null || data.get("lastName").toString().isEmpty() ||
		   data.get("email")==null || data.get("email").toString().isEmpty() ||
		   data.get("phoneNumber")==null || data.get("phoneNumber").toString().isEmpty() ||
		   data.get("password")==null ||data.get("password").toString().isEmpty())
			
		{
			// if any of the data is empty or null sending back the error message  
			response.put("status", "Error");
			response.put("message", "all parameters required");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		else
		{
			//saving the details of the user 
			response=registrationService.saveRegisteredUser(data);
			if(response.get("status").toString().equals("Error"))
			{
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			return ResponseEntity.ok(response);
		}
		
		
	}
	
	

}
