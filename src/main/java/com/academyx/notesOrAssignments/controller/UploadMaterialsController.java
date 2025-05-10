package com.academyx.notesOrAssignments.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.academyx.common.util.Utils;
import com.academyx.notesOrAssignments.dto.UploadRequestDTO;
import com.academyx.notesOrAssignments.service.UploadMaterialService;
import com.academyx.user.model.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class UploadMaterialsController {

	@Autowired
	private UploadMaterialService uploadMaterialService;
	
	@Autowired
	private Utils utils;
	
	@PostMapping( value = "/uploadMaterials", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> uploadNotesOrAssignments(
	    @RequestHeader("userToken") String userToken,
	    @RequestPart("data") String data, 
	    @RequestPart("files") List<MultipartFile> files) {

	    Map<String, Object> response = new HashMap<>();

	    // User validation
	    Map<String, Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    UserCredentials staff = (UserCredentials) verifiedUser.get("user");

	 // Convert JSON string to DTO manually using ObjectMapper
	    UploadRequestDTO dto;
	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        dto = mapper.readValue(data, UploadRequestDTO.class);
	    } catch (Exception e) {
	        response = utils.createErrorResponse("Invalid JSON format: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
	    // Validation
	    if (!StringUtils.hasText(dto.getTitle()) || files == null || files.isEmpty()) {
	        response = utils.createErrorResponse("Title and file to upload are required");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    dto.setFiles(files); //  inject files into DTO if service expects it

	    // Service call
	    response = uploadMaterialService.uploadMaterial(dto, staff);
	    if ("Error".equals(response.get("status"))) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    } else {
	        return ResponseEntity.ok(response);
	    }
	}

	
	@GetMapping("/getNotesOrAssignments")
	public ResponseEntity<Map<String, Object>> getNotesOrAssignments(@RequestHeader("userToken") String userToken,
																	@RequestParam("batchId") Long batchId, 
																	@RequestParam("subjectId") Long subjectId) {
		Map<String,Object> response=new HashMap<>();
		
		//validate user
		Map<String, Object> verifiedUser = utils.validateUser(userToken);
	    if (verifiedUser == null || verifiedUser.get("status").equals("Error")) {
	        response = utils.createErrorResponse("User not verified");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }

	    UserCredentials user = (UserCredentials) verifiedUser.get("user");
		
	    // Validate required parameters
	    if (batchId == null || batchId.toString().isEmpty() || subjectId == null || subjectId.toString().isEmpty()) {
	        response = utils.createErrorResponse("Batch ID and Subject ID are required");
	        return ResponseEntity.badRequest().body(response);
	    }
		
	    else {
	    	response=uploadMaterialService.getNotesOrAssignments(user,batchId,subjectId);
	    	if ("Error".equals(response.get("status"))) {
		        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		    } else {
		        return ResponseEntity.ok(response);
		    }
	    }
	}
	
}
