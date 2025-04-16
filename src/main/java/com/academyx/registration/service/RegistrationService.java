package com.academyx.registration.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.registration.model.UserCredentials;
import com.academyx.registration.model.UserPersonelDetails;
import com.academyx.registration.repository.RegistrationRepositoryForUserCredentials;
import com.academyx.registration.repository.RegistrationRepositoryForUserPersonelDetails;

@Service
public class RegistrationService {
	
	@Autowired
	private RegistrationRepositoryForUserCredentials registrationRepositoryForUserCredentials;
	
	@Autowired
	private RegistrationRepositoryForUserPersonelDetails registrationRepositoryForUserPersonelDetails;

	public Map<String, Object> saveRegisteredUser(HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			String firstName=data.get("firstName").toString();
			String lastName=data.get("lastName").toString();
			String fullName=firstName+lastName;
			
			String email=data.get("email").toString().trim();
			boolean emailExistOrNot=registrationRepositoryForUserCredentials.findEmailExistOrNot(email);
			if (emailExistOrNot)//checking if the email already exist or not
			{
				response.put("status", "Error");
				response.put("message", "This Email already exist");
				return response;
			}
			else
			{
				UserPersonelDetails userPersonelDetails= new UserPersonelDetails();
				userPersonelDetails.setFullName(fullName);
				userPersonelDetails.setPhoneNumber(data.get("phoneNumber").toString());
				registrationRepositoryForUserPersonelDetails.save(userPersonelDetails);
				
				
				UserCredentials userCredentials =new UserCredentials();
				userCredentials.setEmail(email);
				userCredentials.setPassword(data.get("password").toString());
				userCredentials.setStatus(1);
				userCredentials.setRole(2);
				userPersonelDetails.setUserCredentials(userCredentials);
				userCredentials.setUserPersonelDetails(userPersonelDetails);
				registrationRepositoryForUserCredentials.save(userCredentials);
				
				response.put("status", "Success");
				response.put("message", "Account created Successfully");
				return response;
			}
			
		} catch (Exception e) {

			response.put("Some error occured", e);
			return response;
		}
	}	

}
