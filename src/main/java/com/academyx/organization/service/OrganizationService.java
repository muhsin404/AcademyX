package com.academyx.organization.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.common.util.Utils;
import com.academyx.organization.model.Organizations;
import com.academyx.organization.model.UserOrganizationRelation;
import com.academyx.organization.repository.OrganizationRepository;
import com.academyx.organization.repository.UserOrganizationRelationRepository;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;



@Service
public class OrganizationService {

	@Autowired
	public Utils utils;

	@Autowired
	public OrganizationRepository organizationRepository;

	@Autowired
	public UserCredentialsRepository userCredentialsRepository;

	@Autowired
	public UserOrganizationRelationRepository userOrganizationRelationRepository;

	public Map<String, Object> createOrganization(UserCredentials user, HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		try {

			// Long userId=userCredentials.getUserId();
			// Retrieve organization details from input data
			String organizationName = data.get("organizationName").toString();
			Organizations existingOrganization = organizationRepository.findByOrganizationName(organizationName);

			Organizations organization;

			if (existingOrganization != null) {
				// Organization exists, assign sub-admin role for joining user
				organization = existingOrganization;
			} else {
				// Organization does not exist, create a new one and assign admin role for the
				// creator
				organization = new Organizations();
				organization.setOrganizationName(organizationName);
				organization.setOrganizationEmail(data.get("organizationEmail").toString());
				organization.setWebsite(data.get("website").toString());
				organizationRepository.save(organization);
			}

			// Check if user is already related to the organization
			UserOrganizationRelation existingRelation = userOrganizationRelationRepository
					.findByUserAndOrganization(user, organization);

			if (existingRelation != null) {
				return utils.createErrorResponse("User is already associated with this organization");
			}

			// Create a relation linking the user with the organization and set the role
			// accordingly
			UserOrganizationRelation relation = new UserOrganizationRelation();
			relation.setUser(user);
			relation.setOrganization(organization);
			userOrganizationRelationRepository.save(relation);

			response.put("status", "success");
			response.put("message", "Organization Registered");
			return response;

		} catch (Exception e) {
			return utils.createErrorResponse("Exception" + e.getMessage());
		}
	}

	// Update Organization Profile
	public Map<String, Object> profileUpdate(Map<String, Object> verifiedUser, Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();

		try {

			// Fetch the batch details
			Long orgId = Long.parseLong(data.get("organizationId").toString());
			Organizations organization = organizationRepository.getOrganizationByOrganizationId(orgId);
			if (organization == null) {
				return utils.createErrorResponse("Invalid organization ID");
			}
			UserCredentials user = (UserCredentials) verifiedUser.get("user");
			UserOrganizationRelation relation = userOrganizationRelationRepository.findRelationByUser(user);

			if (relation == null || !relation.getOrganization().getOrganizationId().equals(organization.getOrganizationId())) {
				return utils.createErrorResponse("User does not have access to this organization");
			}

			// organization = relation.getOrganization();

			organization.setIndustry(data.get("industry").toString());
			organization.setOrganizationType(data.get("organizationType").toString());
			organization.setTagline(data.get("tagline").toString());
			organizationRepository.save(organization);

			response.put("status", "success");
			response.put("message", "Profile Updated");
			return response;

		} catch (Exception e) {
			return utils.createErrorResponse("Exception" + e.getMessage());
		}

	}
}
