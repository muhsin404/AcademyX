package com.academyx.attendance.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.attendance.model.AttendanceSession;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long>{

	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
		       "FROM AttendanceSession s " +
		       "WHERE s.sessionDate = :date AND s.timetableEntry.timetableEntryId = :timetableId AND s.batch.batchId = :batchId")
		boolean existsSessionByDateAndTimetableAndBatch(@Param("date") LocalDate date,
		                                                @Param("timetableId") Long timetableId,
		                                                @Param("batchId") Long batchId);

}
