package com.academyx.batch.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.batch.service.BatchService;
import com.academyx.common.util.Utils;
import com.academyx.user.model.UserCredentials;

@RestController
public class BatchController {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private BatchService batchService;

	@PostMapping("/createBatch")
	public ResponseEntity<Map<String, Object>> createBatch(@RequestHeader("userToken") String userToken, @RequestBody HashMap<String,Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		Map<String,Object> verifiedUser = utils.validateUser(userToken);
		if(verifiedUser==null || verifiedUser.get("status").equals("Error"))
		{
			response= utils.createErrorResponse("User not verified");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		
		UserCredentials user =(UserCredentials) verifiedUser.get("user");
		
		if(data.get("organizationId").toString()==null || data.get("organizationId").toString().isEmpty())
		{
			response=utils.createErrorResponse("Missing organizationId");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		
       if (data.get("batchName") == null || data.get("batchName").toString().isEmpty()||
    		   data.get("batchStartingDate")==null || data.get("batchStartingDate").toString().isEmpty()||
    		   data.get("batchEndingDate")==null || data.get("batchEndingDate").toString().isEmpty()) {

			response= utils.createErrorResponse("Enter the required parameters");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		} else {
			response = batchService.createBatchDetails(user,data);
			if (response.get("status").toString().equals("Error")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

			} else {
				return ResponseEntity.ok(response);
			}
		}
		
	}
	
	@GetMapping("/getBatches")
	public ResponseEntity<Map<String, Object>> getBatches(
	        @RequestHeader("userToken") String userToken,
	        @RequestParam(value = "batchId", required = false) Long batchId) {

	    Map<String, Object> response;
	    Map<String,Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || "Error".equals(verifiedUser.get("status"))) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    response = batchService.getBatches(batchId);
	    if ("Error".equals(response.get("status"))) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }
	    return ResponseEntity.ok(response);
	}


	
	@PostMapping("/assignUser")
	public ResponseEntity<Map<String, Object>>assignUserToBatch(@RequestHeader("userToken") String userToken,@RequestBody HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		Map<String,Object> verifiedUser = utils.validateUser(userToken);
		if(verifiedUser==null || verifiedUser.get("status").equals("Error"))
		{
			response= utils.createErrorResponse("User not verified");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		
		UserCredentials adminUser =(UserCredentials) verifiedUser.get("user");
				
		if(data.get("batchId")==null || data.get("batchId").toString().isEmpty() ||
		   data.get("userIds")==null || data.get("userIds").toString().isEmpty() ||
		   data.get("organizationId")==null || data.get("organizationId").toString().isEmpty())
		{
			response = utils.createErrorResponse("batchId, organizationId, userIds are required");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}
		
		else 
		{
			response = batchService.assignUserToBatch(adminUser,data);
			return response.get("status").equals("Error")?
					ResponseEntity.status(HttpStatus.CONFLICT).body(response):ResponseEntity.ok(response);
		}
	}
	
	
	@PutMapping("/deleteBatch")
	public ResponseEntity<Map<String, Object>> deleteBatch(
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
	    if (data.get("batchId").toString() == null) {
	        response = utils.createErrorResponse("Missing batchId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    // Perform soft delete
	    Long batchId=Long.parseLong(data.get("batchId").toString());
	    response = batchService.deleteBatch(batchId);
	    if (response.get("status").equals("Error")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    return ResponseEntity.ok(response);
	}
}
