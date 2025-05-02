package com.academyx.department.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.department.dto.DepartmentDTO;
import com.academyx.department.model.DepartmentDetails;

public interface DepartmentDetailsRepository extends JpaRepository<DepartmentDetails, Long>{
	
	@Query(value = "Select COUNT(u) > 0 from DepartmentDetails u where u.departmentName = :departmentName")
	boolean findDepartmentExistOrNot(@Param("departmentName") String departmentName);

	@Query("SELECT d FROM DepartmentDetails d WHERE d.departmentId = :departmentId AND d.status = 1")
	DepartmentDetails getDepartmentById(@Param("departmentId") Long departmentId);

	@Query("SELECT new com.academyx.department.dto.DepartmentDTO(d) FROM DepartmentDetails d WHERE d.status = 1")
    List<DepartmentDTO> findAllDepartmentDTOs();

}
