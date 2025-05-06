package com.academyx.registration.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.academyx.common.util.Utils;
import com.academyx.communication.model.EmailRequest;
import com.academyx.user.model.Invitations;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.model.UserPersonalDetails;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.model.UserPersonalDetails;
import com.academyx.user.repository.InvitationRepository;
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
	
	@Autowired
	private InvitationRepository invitationRepository;
	
	RestTemplate restTemplate = new RestTemplate();
	private static final String EmailAPI = "http://localhost:8080/send-email";

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

	public Map<String, Object> addUser(HashMap<String, Object> data) {
//		Map<String, Object> response = new HashMap<>();
		String status="SENT";
		String email=data.get("email").toString();
		int role=Integer.parseInt(data.get("roleToBeAssigned").toString());
		
		//check if the user already exist and active
		UserCredentials userExist =repositoryForUserCredentials.findUserByEmail(email);
		if(userExist!=null)
		{
			//if user already exist send email as new accesses were added in your portal
			sendFeatureUpdateEmail(email,userExist.getUserPersonalDetails().getFullName());
			return utils.createSuccessResponse("New features have been added to the user");
		}
		//checking if an invitation is already sent to mail whom doesn't create an account 
		if (invitationRepository.existsByEmailAndRoleAndStatus(email, role, status)) {
		      return utils.createErrorResponse("Invitation already sent");  // 409
		    }
		
		//saving the invitation details in the table
		Invitations invitation = new Invitations();
		invitation.setEmail(email);
		invitation.setRole(role);
		invitation.setStatus(status);
		invitation.setToken(UUID.randomUUID().toString());       //to generate random token
		invitation.setExpiresAt(LocalDateTime.now().plusDays(3)); // 3 day for expiring the token
	    invitationRepository.save(invitation);

	    sendInvitationEmail(email, invitation.getToken(), role);
	    return utils.createSuccessResponse("Invitation sent to "+email);
	}

	
	//method to send email on adding new feature to a user
	private void sendFeatureUpdateEmail(String email, String fullName) {

		EmailRequest emailRequest=new EmailRequest();
		emailRequest.setTo(email);
		emailRequest.setSubject("You’ve Got New Features in Your AcademyX Portal!");
		EmailRequest.Body body=new EmailRequest.Body();
		body.setGreeting("Dear, "+fullName+ "!");
		body.setMain("We’ve just rolled out the following new features on your portal.Please refresh your portal to start using them right away!");
		body.setFooter("Best Regards,Company");
		emailRequest.setBody(body);
		restTemplate.postForObject(EmailAPI, emailRequest, Void.class);//calling email API
		
	}

	//method to send email to a new user asking to create account to use it
	private void sendInvitationEmail(String email, String token, int role) {
	    EmailRequest emailRequest = new EmailRequest();
	    emailRequest.setTo(email);
	    emailRequest.setSubject("You’ve Been Invited to Join the AcademyX Portal!");

	    EmailRequest.Body body = new EmailRequest.Body();
	    body.setGreeting("Dear Sir !");
	    String link = "http://localhost:8080/acceptInvitation?token=" + token;  // link send for testing 
	    body.setMain(
	        "You’ve been invited as role " + role +
	        ".\n\nClick the link below to create your account and get started:\n" +
	        link +
	        "\n\nLink expires in 72 hours. Please register before it expires!"
	    );
	    body.setFooter("Best Regards,\nCompany");

	    emailRequest.setBody(body);
	    restTemplate.postForObject(EmailAPI, emailRequest, Void.class);//calling email API
	}

	public Map<String, Object> acceptInvite(String token) {
        Map<String, Object> response = new HashMap<>();

        // 1. Find the invitation by token
        Invitations invite = invitationRepository
            .findByToken(token)
            .orElse(null);

        if (invite == null) {
            // no matching token
            return utils.createErrorResponse("Invalid invitation token");
        }

        // 2. Check expiration
        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            invite.setStatus("EXPIRED");
            invitationRepository.save(invite);
            return utils.createErrorResponse("Invitation has expired");
        }

        // 3. Mark as accepted
        invite.setStatus("ACCEPTED");
        invitationRepository.save(invite);

        // 4. Build success response
        Map<String, Object> data = new HashMap<>();
        data.put("email", invite.getEmail());
        data.put("roleAssigned", invite.getRole());

        return utils.createSuccessResponse("Invitation accepted");
    }

}
