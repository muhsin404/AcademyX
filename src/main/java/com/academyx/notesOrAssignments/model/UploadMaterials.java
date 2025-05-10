package com.academyx.notesOrAssignments.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.academyx.batch.model.BatchDetails;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.user.model.UserCredentials;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadMaterials {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int uploadType;  // 1- notes  and 2- assignments 

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uploadedBy", referencedColumnName = "userId")
	private UserCredentials staff;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectId", referencedColumnName = "subjectId")
	private SubjectDetails subject;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batchId", referencedColumnName = "batchId")
	private BatchDetails batch;

    private String filePath;

    private LocalDateTime uploadedAt;

    private LocalDate dueDate; // Only for assignments

    private boolean submissionAllowed; // Only for assignments

}
