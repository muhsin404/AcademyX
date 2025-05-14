package com.academyx.batch.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.batch.dto.AssignedUserDTO;
import com.academyx.batch.dto.BatchDTO;
import com.academyx.batch.dto.UserDTO;
import com.academyx.batch.model.BatchDetails;
import com.academyx.batch.model.UserBatchRelation;
import com.academyx.batch.repository.BatchDetailsRepository;
import com.academyx.batch.repository.UserBatchRelationRepository;
import com.academyx.common.util.Utils;
import com.academyx.course.model.CourseDetails;
import com.academyx.organization.model.Organizations;
import com.academyx.organization.repository.OrganizationRepository;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;

@Service
public class BatchService {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private BatchDetailsRepository batchDetailsRepository;
	
	@Autowired
	private UserBatchRelationRepository userBatchRelationRepository;
	
	@Autowired
	private UserCredentialsRepository userCredentialsRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;

	public Map<String, Object> createBatchDetails(UserCredentials user, HashMap<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		
		System.out.println(data);
		try {
			
			Long orgId = Long.parseLong(data.get("organizationId").toString());
			Organizations organization = organizationRepository.getOrganizationByOrganizationId(orgId);
			if (organization == null) {
				return utils.createErrorResponse("Invalid organization ID");
			}
			
		String batchName=data.get("batchName").toString().trim();
		
		boolean isBatchExist = batchDetailsRepository.findBatchExistOrNot(batchName);
		if(isBatchExist)
		{
			return utils.createErrorResponse("batch already exist");
		}
		else
		{
			BatchDetails batch=new BatchDetails();
			batch.setBatchName(batchName);
			batch.setBatchDescription(data.get("batchDescription").toString());
			
			String startDateStr = data.get("batchStartingDate").toString();
			String endDateStr = data.get("batchEndingDate").toString();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			LocalDate batchStartingDate = LocalDate.parse(startDateStr, formatter);
			LocalDate batchEndingDate = LocalDate.parse(endDateStr, formatter);
			
			batch.setStartingDate(batchStartingDate);
			batch.setEndingDate(batchEndingDate);
			batch.setStatus(1);//active status
			batch.setCreatedBy(user);
			batch.setOrganization(organization);
			batchDetailsRepository.save(batch);
			
			return utils.createSuccessResponse("successfully created batch");
			
		}
		}catch (Exception e) {
			response.put("status", "Error" + e);
			return response;
		}
		
	}
	
	
	public Map<String, Object> getBatches(Long batchId) {
	    Map<String, Object> resp = new HashMap<>();
	    try {
	        if (batchId == null) {
	            // ——— return all batches ———
	            List<BatchDTO> all = batchDetailsRepository.findAllBatchDTOs();
	            resp.put("status", "Success");
	            resp.put("batches", all);
	        } else {
	            // ——— return one batch + assigned users ———
	            BatchDetails batch = batchDetailsRepository.getBatchById(batchId);
	            if (batch == null) {
	                return utils.createErrorResponse("Batch not found");
	            }

	            // fetch assigned users with their role flagged
	            List<AssignedUserDTO> assigned =
	              userBatchRelationRepository.findAssignedByBatchId(
	                    batchId, batch.getOrganization().getOrganizationId()
	              );

	            // split into students vs staffs
	            Map<Boolean, List<Long>> partitioned = assigned.stream()
	                .collect(Collectors.partitioningBy(
	                    dto -> "student".equals(dto.getAssignmentStatus()),              // predicate
	                    Collectors.mapping(AssignedUserDTO::getUserId, Collectors.toList())  // downstream
	                ));

	            List<Long> students = partitioned.get(true);
	            List<Long> staffs   = partitioned.get(false);

	            // build batch-detail payload
	            Map<String,Object> batchMap = new HashMap<>();
	            batchMap.put("batchId",          batch.getBatchId());
	            batchMap.put("batchName",        batch.getBatchName());
	            batchMap.put("batchDescription", batch.getBatchDescription());
	            batchMap.put("startingDate",     batch.getStartingDate());
	            batchMap.put("endingDate",       batch.getEndingDate());
	            batchMap.put("students",         students);
	            batchMap.put("staffs",           staffs);

	            resp.put("status", "Success");
	            resp.put("batch", batchMap);
	        }
	        return resp;
	    } catch (Exception e) {
	        return utils.createErrorResponse("Failed to fetch batches: " + e.getMessage());
	    }
	}



