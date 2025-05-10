package com.academyx.notesOrAssignments.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.academyx.batch.model.BatchDetails;
import com.academyx.batch.repository.BatchDetailsRepository;
import com.academyx.batch.repository.UserBatchRelationRepository;
import com.academyx.common.util.Utils;
import com.academyx.notesOrAssignments.dto.UploadRequestDTO;
import com.academyx.notesOrAssignments.model.UploadMaterials;
import com.academyx.notesOrAssignments.repository.UploadMaterialsRepository;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.subject.repository.SubjectDetailsRepository;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;

@Service
public class UploadMaterialService {

    @Autowired
    private UploadMaterialsRepository materialsRepository;

    @Autowired
    private SubjectDetailsRepository subjectRepo;

    @Autowired
    private BatchDetailsRepository batchRepo;

    @Autowired
    private UserCredentialsRepository userRepo;
    
    @Autowired
    private UserBatchRelationRepository batchRelationRepository;

    @Autowired
    private Utils utils;

//    @Value("${file.upload-dir}")
//    private String uploadDir;

    public Map<String, Object> uploadMaterial(UploadRequestDTO dto, UserCredentials staff) {
        Map<String, Object> response = new HashMap<>();

        // Validate subject
        SubjectDetails subject = subjectRepo.getSubjectById(dto.getSubjectId());
        if (subject == null) {
            return utils.createErrorResponse("Subject not found");
        }

        // Validate batch
        BatchDetails batch = batchRepo.getBatchById(dto.getBatchId());
        if (batch == null) {
            return utils.createErrorResponse("Batch not found");
        }

        List<UploadMaterials> materialsList = new ArrayList<>();

        try {
            Path baseUploadPath = Paths.get("C:\\Users\\User\\OneDrive\\Desktop\\LMS-PROJECT\\fileUpload");
            if (!Files.exists(baseUploadPath)) {
                Files.createDirectories(baseUploadPath);
            }

            for (MultipartFile file : dto.getFiles()) {
                String originalFileName = file.getOriginalFilename();
                String extension = "";

                if (originalFileName != null && originalFileName.contains(".")) {
                    extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }

                String storedFileName = UUID.randomUUID().toString() + extension;
                Path filePath = baseUploadPath.resolve(storedFileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                UploadMaterials material = new UploadMaterials();
                        material.setTitle(dto.getTitle());
                        material.setDescription(dto.getDescription());
                        material.setUploadType(dto.getUploadType());
                        material.setStaff(staff);
                        material.setSubject(subject);
                        material.setBatch(batch);
                        material.setFilePath(filePath.toString());
                        material.setUploadedAt(LocalDateTime.now());
                        material.setDueDate(dto.getDueDate());
                        material.setSubmissionAllowed(dto.isSubmissionAllowed());

                materialsList.add(material);
            }

            materialsRepository.saveAll(materialsList);

            response.put("status", "Success");
            response.put("message", "Files uploaded successfully");
            response.put("uploadedCount", materialsList.size());

            return response;

        } catch (IOException e) {
            response = utils.createErrorResponse("File upload failed: " + e.getMessage());
            return response;
        }
    }

    public Map<String, Object> getNotesOrAssignments(UserCredentials user, Long batchId, Long subjectId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate subject
            SubjectDetails subject = subjectRepo.getSubjectById(subjectId);
            if (subject == null) {
                return utils.createErrorResponse("Subject not found");
            }

            // Validate batch
            BatchDetails batch = batchRepo.getBatchById(batchId);
            if (batch == null) {
                return utils.createErrorResponse("Batch not found");
            }

            // If user is a student, verify they belong to the batch
            if (user.getRole() == 3) { // 3 = student
                boolean isStudentInBatch = batchRelationRepository.existsByUserIdAndBatchId(user.getUserId(), batchId);
                if (!isStudentInBatch) {
                    return utils.createErrorResponse("You are not assigned to this batch");
                }
            }

            // Fetch notes/assignments
            List<UploadMaterials> notesList = materialsRepository.findNotesORAssignmentsByBatchIdAndSubjectId(batchId, subjectId);

            if (notesList == null || notesList.isEmpty()) {
                return utils.createErrorResponse("No notes or assignments found for this batch and subject");
            }

            // Map data
            List<Map<String, Object>> dataList = notesList.stream().map(note -> {
                Map<String, Object> noteMap = new HashMap<>();
                noteMap.put("title", note.getTitle());
                noteMap.put("description", note.getDescription());
                noteMap.put("filePath", note.getFilePath());
                noteMap.put("type", note.getUploadType()); // e.g., "NOTE" or "ASSIGNMENT"
                if (note.getUploadType() == 2) {
                    noteMap.put("dueDate", note.getDueDate());
                    noteMap.put("canSubmit", note.isSubmissionAllowed());
                }
                return noteMap;
            }).collect(Collectors.toList());

            response.put("status", "Success");
            response.put("data", dataList);
            return response;

        } catch (Exception e) {
            e.printStackTrace(); // Optional: replace with logger.error(...) in production
            return utils.createErrorResponse("Something went wrong: " + e.getMessage());
        }
    }

}
