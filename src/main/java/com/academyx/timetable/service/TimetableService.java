package com.academyx.timetable.service;

import com.academyx.timetable.dto.*;
import com.academyx.timetable.model.TimetableEntry;
import com.academyx.timetable.repository.TimetableEntryRepository;
import com.academyx.batch.repository.BatchDetailsRepository;
import com.academyx.subject.repository.SubjectDetailsRepository;
import com.academyx.user.repository.UserCredentialsRepository;
import com.academyx.common.util.Utils;
import com.academyx.user.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Service
public class TimetableService {
    @Autowired private TimetableEntryRepository timetableRepo;
    @Autowired private BatchDetailsRepository batchRepo;
    @Autowired private SubjectDetailsRepository subjectRepo;
    @Autowired private UserCredentialsRepository userRepo;
    @Autowired private Utils utils;

    public Map<String,Object> createTimetableForDay(UserCredentials admin, TimetableDayRequest request) {
        Map<String,Object> response = new HashMap<>();
        var batch = batchRepo.getBatchById(request.getBatchId());
        if (batch == null) return utils.createErrorResponse("Batch not found");

        List<Map<String,Object>> results = new ArrayList<>();
        for (PeriodDTO period : request.getPeriods()) {
            Map<String,Object> single = new HashMap<>();
            try {
                // Fetch subject and staff
                var subject = subjectRepo.getSubjectById(period.getSubjectId());
                var staff   = userRepo.getActiveUserById(period.getStaffId());
                if (subject == null) return utils.createErrorResponse("Subject not found: " + period.getSubjectId());
                if (staff   == null) return utils.createErrorResponse("Staff not found: "   + period.getStaffId());

                LocalTime startTime = LocalTime.parse(period.getStartTime());
                LocalTime endTime   = LocalTime.parse(period.getEndTime());

                // 1) Check staff conflict across batches
                long conflicts = timetableRepo.countOverlappingStaffPeriods(
                        period.getStaffId(),
                        request.getDayOfWeek(),
                        startTime,
                        endTime
                );
                if (conflicts > 0) {
                    single.put("status","Error");
                    single.put("message","Staff is already assigned during this time slot");
                    results.add(single);
                    continue;
                }

                // 2) Check duplicate period for this batch
                var existing = timetableRepo.findByBatchIdAndTimeSlot(
                        request.getBatchId(),
                        request.getDayOfWeek(),
                        startTime,
                        endTime
                );
                if (existing.isPresent()) {
                    // update existing entry
                    TimetableEntry entry = existing.get();
                    entry.setSubject(subject);
                    entry.setStaff(staff);
                    timetableRepo.save(entry);
                    single.put("status","Updated");
                    single.put("message","Existing period updated");
                } else {
                    // 3) Create new entry
                    TimetableEntry entry = new TimetableEntry();
                    entry.setBatch(batch);
                    entry.setDayOfWeek(request.getDayOfWeek());
                    entry.setStartTime(startTime);
                    entry.setEndTime(endTime);
                    entry.setSubject(subject);
                    entry.setStaff(staff);
                    entry.setStatus(1);
                    timetableRepo.save(entry);
                    single.put("status","Created");
                }

                single.put("startTime", period.getStartTime());
                single.put("endTime",   period.getEndTime());
                single.put("subject",    subject.getSubjectName());
                single.put("staff",      staff.getUserPersonalDetails().getFullName());
            } catch (Exception ex) {
                single.put("status","Error");
                single.put("message", ex.getMessage());
            }
            results.add(single);
        }

        response.put("status","Success");
        response.put("periods", results);
        return response;
    }

//    public Map<String,Object> getTimetableForBatch(Long batchId) {
//        Map<String,Object> response = new HashMap<>();
//        var batch = batchRepo.getBatchById(batchId);
//        if (batch == null) return utils.createErrorResponse("Batch not found");
//
//        List<TimetableEntryDTO> list = timetableRepo.findByBatchIdOrderByDayTime(batchId);
//        response.put("status","Success");
//        response.put("batch", batch.getBatchName());
//        response.put("timetable", list);
//        return response;
//    }
    
    
    public Map<String, Object> getTimetableForBatch(Long batchId) {
        Map<String, Object> response = new HashMap<>();

        var batch = batchRepo.getBatchById(batchId);
        if (batch == null) return utils.createErrorResponse("Batch not found");

        List<TimetableEntryDTO> list = timetableRepo.findByBatchIdOrderByDayTime(batchId);

        // Initialize timetable map with all days of the week
        Map<String, List<Map<String, Object>>> timetableMap = new LinkedHashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetableMap.put(day.name(), new ArrayList<>());
        }

        // Group timetable entries by day, excluding dayOfWeek from individual entries
        for (TimetableEntryDTO entry : list) {
            String day = entry.getDayOfWeek().toUpperCase(); // e.g., "MONDAY"

            Map<String, Object> entryMap = new HashMap<>();
            entryMap.put("startTime", entry.getStartTime());
            entryMap.put("endTime", entry.getEndTime());
            entryMap.put("subjectName", entry.getSubjectName());
            entryMap.put("staffName", entry.getStaffName());

            timetableMap.get(day).add(entryMap);
        }

        response.put("status", "Success");
        response.put("batch", batch.getBatchName());
        response.put("timetable", timetableMap);

        return response;
    }


}
