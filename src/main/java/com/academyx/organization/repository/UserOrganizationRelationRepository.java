package com.academyx.organization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.organization.model.Organizations;
import com.academyx.organization.model.UserOrganizationRelation;
import com.academyx.user.model.UserCredentials;


public interface UserOrganizationRelationRepository extends JpaRepository<UserOrganizationRelation, Long> {

	@Query("SELECT uo.organization FROM UserOrganizationRelation uo WHERE uo.user.userToken = :userToken")
	List<Organizations> findOrganizationsByUserToken(@Param("userToken") String userToken);

	@Query("SELECT r FROM UserOrganizationRelation r WHERE r.user = :user")
	UserOrganizationRelation findRelationByUser(@Param("user")  UserCredentials user);
	
	@Query("SELECT r FROM UserOrganizationRelation r WHERE r.user = :user AND r.organization = :organization")
	UserOrganizationRelation findByUserAndOrganization(@Param("user") UserCredentials user,
	                                                   @Param("organization") Organizations organization);


}
