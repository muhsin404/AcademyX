package com.academyx.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.batch.dto.BatchDTO;
import com.academyx.batch.model.BatchDetails;

public interface BatchDetailsRepository extends JpaRepository<BatchDetails, Long>{
	
	@Query(value = "Select COUNT(u) > 0 from BatchDetails u where u.batchName = :batchName")
	boolean findBatchExistOrNot(@Param("batchName") String batchName);
	

	@Query("SELECT b FROM BatchDetails b WHERE b.batchId = :batchId AND b.status = 1")
	BatchDetails getBatchById(@Param("batchId") Long batchId);

	@Query("SELECT new com.academyx.batch.dto.BatchDTO(b) FROM BatchDetails b WHERE b.status = 1")
    List<BatchDTO> findAllBatchDTOs();
}
