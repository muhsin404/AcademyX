package com.academyx.attendance.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.attendance.dto.AttendanceRequestDTO;
import com.academyx.attendance.dto.StudentsDTO;
import com.academyx.attendance.model.AttendanceRecord;
import com.academyx.attendance.model.AttendanceSession;
import com.academyx.attendance.repository.AttendanceRecordRepository;
import com.academyx.attendance.repository.AttendanceSessionRepository;
import com.academyx.batch.model.BatchDetails;
import com.academyx.batch.repository.BatchDetailsRepository;
import com.academyx.common.util.Utils;
import com.academyx.timetable.model.TimetableEntry;
import com.academyx.timetable.repository.TimetableEntryRepository;
import com.academyx.user.model.UserCredentials;
import com.academyx.user.repository.UserCredentialsRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private BatchDetailsRepository batchRepository;

    @Autowired
    private TimetableEntryRepository timetableRepository;
    
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private Utils utils;

    public Map<String, Object> markAttendance(AttendanceRequestDTO attendanceRequest, UserCredentials user) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate batch and timetable
            Optional<BatchDetails> batchOpt = batchRepository.findById(attendanceRequest.getBatchId());
            if (batchOpt.isEmpty()) {
                return utils.createErrorResponse("Invalid batch ID");
            }

            Optional<TimetableEntry> timetableOpt = timetableRepository.findById(attendanceRequest.getTimetableEntryId());
            if (timetableOpt.isEmpty()) {
                return utils.createErrorResponse("Invalid timetable ID");
            }

            // Check if session already exists
            boolean sessionExists = attendanceSessionRepository.existsSessionByDateAndTimetableAndBatch(
                    attendanceRequest.getSessionDate(),
                    attendanceRequest.getTimetableEntryId(),
                    attendanceRequest.getBatchId()
            );
            if (sessionExists) {
                return utils.createErrorResponse("Attendance already marked for the session");
            }

            // Save attendance session
            AttendanceSession session = new AttendanceSession();
            session.setSessionDate(attendanceRequest.getSessionDate());
            session.setBatch(batchOpt.get());
            session.setTimetableEntry(timetableOpt.get());
            session.setMarkedBy(user);
            session.setMarkedAt(LocalDateTime.now());
            attendanceSessionRepository.save(session);

            // Save student attendance records
            List<AttendanceRecord> attendanceList = new ArrayList<>();
            List<String> failedStudents = new ArrayList<>();
            
            for (StudentsDTO dto : attendanceRequest.getStudentsList()) {
            	
            	UserCredentials student=userCredentialsRepository.getActiveUserById(dto.getStudentId());
            	if(student==null)
            	{
            		failedStudents.add("Student ID not found: " + dto.getStudentId());
                    continue; // Skip this student
            	}
                AttendanceRecord attendance = new AttendanceRecord();
                attendance.setSession(session);
                attendance.setStudent(student);
                attendance.setStatus(dto.getStatus());
                attendanceList.add(attendance);
            }

            if (!attendanceList.isEmpty()) {
                attendanceRecordRepository.saveAll(attendanceList);
            }

            if (!failedStudents.isEmpty()) {
                response.put("status", "Partial Success");
                response.put("message", "Attendance marked with some errors.");
                response.put("errors", failedStudents);
            } else {
                response = utils.createSuccessResponse("Attendance marked successfully");
            }

            return response;

        } catch (Exception e) {
            return utils.createErrorResponse("Error while marking attendance: " + e.getMessage());
        }
    }
}