	public Map<String, Object> assignUserToBatch(UserCredentials adminUser, HashMap<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        Long batchId        = Long.parseLong(data.get("batchId").toString());
        Long organizationId = Long.parseLong(data.get("organizationId").toString());
        List<Long> userIds  = ((List<?>)data.get("userIds")).stream()
                                 .map(id -> Long.parseLong(id.toString()))
                                 .collect(Collectors.toList());

        // 4.a Validate batch & organization
        BatchDetails batch = batchDetailsRepository.getBatchById(batchId);
        if (batch == null) return utils.createErrorResponse("Batch not found");
        Organizations organization = organizationRepository.getOrganizationByOrganizationId(organizationId);
        if (organization == null) return utils.createErrorResponse("Organization not found");

        // 4.b Fetch already assigned users via DTO projection
        List<AssignedUserDTO> assignedDTOs =
            userBatchRelationRepository.findByUserIdInAndBatchIdAndOrganizationId(userIds, batchId, organizationId);
        Set<Long> alreadyAssignedUserIds = assignedDTOs.stream()
                                                      .map(AssignedUserDTO::getUserId)
                                                      .collect(Collectors.toSet());

        // 4.c Enforce “one batch per organization” rule for students
        List<Long> toAssign = new ArrayList<>();
        for (Long userId : userIds) {
            UserCredentials user = userCredentialsRepository.getActiveUserById(userId);
            if (user == null) return utils.createErrorResponse("User not found: " + userId);

            if (user.getRole() == 3) {  // if user is student
                boolean existsInOrg = !userBatchRelationRepository
                                         .findByUserIdAndOrganizationId(userId, organizationId)
                                         .isEmpty();
                if (existsInOrg && !alreadyAssignedUserIds.contains(userId)) {
                    return utils.createErrorResponse("The student with ID " + userId + " is already assigned to another batch.");
                }
            }
            if (!alreadyAssignedUserIds.contains(userId)) {
                toAssign.add(userId);
            }
        }

        // 4.d Create new relations & add to DTO list
        for (Long userId : toAssign) {
            UserBatchRelation relation = new UserBatchRelation();
            relation.setBatchDetails(batch);
            relation.setOrganization(organization);
            relation.setUserCredentials(userCredentialsRepository.getActiveUserById(userId));
            relation.setStatus(1);
            userBatchRelationRepository.save(relation);

            assignedDTOs.add(new AssignedUserDTO(userId, "newlyAssigned"));
        }

        // 4.e Build response lists
        List<Long> alreadyAssignedList = assignedDTOs.stream()
                                             .filter(d -> "alreadyAssigned".equals(d.getAssignmentStatus()))
                                             .map(AssignedUserDTO::getUserId)
                                             .toList();
        List<Long> newlyAssignedList   = assignedDTOs.stream()
                                             .filter(d -> "newlyAssigned".equals(d.getAssignmentStatus()))
                                             .map(AssignedUserDTO::getUserId)
                                             .toList();

        response.put("status", "Success");
        response.put("alreadyAssignedUsers", alreadyAssignedList);
        response.put("newlyAssignedUsers", newlyAssignedList);
        return response;
    }


	public Map<String, Object> deleteBatch(Long batchId) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        BatchDetails batch = batchDetailsRepository.getBatchById(batchId);

	        if (batch == null) {
	            return utils.createErrorResponse("Batch not found or already deleted");
	        }

	        batch.setStatus(0); // soft delete
	        batchDetailsRepository.save(batch);

	        return utils.createSuccessResponse("Batch deleted successfully");
	    } catch (Exception e) {
	        response.put("status", "Error" + e);
	        return response;
	    }
	}


	public Map<String, Object> getUsersOfABatch(Long batchId) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        BatchDetails batch = batchDetailsRepository.getBatchById(batchId);

	        if (batch == null) {
	            return utils.createErrorResponse("Batch not found");
	        }

	        List<UserDTO> students = userBatchRelationRepository.findStudentsByBatchId(batchId);
	        List<UserDTO> staffs = userBatchRelationRepository.findStaffByBatchId(batchId);

	        boolean hasStudents = students != null && !students.isEmpty();
	        boolean hasStaffs = staffs != null && !staffs.isEmpty();

	        if (!hasStudents && !hasStaffs) {
	            return utils.createErrorResponse("No users assigned to this batch");
	        }

	        response.put("status", "Partial Success");

	        if (hasStudents) {
	            response.put("students", students);
	        } else {
	            response.put("studentMessage", "No students assigned to this batch");
	        }

	        if (hasStaffs) {
	            response.put("staffs", staffs);
	        } else {
	            response.put("staffMessage", "No staffs assigned to this batch");
	        }

	        // If both groups are present, mark as full success
	        if (hasStudents && hasStaffs) {
	            response.put("status", "Success");
	        }

	        return response;

	    } catch (Exception e) {
	        response.put("status", "Error");
	        response.put("message", e.getMessage());
	        return response;
	    }
	}

	
}
