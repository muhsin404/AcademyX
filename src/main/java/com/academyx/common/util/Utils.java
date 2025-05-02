package com.academyx.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;

@Service
public class Utils {
	
	@Autowired
	private UserCredentialsRepository userCredentialsRepository;
	
	public Map<String, Object> validateUser(String userToken) {
		Map<String, Object> response = new HashMap<>();
		UserCredentials user = userCredentialsRepository.verifyUser(userToken);
		if (user == null) {
			
			response.put("status", "Error");
			response.put("message", "Invalid Token");
			
			return response;
		} else {
			
			
			response.put("status", 1);
			response.put("user", user);
			return response;
		}
	}
	
	public  Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", errorMessage);
        errorResponse.put("status", "Error");
        return errorResponse;
    }
	
	public  Map<String, Object> createSuccessResponse(String successMessage) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", successMessage);
        successResponse.put("status", "Success");
        return successResponse;
    }
}
