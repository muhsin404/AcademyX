package com.academyx.course.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.academyx.batch.dto.BatchDTO;
import com.academyx.common.util.Utils;
import com.academyx.course.dto.CourseDTO;
import com.academyx.course.model.CourseDetails;
import com.academyx.course.repository.CourseDetailsRepository;
import com.academyx.organization.model.Organizations;
import com.academyx.organization.repository.OrganizationRepository;
import com.academyx.user.model.UserCredentials;

@Service
public class CourseService {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	private CourseDetailsRepository courseDetailsRepository;

	public Map<String, Object> createOrUpdateCourseDetails(UserCredentials user, HashMap<String, Object> data) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        Long orgId = Long.parseLong(data.get("organizationId").toString());
	        Organizations organization = organizationRepository.getOrganizationByOrganizationId(orgId);
	        if (organization == null) {
	            return utils.createErrorResponse("Invalid organization ID");
	        }

	        String courseName = data.get("courseName").toString().trim();

	        if (data.containsKey("courseId") && data.get("courseId") != null && !data.get("courseId").toString().isEmpty()) {
	            // --- UPDATE FLOW ---
	            Long courseId = Long.parseLong(data.get("courseId").toString()); // <-- new
	            CourseDetails optionalCourse = courseDetailsRepository.getCourseById(courseId); // <-- new
	            if (optionalCourse==null) {
	                return utils.createErrorResponse("Course not found with the given ID"); // <-- new
	            }

	            CourseDetails course = optionalCourse;

	            // Optional: Check if the course name is changing, and if it's a duplicate
	            if (!course.getCourseName().equalsIgnoreCase(courseName)) {
	                boolean isCourseExist = courseDetailsRepository.findCourseExistOrNot(courseName);
	                if (isCourseExist) {
	                    return utils.createErrorResponse("Course name already exists");
	                }
	            }

	            // Update other fields
	            course.setCourseName(courseName); 
	            course.setCourseDescription(data.get("courseDescription").toString());
	            course.setDuration(data.get("courseDuration").toString());
	            course.setOrganization(organization); // In case org is changed
	            course.setUpdatedBy(user); // Optional if you track update info

	            courseDetailsRepository.save(course); // <-- updated
	            return utils.createSuccessResponse("Course successfully updated"); // <-- updated message

	        } else {
	            // --- CREATE FLOW ---
	            boolean isCourseExist = courseDetailsRepository.findCourseExistOrNot(courseName);
	            if (isCourseExist) {
	                return utils.createErrorResponse("Course already exists");
	            }

	            CourseDetails course = new CourseDetails();
	            course.setCourseName(courseName);
	            course.setCourseDescription(data.get("courseDescription").toString());
	            course.setDuration(data.get("courseDuration").toString());
	            course.setCreatedBy(user);
	            course.setStatus(1); // active
	            course.setOrganization(organization);

	            courseDetailsRepository.save(course);
	            return utils.createSuccessResponse("Course successfully created");
	        }

	    } catch (Exception e) {
	        response.put("status", "Error");
	        response.put("message", e.getMessage());
	        return response;
	    }
	}

	
	public Map<String, Object> getAllCourses() {
	    Map<String, Object> response = new HashMap<>();
	    try {
	    	 List<CourseDTO> activeCourses = courseDetailsRepository.findAllCourseDTOs();
	        response.put("status", "Success");
	        response.put("courses", activeCourses);
	        return response;
	    } catch (Exception e) {
	        return utils.createErrorResponse("Failed to fetch courses: " + e.getMessage());
	    }
	}
	
	public Map<String, Object> deleteCourse(Long courseId) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        CourseDetails course = courseDetailsRepository.getCourseById(courseId);

	        if (course == null) {
	            return utils.createErrorResponse("Course not found or already deleted");
	        }

	        course.setStatus(0); // soft delete
	        courseDetailsRepository.save(course);

	        return utils.createSuccessResponse("Course deleted successfully");
	    } catch (Exception e) {
	        response.put("status", "Error" + e);
	        return response;
	    }
	}

}
