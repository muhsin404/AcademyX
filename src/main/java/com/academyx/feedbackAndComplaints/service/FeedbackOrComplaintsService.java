package com.academyx.feedbackAndComplaints.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.common.util.Utils;
import com.academyx.feedbackAndComplaints.dto.FeedbackOrComplaintsDTO;
import com.academyx.feedbackAndComplaints.model.FeedbackOrComplaints;
import com.academyx.feedbackAndComplaints.repository.FeedbackOrComplaintsRepository;
import com.academyx.user.model.UserCredentials;


@Service
public class FeedbackOrComplaintsService {
	
	@Autowired
    private FeedbackOrComplaintsRepository feedbackOrComplaintsRepository;

    @Autowired
    private Utils utils;

    public Map<String, Object> submitFeedbacksOrComplaints(Map<String, Object> data,UserCredentials user) {
        

        int type = Integer.parseInt(data.get("type").toString());
        if (type!=1 && type!=2) {
            return utils.createErrorResponse("Invalid type. Must be 'feedback' or 'complaint'");
        }

        FeedbackOrComplaints item = new FeedbackOrComplaints();
        item.setType(type);
        item.setSubject(data.get("subject").toString());
        item.setMessage(data.get("message").toString());
        item.setSubmittedBy(user);

        feedbackOrComplaintsRepository.save(item);

        return utils.createSuccessResponse("Submitted successfully");
    }

    public Map<String, Object> getAllFeedbacksOrComplaints() {
	    Map<String, Object> response = new HashMap<>();

        List<FeedbackOrComplaintsDTO> all = feedbackOrComplaintsRepository.findAllFeedbackOrComplaintsDTOs();
        response.put("status", "Success");
        response.put("feedbacksOrCompaints", all);
        return response;
    }


	public Map<String, Object> getFeedbackOrComplaintsOfAUser(UserCredentials user) {
		Map<String,Object> response =new HashMap<>();
		
		 List<FeedbackOrComplaintsDTO> feedbacksOrComplaintsOfUser = feedbackOrComplaintsRepository.findFeedbackOrComplaintsOfUserDTOs(user);
	        response.put("status", "Success");
	        response.put("feedbacksOrCompaints", feedbacksOrComplaintsOfUser);
	        return response;
			
		
	}

}
