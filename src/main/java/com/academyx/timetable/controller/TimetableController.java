package com.academyx.timetable.controller;

import com.academyx.common.util.Utils;
import com.academyx.timetable.dto.TimetableCreateRequest;
import com.academyx.timetable.model.Periods;
import com.academyx.timetable.model.TimetableEntry;
import com.academyx.timetable.repository.PeriodRepository;
import com.academyx.timetable.service.TimetableService;
import com.academyx.user.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TimetableController {

    @Autowired private TimetableService timetableService;
    @Autowired private Utils utils;
    @Autowired private PeriodRepository periodRepository;
    
    
    @PostMapping("/createPeriod")
    public ResponseEntity<Map<String, Object>> createPeriod(
            @RequestHeader("userToken") String userToken,
            @RequestBody HashMap<String, Object> period) {

        Map<String, Object> response = new HashMap<>();

        // 1. Validate user
        var validUser = utils.validateUser(userToken);
        if (validUser == null || "Error".equals(validUser.get("status"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(utils.createErrorResponse("User not verified"));
        }

        UserCredentials admin = (UserCredentials) validUser.get("user");

        try {
            // 2. Extract and parse times
            LocalTime startTime = LocalTime.parse((String) period.get("startTime"));
            LocalTime endTime = LocalTime.parse((String) period.get("endTime"));
            Integer periodNumber = Integer.parseInt(period.get("periodNumber").toString());

            // 3. Validate
            if (startTime.isAfter(endTime)) {
                response.put("status", "Error");
                response.put("message", "Start time must be before end time");
                return ResponseEntity.badRequest().body(response);
            }

            // 4. Delegate to service
            response = timetableService.createPeriod(admin, startTime, endTime, periodNumber);
            HttpStatus status = "Error".equals(response.get("status")) ? HttpStatus.CONFLICT : HttpStatus.OK;
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", "Invalid data format: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    

    @PostMapping("/createTimetable")
    public ResponseEntity<Map<String,Object>> createForDay(
            @RequestHeader("userToken") String userToken,
            @RequestBody TimetableCreateRequest request) {

        var validUser = utils.validateUser(userToken);
        if (validUser==null || "Error".equals(validUser.get("status"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(utils.createErrorResponse("User not verified"));
        }
        UserCredentials admin = (UserCredentials) validUser.get("user");
        var response = timetableService.createTimetable(admin, request);
        HttpStatus status = "Error".equals(response.get("status")) ? HttpStatus.CONFLICT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    
    @GetMapping("/getTimetableOfBatch")
    public ResponseEntity<Map<String, Object>> getTimetableOfABatch(@RequestHeader("userToken") String userToken,
            @RequestParam("batchId") Long batchId, @RequestParam("dayOfWeek") String dayOfWeek) {
    	
    	var validUser = utils.validateUser(userToken);
        if (validUser==null || "Error".equals(validUser.get("status"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(utils.createErrorResponse("User not verified"));
        }
        var response= timetableService.getTimetableByBatchAndDay(batchId, dayOfWeek);
        HttpStatus status = "Error".equals(response.get("status")) ? HttpStatus.CONFLICT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
    
    @GetMapping("/getTimetableOfAStaff")
    public ResponseEntity<Map<String, Object>> getTimetableOfAStaff(@RequestHeader("userToken") String userToken,
    		 @RequestParam("staffId") Long staffId, @RequestParam("dayOfWeek") String dayOfWeek) {
    	
    	var validUser = utils.validateUser(userToken);
        if (validUser==null || "Error".equals(validUser.get("status"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(utils.createErrorResponse("User not verified"));
        }
        
        var response= timetableService.getTimetableOfStaff(staffId, dayOfWeek);
        HttpStatus status = "Error".equals(response.get("status")) ? HttpStatus.CONFLICT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
    
}