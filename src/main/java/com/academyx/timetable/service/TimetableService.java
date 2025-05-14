package com.academyx.timetable.service;

import com.academyx.timetable.dto.*;
import com.academyx.timetable.model.*;
import com.academyx.timetable.repository.TimetableEntryRepository;
import com.academyx.batch.model.BatchDetails;
import com.academyx.batch.repository.BatchDetailsRepository;
import com.academyx.subject.model.SubjectDetails;
import com.academyx.subject.repository.SubjectDetailsRepository;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;
import com.academyx.timetable.repository.PeriodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableService {

	@Autowired private  TimetableEntryRepository timetableRepo;
    @Autowired private  BatchDetailsRepository batchRepo;
    @Autowired private  SubjectDetailsRepository subjectRepo;
    @Autowired private  UserCredentialsRepository userRepo;
    @Autowired private  PeriodRepository periodRepo;

    // Method to create timetable
    public Map<String, Object> createTimetable( UserCredentials admin, TimetableCreateRequest request) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();

        // Fetch batch from repository
        var batch = batchRepo.findById(request.getBatchId()).orElse(null);
        if (batch == null) {
            response.put("status", "Error");
            response.put("message", "Batch not found");
            return response;
        }

        // Process each period from the request
        for (TimetablePeriodDTO dto : request.getPeriods()) {
            Map<String, Object> item = new HashMap<>();

            // Fetch period, subject, and staff
            Periods period=periodRepo.findActivePeriod(dto.getPeriodId());
            SubjectDetails subject = subjectRepo.getSubjectById(dto.getSubjectId());
            UserCredentials staff = userRepo.getActiveUserById(dto.getStaffId());

            // Check if any of the required entities are missing
            if (period == null || subject == null || staff == null) {
                item.put("status", "Error");
                item.put("message", "Invalid data provided");
                result.add(item);
                continue;
            }

            // Check for staff conflict (same period for the same staff)
            long conflict = timetableRepo.countConflictsForStaff(staff.getUserId(), request.getDayOfWeek(), period.getPeriodId());
            if (conflict > 0) {
                item.put("status", "Error");
                item.put("message", "Staff conflict for this period");
                result.add(item);
                continue;
            }

            // Check if the period already exists in the timetable for the given batch and day
            Optional<TimetableEntry> existing = timetableRepo.findExistingEntry(batch.getBatchId(), request.getDayOfWeek(), period.getPeriodId());
            TimetableEntry entry;
            if (existing.isPresent()) {
                entry = existing.get();
                entry.setSubject(subject);
                entry.setStaff(staff);
                timetableRepo.save(entry); // Update existing entry
                item.put("status", "Updated existing period");
            } else {
                entry = new TimetableEntry();
                entry.setBatch(batch);
                entry.setDayOfWeek(request.getDayOfWeek());
                entry.setPeriods(period);
                entry.setSubject(subject);
                entry.setStaff(staff);
                entry.setCreatedBy(admin);
                entry.setStatus(1); // Status set to 1 (active)
                timetableRepo.save(entry); // Save new timetable entry
                item.put("status", "Created new period");
            }

            // Add period details to the response item
            item.put("periodId", period.getPeriodId());
            item.put("periodNumber", period.getPeriodNumber());
            item.put("subject", subject.getSubjectName());
            item.put("staff", staff.getUserPersonalDetails().getFullName());
            result.add(item);
        }

        // Build final response
        response.put("status", "Success");
        response.put("data", result);
        return response;
    }
    
    //methods to define a period (period number and starting and ending time)

    public Map<String, Object> createPeriod(UserCredentials admin, LocalTime startTime, LocalTime endTime, int periodNumber) {
        Map<String, Object> response = new HashMap<>();

        // Check if a period with the given periodNumber already exists
        Optional<Periods> existingPeriodOpt = periodRepo.findByPeriodNumber(periodNumber);

        if (existingPeriodOpt.isPresent()) {
            Periods existingPeriod = existingPeriodOpt.get();
            existingPeriod.setStartTime(startTime);
            existingPeriod.setEndTime(endTime);
            periodRepo.save(existingPeriod);

            response.put("status", "Updated");
            response.put("message", "Period updated successfully");
            response.put("periodId", existingPeriod.getPeriodId());
            response.put("startTime", existingPeriod.getStartTime().toString());
            response.put("endTime", existingPeriod.getEndTime().toString());
            response.put("periodNumber", existingPeriod.getPeriodNumber());

            return response;
        }

        // Else, create a new period
        Periods newPeriod = new Periods();
        newPeriod.setStartTime(startTime);
        newPeriod.setEndTime(endTime);
        newPeriod.setPeriodNumber(periodNumber);
        periodRepo.save(newPeriod);

        response.put("status", "Success");
        response.put("message", "Period created successfully");
        response.put("periodId", newPeriod.getPeriodId());
        response.put("startTime", newPeriod.getStartTime().toString());
        response.put("endTime", newPeriod.getEndTime().toString());
        response.put("periodNumber", newPeriod.getPeriodNumber());

        return response;
    }

 // Get timetable for a batch on a specific day
    public Map<String, Object> getTimetableByBatchAndDay(Long batchId, String dayOfWeek) {
        Map<String, Object> response = new HashMap<>();

        Optional<BatchDetails> batchOpt = batchRepo.findById(batchId);
        if (batchOpt.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Batch not found");
            return response;
        }

        List<TimetableEntry> timetables = timetableRepo.findByBatchAndDayOfWeek(batchId, dayOfWeek);

        List<TimetableEntryDTO> responseDTO = timetables.stream().map(TimetableEntryDTO::fromBatchView).collect(Collectors.toList());

        response.put("status", "success");
        response.put("day", dayOfWeek);
        response.put("batchId", batchId);
        response.put("timetable", responseDTO); // list of the timetables assigned for the batch
        return response;
    }

	public Map<String , Object> getTimetableOfStaff(Long staffId, String dayOfWeek) {
		Map<String , Object> response=new HashMap<>();
		
		UserCredentials staff=userRepo.getActiveUserById(staffId);
		if(staff==null)
		{
			response.put("Status", "Error");
			response.put("message", "User not found");
			return response;
		}
		
		List<TimetableEntry> timetables=timetableRepo.findByStaffAndDayOfWeek(staffId, dayOfWeek);
		
        List<TimetableEntryDTO> responseDTO = timetables.stream().map(TimetableEntryDTO::fromStaffView).collect(Collectors.toList());

        response.put("status", "success");
        response.put("day", dayOfWeek);
        response.put("staff", staff.getUserPersonalDetails().getFullName());
        response.put("timetable", responseDTO); // list of the timetables assigned for the batch
        return response;
	}


}
