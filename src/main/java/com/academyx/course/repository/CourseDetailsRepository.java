package com.academyx.course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.batch.dto.BatchDTO;
import com.academyx.course.dto.CourseDTO;
import com.academyx.course.model.CourseDetails;

public interface CourseDetailsRepository extends JpaRepository<CourseDetails, Long>{
	
	@Query(value = "Select COUNT(u) > 0 from CourseDetails u where u.courseName = :courseName")
	boolean findCourseExistOrNot(@Param("courseName") String courseName);

	@Query("SELECT c FROM CourseDetails c WHERE c.courseId = :courseId AND c.status = 1")
	CourseDetails getCourseById(@Param("courseId") Long courseId);

	@Query("SELECT new com.academyx.course.dto.CourseDTO(c) FROM CourseDetails c WHERE c.status = 1")
    List<CourseDTO> findAllCourseDTOs();

}
