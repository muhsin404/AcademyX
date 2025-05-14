package com.academyx.timetable.repository;

import com.academyx.timetable.model.TimetableEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {

    @Query("""
        SELECT COUNT(t) FROM TimetableEntry t
         WHERE t.staff.userId = :staffId
           AND t.dayOfWeek = :dayOfWeek
           AND t.period.periodId = :periodId
           AND t.status = 1
    """)
    long countConflictsForStaff(
        @Param("staffId") Long staffId,
        @Param("dayOfWeek") String dayOfWeek,
        @Param("periodId") Long periodId
    );

    @Query("""
        SELECT t FROM TimetableEntry t
         WHERE t.batch.batchId = :batchId
           AND t.dayOfWeek = :dayOfWeek
           AND t.period.periodId = :periodId
           AND t.status = 1
    """)
    Optional<TimetableEntry> findExistingEntry(
        @Param("batchId") Long batchId,
        @Param("dayOfWeek") String dayOfWeek,
        @Param("periodId") Long periodId
    );

	// Get timetable by batch for a specific day
    
    @Query("SELECT t FROM TimetableEntry t WHERE t.batch.batchId = :batchId AND t.dayOfWeek = :dayOfWeek")
    List<TimetableEntry> findByBatchAndDayOfWeek(@Param("batchId") Long batchId, @Param("dayOfWeek") String dayOfWeek);
    
    // Get timetable by staff for a specific day
    
    @Query("SELECT t FROM TimetableEntry t WHERE t.staff.userId = :staffId AND t.dayOfWeek = :dayOfWeek")
    List<TimetableEntry> findByStaffAndDayOfWeek(@Param("staffId") Long batchId, @Param("dayOfWeek") String dayOfWeek);
}
