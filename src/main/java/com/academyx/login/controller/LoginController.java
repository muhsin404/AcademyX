package com.academyx.login.controller;

import org.springframework.web.bind.annotation.RestController;

import com.academyx.common.util.Utils;
import com.academyx.login.service.LoginService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private Utils utils;
	
	@GetMapping("/check")
	public String checking() {
		return "working";
	}

	@PostMapping("/userLogin")
	public ResponseEntity<Map<String, Object>> loginCustomer(@RequestBody HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		
		if(data.get("email")==null || data.get("email").toString().isEmpty() ||
				data.get("password")==null || data.get("password").toString().isEmpty())
		{
			response.put("status", "Error");
			response.put("message", "Enter all details");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			
		}
		else
		{
			System.out.println(data.get("email"));
			System.out.println(data.get("password"));
			response = loginService.loginTheCustomer(data);
			if (response.get("status").toString().equals("Error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
			
		}
	}
	
}
