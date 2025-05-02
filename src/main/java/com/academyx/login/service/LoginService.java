package com.academyx.login.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.common.util.Utils;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;

@Service
public class LoginService {
	
	@Autowired
	private UserCredentialsRepository repositoryForCustomerCredentials;
	
	@Autowired
	private Utils utils;

	public Map<String, Object> loginTheCustomer(HashMap<String, Object> data) {
	    HashMap<String, Object> response = new HashMap<>();

	    try {
	        String email = data.get("email").toString();
	        String password = data.get("password").toString();

	        UserCredentials user = repositoryForCustomerCredentials.findUserByEmail(email);

	        if (user == null) {
	        	
	            return utils.createErrorResponse("user not found");
	            
	        } else if (!user.getPassword().equals(password)) {
	        	
	             return utils.createErrorResponse("invalid password");
	        } else {
	        	
	            return utils.createSuccessResponse("SuccessFully Logged in");
	        }
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "An unexpected error occurred: " + e.getMessage());
	        return response;
	    }

	}


}
