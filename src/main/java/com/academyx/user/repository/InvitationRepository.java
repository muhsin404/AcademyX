package com.academyx.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.user.model.Invitations;

public interface InvitationRepository extends JpaRepository<Invitations, UUID>{
	
	@Query(
		      "SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END " +
		      "FROM Invitations i " +
		      "WHERE i.email = :email " +
		      "  AND i.role  = :role  " +
		      "  AND i.status = :status"
		    )
		    boolean existsByEmailAndRoleAndStatus(
		      @Param("email") String email,
		      @Param("role")  int role,
		      @Param("status") String status
		    );

	@Query("SELECT i FROM Invitations i WHERE i.token = :token")
    Optional<Invitations> findByToken(@Param("token") String token);
}
