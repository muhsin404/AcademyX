package com.academyx.notesOrAssignments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.notesOrAssignments.model.UploadMaterials;

public interface UploadMaterialsRepository extends JpaRepository<UploadMaterials, Long>{
	
	@Query("SELECT n FROM UploadMaterials n WHERE n.batch.batchId = :batchId AND n.subject.subjectId = :subjectId")
	List<UploadMaterials> findNotesORAssignmentsByBatchIdAndSubjectId(@Param("batchId") Long batchId, @Param("subjectId") Long subjectId);

}
