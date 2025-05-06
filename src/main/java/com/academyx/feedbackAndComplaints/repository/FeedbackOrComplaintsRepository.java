package com.academyx.feedbackAndComplaints.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.feedbackAndComplaints.dto.FeedbackOrComplaintsDTO;
import com.academyx.feedbackAndComplaints.model.FeedbackOrComplaints;
import com.academyx.subject.dto.SubjectDTO;
import com.academyx.user.model.UserCredentials;

public interface FeedbackOrComplaintsRepository extends JpaRepository<FeedbackOrComplaints, Long>{

	@Query("SELECT new com.academyx.feedbackAndComplaints.dto.FeedbackOrComplaintsDTO(f) FROM FeedbackOrComplaints f")
    List<FeedbackOrComplaintsDTO> findAllFeedbackOrComplaintsDTOs();
	
	@Query("SELECT new com.academyx.feedbackAndComplaints.dto.FeedbackOrComplaintsDTO(f) FROM FeedbackOrComplaints f where f.submittedBy = :user")
    List<FeedbackOrComplaintsDTO> findFeedbackOrComplaintsOfUserDTOs(@Param("user") UserCredentials user);
}
