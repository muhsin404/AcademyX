package com.academyx.notesOrAssignments.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestDTO {
    private String title;
    private String description;
    private int uploadType;
    private Long subjectId;
    private Long batchId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private boolean submissionAllowed;
    private List<MultipartFile> files;
}
