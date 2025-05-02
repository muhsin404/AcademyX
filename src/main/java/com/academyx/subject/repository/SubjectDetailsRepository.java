package com.academyx.subject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.subject.dto.SubjectDTO;
import com.academyx.subject.model.SubjectDetails;

public interface SubjectDetailsRepository extends JpaRepository<SubjectDetails, Long>{
	
	@Query(value = "Select COUNT(u) > 0 from SubjectDetails u where u.subjectName = :subjectName")
	boolean findSubjectExistOrNot(@Param("subjectName") String subjectName);

	@Query("SELECT s FROM SubjectDetails s WHERE s.subjectId = :subjectId AND s.status = 1")
	SubjectDetails getSubjectById(@Param("subjectId") Long subjectId);

	@Query("SELECT new com.academyx.subject.dto.SubjectDTO(s) FROM SubjectDetails s WHERE s.status = 1")
    List<SubjectDTO> findAllSubjectDTOs();
	
}
