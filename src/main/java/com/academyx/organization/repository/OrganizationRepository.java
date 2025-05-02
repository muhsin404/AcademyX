package com.academyx.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.organization.model.Organizations;


public interface OrganizationRepository extends JpaRepository<Organizations, Long>{
	

	@Query("SELECT o FROM Organizations o WHERE o.organizationName = :organizationName")
   Organizations findByOrganizationName(@Param("organizationName") String organizationName);

	
	@Query(value = "select o from Organizations o where o.organizationId = :organizationId")
	Organizations getOrganizationByOrganizationId(@Param("organizationId") Long organizationId);

}
