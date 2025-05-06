package com.academyx.feedbackAndComplaints.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.common.util.Utils;
import com.academyx.feedbackAndComplaints.service.FeedbackOrComplaintsService;
import com.academyx.user.model.UserCredentials;

@RestController
public class FeedbacksOrComplaintsController {
	
	 @Autowired
	    private FeedbackOrComplaintsService feedbackOrComplaintsService;
	 
	 @Autowired
	 private Utils utils;

	    @PostMapping("/submitFeedbackOrComplaints")
	    public ResponseEntity<Map<String, Object>> submitFeedbackComplaint(@RequestHeader("userToken") String userToken,@RequestBody HashMap<String, Object> data) {
			Map<String, Object> response = new HashMap<>();

	    	Map<String,Object> verifiedUser = utils.validateUser(userToken);
			if(verifiedUser==null || verifiedUser.get("status").equals("Error"))
			{
				response= utils.createErrorResponse("User not verified");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			
			UserCredentials user = (UserCredentials) verifiedUser.get("user");
			
			if (data.get("type") == null || data.get("subject") == null || data.get("message") == null ) {
	            response= utils.createErrorResponse("All fields (type, subject, message) are required");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	        }else
	        {
	        	response=feedbackOrComplaintsService.submitFeedbacksOrComplaints(data,user);
	        	if (response.get("status").toString().equals("Error")) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

				} else {
					return ResponseEntity.ok(response);
				}
	        }
			
	    }

	    @GetMapping("/getAllFeedbacksOrComplaints")
	    public ResponseEntity<Map<String, Object>> getAllFeedbacksAndComplaints(@RequestHeader("userToken") String userToken) {
	    	Map<String, Object> response = new HashMap<>();

	    	Map<String,Object> verifiedUser = utils.validateUser(userToken);
			if(verifiedUser==null || verifiedUser.get("status").equals("Error"))
			{
				response= utils.createErrorResponse("User not verified");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			UserCredentials user = (UserCredentials) verifiedUser.get("user");
			if(user.getRole()==1) {
				response=feedbackOrComplaintsService.getAllFeedbacksOrComplaints();
				if (response.get("status").toString().equals("Error")) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

				} else {
						return ResponseEntity.ok(response);
					}
			}
			else {
//				Long userId=user.getuserId();
				response=feedbackOrComplaintsService.getFeedbackOrComplaintsOfAUser(user);
				if (response.get("status").toString().equals("Error")) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

				} else 
					{
						return ResponseEntity.ok(response);
					}
			}
	    }

}
