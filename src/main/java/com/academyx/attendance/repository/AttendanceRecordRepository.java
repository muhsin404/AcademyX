package com.academyx.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.attendance.model.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long>{

	@Query("SELECT ar FROM AttendanceRecord ar " +
	           "JOIN FETCH ar.session s " +
	           "JOIN FETCH s.timetableEntry te " +
	           "WHERE ar.student.userId = :studentId AND s.sessionDate = :sessionDate")
	    List<AttendanceRecord> findByStudentAndDate(@Param("studentId") Long studentId,
	                                                @Param("sessionDate") LocalDate sessionDate);
}
