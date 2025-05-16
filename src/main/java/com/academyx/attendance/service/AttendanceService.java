package com.academyx.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academyx.attendance.dto.AttendanceRequestDTO;
import com.academyx.attendance.dto.StudentAttendanceDTO;
import com.academyx.attendance.dto.StudentsDTO;
import com.academyx.attendance.model.AttendanceRecord;
import com.academyx.attendance.model.AttendanceSession;
import com.academyx.attendance.repository.AttendanceRecordRepository;
import com.academyx.attendance.repository.AttendanceSessionRepository;
import com.academyx.batch.model.BatchDetails;
import com.academyx.batch.repository.BatchDetailsRepository;
import com.academyx.batch.repository.UserBatchRelationRepository;
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
	private UserBatchRelationRepository userBatchRelationRepository;

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

			Optional<TimetableEntry> timetableOpt = timetableRepository
					.findById(attendanceRequest.getTimetableEntryId());
			if (timetableOpt.isEmpty()) {
				return utils.createErrorResponse("Invalid timetable ID");
			}

			// Check if session already exists
			boolean sessionExists = attendanceSessionRepository.existsSessionByDateAndTimetableAndBatch(
					attendanceRequest.getSessionDate(), attendanceRequest.getTimetableEntryId(),
					attendanceRequest.getBatchId());
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

				UserCredentials student = userCredentialsRepository.getActiveUserById(dto.getStudentId());
				if (student == null) {
					failedStudents.add("Student ID not found: " + dto.getStudentId());
					continue; // Skip this student
				}
				// Check if student belongs to the batch
			    boolean isStudentInBatch = userBatchRelationRepository
			        .existsByUserIdAndBatchId(dto.getStudentId(), attendanceRequest.getBatchId());

			    if (!isStudentInBatch) {
			        failedStudents.add("Student does not belong to batch: " + dto.getStudentId());
			        continue;
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
	
	
	

//	public Map<String, Object> getStudentAttendanceByDate(Long studentId, LocalDate sessionDate, LocalDate startDate,
//			LocalDate endDate, Long subjectId) {
//		Map<String, Object> response = new HashMap<>();
//
//		if (studentId == null || studentId <= 0) {
//			response.put("status", "Error");
//			response.put("message", "Invalid student ID");
//			return response;
//		}
//
//		List<AttendanceRecord> records = new ArrayList<>();
//
//		try {
//			if (startDate != null && endDate != null) {
//				if (endDate.isBefore(startDate)) {
//					response.put("status", "Error");
//					response.put("message", "Invalid date range");
//					return response;
//				}
//
//				if (subjectId != null) {
//					records = attendanceRecordRepository.findByStudentAndDateRangeAndSubject(studentId, startDate,
//							endDate, subjectId);
//				} else {
//					records = attendanceRecordRepository.findByStudentAndDateBetween(studentId, startDate, endDate);
//				}
//			} else {
//				if (sessionDate == null)
//					sessionDate = LocalDate.now();
//
//				if (subjectId != null) {
//					records = attendanceRecordRepository.findByStudentAndDateAndSubject(studentId, sessionDate,
//							subjectId);
//				} else {
//					records = attendanceRecordRepository.findByStudentAndDate(studentId, sessionDate);
//				}
//			}
//
//			if (records.isEmpty()) {
//				response.put("status", "Error");
//				response.put("message", "No attendance records found for the given criteria.");
//				return response;
//			}
//
//			List<StudentAttendanceDTO> attendanceDTOList = records.stream().map(record -> {
//				var session = record.getSession();
//				var entry = session.getTimetableEntry();
//				return new StudentAttendanceDTO(session.getSessionId(), session.getSessionDate(), record.getStatus(),
//						entry.getSubject().getSubjectName(), entry.getPeriods().getStartTime(),
//						entry.getPeriods().getEndTime(), entry.getPeriod().getPeriodNumber());
//			}).collect(Collectors.toList());
//
//			response.put("status", "Success");
//			response.put("message", "Attendance records retrieved successfully");
//			response.put("attendance", attendanceDTOList);
//
//		} catch (Exception e) {
//			response.put("status", "Error");
//			response.put("message", "An error occurred while retrieving attendance: " + e.getMessage());
//		}
//
//		return response;
//	}

	
	//method to get the attendance of a student based on filters - of a subject using subjectId (of a date / in between date range)
		// without subject whole attendance of a date or in between date range
	
//	public Map<String, Object> getStudentAttendance(Long studentId, LocalDate sessionDate, LocalDate startDate,
//			LocalDate endDate, Long subjectId) {
//	Map<String, Object> response = new HashMap<>();
//
//	if (studentId == null || studentId <= 0) {
//		response.put("status", "Error");
//		response.put("message", "Invalid student ID");
//		return response;
//	}
//
//	List<AttendanceRecord> records = new ArrayList<>();
//
//	try {
//		if (startDate != null && endDate != null) {
//			if (endDate.isBefore(startDate)) {
//				response.put("status", "Error");
//				response.put("message", "Invalid date range");
//				return response;
//			}
//
//			if (subjectId != null) {
//				records = attendanceRecordRepository.findByStudentAndDateRangeAndSubject(studentId, startDate, endDate, subjectId);
//			} else {
//				records = attendanceRecordRepository.findByStudentAndDateBetween(studentId, startDate, endDate);
//			}
//		} else {
//			if (sessionDate == null)
//				sessionDate = LocalDate.now();
//
//			if (subjectId != null) {
//				records = attendanceRecordRepository.findByStudentAndDateAndSubject(studentId, sessionDate, subjectId);
//			} else {
//				records = attendanceRecordRepository.findByStudentAndDate(studentId, sessionDate);
//			}
//		}
//
//		if (records.isEmpty()) {
//			response.put("status", "Error");
//			response.put("message", "No attendance records found for the given criteria.");
//			return response;
//		}
//
//		// Map attendance records to DTOs
//		List<StudentAttendanceDTO> attendanceDTOList = records.stream().map(record -> {
//			var session = record.getSession();
//			var entry = session.getTimetableEntry();
//			return new StudentAttendanceDTO(
//					session.getSessionId(),
//					session.getSessionDate(),
//					record.getStatus(),
//					entry.getSubject().getSubjectName(),
//					entry.getPeriods().getStartTime(),
//					entry.getPeriods().getEndTime(),
//					entry.getPeriod().getPeriodNumber()
//			);
//		}).collect(Collectors.toList());
//
//		// Daily Attendance Summary Logic
//		// Group attendance by session date
//		Map<LocalDate, List<AttendanceRecord>> groupedByDate = records.stream()
//			    .collect(Collectors.groupingBy(r -> r.getSession().getSessionDate()));
//
//		// Summary only for date range
//		if (startDate != null && endDate != null) {
//			long totalWorkingDays = groupedByDate.keySet().size();
//			long daysPresent = 0, daysAbsent = 0, daysLeave = 0;
//
//			for (List<AttendanceRecord> dailyRecords : groupedByDate.values()) {
//			    boolean hasPresent = dailyRecords.stream().anyMatch(r -> "PRESENT".equalsIgnoreCase(r.getStatus()));
//			    boolean allLeave = dailyRecords.stream().allMatch(r -> 
//			        "LEAVE".equalsIgnoreCase(r.getStatus()) || "MEDICAL_LEAVE".equalsIgnoreCase(r.getStatus()));
//
//			    if (hasPresent) {
//			        daysPresent++;
//			    } else if (allLeave) {
//			        daysLeave++;
//			    } else {
//			        daysAbsent++;
//			    }
//			}
//
//			double attendancePercentage = totalWorkingDays == 0 ? 0.0 : (daysPresent * 100.0) / totalWorkingDays;
//
//			Map<String, Object> summary = new HashMap<>();
//			summary.put("totalWorkingDays", totalWorkingDays);
//			summary.put("daysPresent", daysPresent);
//			summary.put("daysAbsent", daysAbsent);
//			summary.put("daysLeave", daysLeave);
//			summary.put("attendancePercentage", attendancePercentage);
//
//			response.put("summary", summary);
//		}
//
//		// Final response
//		response.put("status", "Success");
//		response.put("message", "Attendance records retrieved successfully");
//		response.put("attendance", attendanceDTOList);
//
//
//	} catch (Exception e) {
//		response.put("status", "Error");
//		response.put("message", "An error occurred while retrieving attendance: " + e.getMessage());
//	}
//
//	return response;
//}
	
	public Map<String, Object> getStudentAttendance(Long studentId, LocalDate sessionDate, LocalDate startDate,
	        LocalDate endDate, Long subjectId) {

	    Map<String, Object> response = new HashMap<>();

	    if (studentId == null || studentId <= 0) {
	        response.put("status", "Error");
	        response.put("message", "Invalid student ID");
	        return response;
	    }

	    List<AttendanceRecord> records = new ArrayList<>();

	    try {
	        // Fetch records based on the input parameters
	        if (startDate != null && endDate != null) {
	            if (endDate.isBefore(startDate)) {
	                response.put("status", "Error");
	                response.put("message", "Invalid date range");
	                return response;
	            }

	            if (subjectId != null) {
	                records = attendanceRecordRepository.findByStudentAndDateRangeAndSubject(studentId, startDate, endDate, subjectId);
	            } else {
	                records = attendanceRecordRepository.findByStudentAndDateBetween(studentId, startDate, endDate);
	            }
	        } else {
	            if (sessionDate == null)
	                sessionDate = LocalDate.now();

	            if (subjectId != null) {
	                records = attendanceRecordRepository.findByStudentAndDateAndSubject(studentId, sessionDate, subjectId);
	            } else {
	                records = attendanceRecordRepository.findByStudentAndDate(studentId, sessionDate);
	            }
	        }

	        if (records.isEmpty()) {
	            response.put("status", "Error");
	            response.put("message", "No attendance records found for the given criteria.");
	            return response;
	        }

	        // Map attendance records to DTOs
	        List<StudentAttendanceDTO> attendanceDTOList = records.stream().map(record -> {
	            var session = record.getSession();
	            var entry = session.getTimetableEntry();
	            return new StudentAttendanceDTO(
	                    session.getSessionId(),
	                    session.getSessionDate(),
	                    record.getStatus(),
	                    entry.getSubject().getSubjectName(),
	                    entry.getPeriods().getStartTime(),
	                    entry.getPeriods().getEndTime(),
	                    entry.getPeriod().getPeriodNumber()
	            );
	        }).collect(Collectors.toList());

	        // Group DTOs by date (String version of LocalDate for JSON key)
	        Map<String, List<StudentAttendanceDTO>> groupedAttendance = attendanceDTOList.stream()
	                .collect(Collectors.groupingBy(dto -> dto.getSessionDate().toString(), LinkedHashMap::new, Collectors.toList()));

	        // Summary calculation if it's a range
	        if (startDate != null && endDate != null) {
	            Map<LocalDate, List<AttendanceRecord>> groupedByDate = records.stream()
	                    .collect(Collectors.groupingBy(r -> r.getSession().getSessionDate()));

	            long totalWorkingDays = groupedByDate.keySet().size();
	            long daysPresent = 0, daysAbsent = 0, daysLeave = 0;

	            for (List<AttendanceRecord> dailyRecords : groupedByDate.values()) {
	                boolean hasPresent = dailyRecords.stream().anyMatch(r -> "PRESENT".equalsIgnoreCase(r.getStatus()));
	                boolean allLeave = dailyRecords.stream().allMatch(r ->
	                        "LEAVE".equalsIgnoreCase(r.getStatus()) || "MEDICAL_LEAVE".equalsIgnoreCase(r.getStatus()));

	                if (hasPresent) {
	                    daysPresent++;
	                } else if (allLeave) {
	                    daysLeave++;
	                } else {
	                    daysAbsent++;
	                }
	            }

	            double attendancePercentage = totalWorkingDays == 0 ? 0.0 : (daysPresent * 100.0) / totalWorkingDays;

	            Map<String, Object> summary = new HashMap<>();
	            summary.put("totalWorkingDays", totalWorkingDays);
	            summary.put("daysPresent", daysPresent);
	            summary.put("daysAbsent", daysAbsent);
	            summary.put("daysLeave", daysLeave);
	            summary.put("attendancePercentage", attendancePercentage);

	            response.put("summary", summary);
	        }

	        // Final response
	        response.put("status", "Success");
	        response.put("message", "Attendance records retrieved successfully");
	        response.put("attendance", groupedAttendance);

	    } catch (Exception e) {
	        response.put("status", "Error");
	        response.put("message", "An error occurred while retrieving attendance: " + e.getMessage());
	    }

	    return response;
	}


}
