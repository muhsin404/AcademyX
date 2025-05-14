package com.academyx.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.attendance.model.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long>{

	//query to get the attendance of a student on a single date
	@Query("SELECT ar FROM AttendanceRecord ar " +
	           "JOIN FETCH ar.session s " +
	           "JOIN FETCH s.timetableEntry te " +
	           "WHERE ar.student.userId = :studentId AND s.sessionDate = :sessionDate")
	    List<AttendanceRecord> findByStudentAndDate(@Param("studentId") Long studentId,
	                                                @Param("sessionDate") LocalDate sessionDate);

	
	// query to get the attendance of  a student in between date range
	@Query("SELECT ar FROM AttendanceRecord ar " +
		       "WHERE ar.student.userId = :studentId " +
		       "AND ar.session.sessionDate BETWEEN :startDate AND :endDate")
		List<AttendanceRecord> findByStudentAndDateBetween(
		        @Param("studentId") Long studentId,
		        @Param("startDate") LocalDate startDate,
		        @Param("endDate") LocalDate endDate);
	
	
	//query to get the attendance of the student in between a date range based on the subject 
	@Query("SELECT ar FROM AttendanceRecord ar " +
		       "WHERE ar.student.userId = :studentId " +
		       "AND ar.session.sessionDate BETWEEN :startDate AND :endDate " +
		       "AND ar.session.timetableEntry.subject.subjectId = :subjectId")
		List<AttendanceRecord> findByStudentAndDateRangeAndSubject(
		        @Param("studentId") Long studentId,
		        @Param("startDate") LocalDate startDate,
		        @Param("endDate") LocalDate endDate,
		        @Param("subjectId") Long subjectId);
	
	
	//query to get the attendance of the student of a date based on subject
	@Query("SELECT ar FROM AttendanceRecord ar " +
		       "WHERE ar.student.userId = :studentId " +
		       "AND ar.session.sessionDate = :sessionDate " +
		       "AND ar.session.timetableEntry.subject.subjectId = :subjectId")
		List<AttendanceRecord> findByStudentAndDateAndSubject(
		        @Param("studentId") Long studentId,
		        @Param("sessionDate") LocalDate sessionDate,
		        @Param("subjectId") Long subjectId);

}
