package com.academyx.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.batch.dto.AssignedUserDTO;
import com.academyx.batch.dto.UserDTO;
import com.academyx.batch.model.UserBatchRelation;
import com.academyx.user.model.UserCredentials;

public interface UserBatchRelationRepository extends JpaRepository<UserBatchRelation, Long>{

	// 2.a Fetch IDs already on this batch
    @Query("""
      SELECT new com.academyx.batch.dto.AssignedUserDTO(
               r.userCredentials.userId,
               'alreadyAssigned'
             )
        FROM UserBatchRelation r
       WHERE r.userCredentials.userId    IN :userIds
         AND r.batchDetails.batchId       = :batchId
         AND r.organization.organizationId = :organizationId
    """)
    List<AssignedUserDTO> findByUserIdInAndBatchIdAndOrganizationId(
        @Param("userIds") List<Long> userIds,
        @Param("batchId") Long batchId,
        @Param("organizationId") Long organizationId);

    // 2.b Check any assignment for student in this organization
    @Query("""
      SELECT r FROM UserBatchRelation r
       WHERE r.userCredentials.userId    = :userId
         AND r.organization.organizationId = :organizationId
    """)
    List<UserBatchRelation> findByUserIdAndOrganizationId(
        @Param("userId") Long userId,
        @Param("organizationId") Long organizationId);
    
    @Query("""
    		  SELECT new com.academyx.batch.dto.AssignedUserDTO(
    		           r.userCredentials.userId,
    		           CASE WHEN r.userCredentials.role = 3 THEN 'student' ELSE 'staff' END
    		         )
    		    FROM UserBatchRelation r
    		   WHERE r.batchDetails.batchId       = :batchId
    		     AND r.organization.organizationId = :organizationId
    		     AND r.status = 1
    		""")
    		List<AssignedUserDTO> findAssignedByBatchId(
    		    @Param("batchId") Long batchId,
    		    @Param("organizationId") Long organizationId);

    
    @Query("SELECT CASE WHEN COUNT(ub) > 0 THEN true ELSE false END FROM UserBatchRelation ub WHERE ub.userCredentials.userId = :userId AND ub.batchDetails.batchId = :batchId")
    boolean existsByUserIdAndBatchId(@Param("userId") Long userId, @Param("batchId") Long batchId);
    
    @Query("SELECT new com.academyx.batch.dto.UserDTO(ub.userCredentials.userId, ub.userCredentials.userPersonalDetails.fullName) " +
    	       "FROM UserBatchRelation ub " +
    	       "WHERE ub.batchDetails.batchId = :batchId AND ub.userCredentials.role = 4")
    List<UserDTO> findStaffByBatchId(@Param("batchId") Long batchId);
    
    @Query("SELECT new com.academyx.batch.dto.UserDTO(ub.userCredentials.userId, ub.userCredentials.userPersonalDetails.fullName) " +
    	       "FROM UserBatchRelation ub " +
    	       "WHERE ub.batchDetails.batchId = :batchId AND ub.userCredentials.role = 5")
    List<UserDTO> findStudentsByBatchId(@Param("batchId") Long batchId);
}
