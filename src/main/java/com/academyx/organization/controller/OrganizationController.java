package com.academyx.organization.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.common.util.Utils;
import com.academyx.organization.service.OrganizationService;
import com.academyx.user.model.UserCredentials;

@RestController
public class OrganizationController {

	@Autowired
	public OrganizationService organizationService;

	@Autowired
	public Utils utils;

	@PostMapping("/registerOrganization")
	public ResponseEntity<Map<String, Object>> createOrganization(@RequestHeader("userToken") String userToken,
			@RequestBody HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		if (userToken == null || userToken.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(utils.createErrorResponse("Missing userToken in headers"));

		}

		Map<String, Object> verifiedUser = utils.validateUser(userToken);

		if (verifiedUser.get("status") == null || verifiedUser.get("status").equals("error")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(utils.createErrorResponse("User Not Found"));

		}

		

			if (data.get("organizationName") == null || data.get("organizationName").toString().isEmpty() || 
					data.get("organizationEmail") == null|| data.get("organizationEmail").toString().isEmpty() || 
					data.get("website") == null || data.get("website").toString().isEmpty()) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(utils.createErrorResponse("Enter Required Parameters"));
			} 

				UserCredentials user = (UserCredentials) verifiedUser.get("user");
				// Long userId=user.getUserId();
				response = organizationService.createOrganization(user, data);

				if (response.get("status").toString().equals("error")) {

					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
				} else {
					return ResponseEntity.ok(response);
				}
			}
		

	

	@PostMapping("/updateOrganizationProfile")
	public ResponseEntity<Map<String, Object>> organizationProfileUpdate(@RequestHeader("userToken") String userToken,
			@RequestBody Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		if (userToken == null || userToken.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(utils.createErrorResponse("Missing userToken in headers"));
		}
		Map<String, Object> verifiedUser = utils.validateUser(userToken);

		if (verifiedUser.get("status") == null || verifiedUser.get("status").equals("error")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(utils.createErrorResponse("User Not Found"));
		}

		else {

			if (data.get("industry") == null || data.get("industry").toString().isEmpty()) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(utils.createErrorResponse("Enter Required Parameters"));

			} else {
				response = organizationService.profileUpdate(verifiedUser, data);
				if (response.get("status").toString().equals("error")) {

					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
				} else {
					return ResponseEntity.ok(response);
				}
			}

		}

	}
}
