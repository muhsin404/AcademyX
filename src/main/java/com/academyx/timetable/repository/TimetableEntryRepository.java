package com.academyx.timetable.repository;

import com.academyx.timetable.dto.TimetableEntryDTO;
import com.academyx.timetable.model.TimetableEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {

    // Fetch existing timetable entries for display
    @Query("""
      SELECT new com.academyx.timetable.dto.TimetableEntryDTO(t)
      FROM TimetableEntry t
      WHERE t.batch.batchId = :batchId
        AND t.status = 1
      ORDER BY
        CASE t.dayOfWeek
          WHEN 'MONDAY'    THEN 1
          WHEN 'TUESDAY'   THEN 2
          WHEN 'WEDNESDAY' THEN 3
          WHEN 'THURSDAY'  THEN 4
          WHEN 'FRIDAY'    THEN 5
          ELSE 6 END,
        t.startTime
    """)
    List<TimetableEntryDTO> findByBatchIdOrderByDayTime(@Param("batchId") Long batchId);

    // Count any overlapping periods for a staff on given day/time
    @Query("""
      SELECT COUNT(t) 
        FROM TimetableEntry t
       WHERE t.staff.userId = :staffId
         AND t.dayOfWeek = :dayOfWeek
         AND t.status = 1
         AND t.startTime < :endTime
         AND t.endTime > :startTime
    """)
    long countOverlappingStaffPeriods(
        @Param("staffId") Long staffId,
        @Param("dayOfWeek") String dayOfWeek,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );

    // Find if this exact period already exists for the batch
    @Query("""
      SELECT t 
        FROM TimetableEntry t
       WHERE t.batch.batchId = :batchId
         AND t.dayOfWeek = :dayOfWeek
         AND t.startTime = :startTime
         AND t.endTime = :endTime
         AND t.status = 1
    """)
    Optional<TimetableEntry> findByBatchIdAndTimeSlot(
        @Param("batchId") Long batchId,
        @Param("dayOfWeek") String dayOfWeek,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}
