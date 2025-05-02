package com.academyx.registration.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.common.util.Utils;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.model.UserPersonalDetails;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.model.UserPersonalDetails;
import com.academyx.user.repository.UserCredentialsRepository;
import com.academyx.user.repository.UserPersonalDetailsRepository;
import com.academyx.user.repository.UserCredentialsRepository;
import com.academyx.user.repository.UserPersonalDetailsRepository;


@Service
public class RegistrationService {

	@Autowired
	private Utils utils;
	
	@Autowired
	public UserCredentialsRepository repositoryForUserCredentials;

	@Autowired
	public UserPersonalDetailsRepository repositoryForUserPersonalDetails;

	public Map<String, Object> saveRegistrationDetails(HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		try {

			String email = data.get("email").toString().trim();
			boolean isEmailExist = repositoryForUserCredentials.findEmailExistOrNot(email);
//			boolean isEmailExist = true;
			// checking email exist or not
			if (isEmailExist) {
				return utils.createErrorResponse("Email already exists");

			} else {

				String firstName = data.get("firstName").toString();
				String lastName = data.get("lastName").toString();
				String fullName = firstName + lastName;

				// saving to personal details table
				UserPersonalDetails userPersonalDetails = new UserPersonalDetails();
				userPersonalDetails.setFullName(fullName);
				userPersonalDetails.setPhoneNumber(data.get("phoneNumber").toString().trim());
				repositoryForUserPersonalDetails.save(userPersonalDetails);

				// saving to userCredential table
				UserCredentials userCredentials = new UserCredentials();
				userCredentials.setEmail(data.get("email").toString());
				userCredentials.setPassword(data.get("password").toString());
				userCredentials.setRole(2); // role 2 = customer / admin of organizations
				userCredentials.setStatus(1);
				userCredentials.setUserPersonalDetails(userPersonalDetails);
				userPersonalDetails.setUserCredentials(userCredentials);
				repositoryForUserCredentials.save(userCredentials);
				
				return utils.createSuccessResponse("successfully registered");


			}

		} catch (Exception e) {
			response.put("status", "Error" + e);
			return response;
		}

		
	}

	public Map<String, Object> createTheUser(HashMap<String, Object> data) {
		HashMap<String, Object> response = new HashMap<>();

		try {

			String email = data.get("email").toString().trim();
			boolean isEmailExist = repositoryForUserCredentials.findEmailExistOrNot(email);
//			boolean isEmailExist = true;
			// checking email exist or not
			if (isEmailExist) {
				return utils.createErrorResponse("Email already exists");
			} else {


				// saving to personal details table
				UserPersonalDetails userPersonalDetails = new UserPersonalDetails();
				UserCredentials userCredentials = new UserCredentials();
				
				userPersonalDetails.setPhoneNumber(data.get("phoneNumber").toString().trim());
				repositoryForUserPersonalDetails.save(userPersonalDetails);

				// saving to userCredential table
				userCredentials.setEmail(data.get("email").toString());
				userCredentials.setPassword(data.get("password").toString());
				userCredentials.setRole(Integer.parseInt(data.get("role").toString())); // role 1 = sub Admin of the organization
				userCredentials.setStatus(1);
				userPersonalDetails.setUserCredentials(userCredentials);
				repositoryForUserCredentials.save(userCredentials);
				
				return utils.createSuccessResponse("successfully created the user");

			}

		} catch (Exception e) {
			response.put("status", "Error" + e);
			return response;
		}
	}

	
	
}
