package com.academyx.timetable.controller;

import com.academyx.common.util.Utils;
import com.academyx.timetable.dto.TimetableDayRequest;
import com.academyx.timetable.service.TimetableService;
import com.academyx.user.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TimetableController {

    @Autowired private TimetableService timetableService;
    @Autowired private Utils utils;

    @PostMapping("/createTimetableForDay")
    public ResponseEntity<Map<String,Object>> createForDay(
            @RequestHeader("userToken") String userToken,
            @RequestBody TimetableDayRequest request) {

        var validUser = utils.validateUser(userToken);
        if (validUser==null || "Error".equals(validUser.get("status"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(utils.createErrorResponse("User not verified"));
        }
        UserCredentials admin = (UserCredentials) validUser.get("user");
        var response = timetableService.createTimetableForDay(admin, request);
        HttpStatus status = "Error".equals(response.get("status")) ? HttpStatus.CONFLICT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/getTimetableOFBatch")
    public ResponseEntity<Map<String,Object>> getByBatch(
            @RequestHeader("userToken") String userToken,
            @RequestParam("batchId") Long batchId) {

        var validUser = utils.validateUser(userToken);
        if (validUser==null || "Error".equals(validUser.get("status"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(utils.createErrorResponse("User not verified"));
        }
        var response = timetableService.getTimetableForBatch(batchId);
        HttpStatus status = "Error".equals(response.get("status")) ? HttpStatus.CONFLICT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
}
