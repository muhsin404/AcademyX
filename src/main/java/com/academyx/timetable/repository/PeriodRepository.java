package com.academyx.timetable.repository;

import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.timetable.model.Periods;

public interface PeriodRepository extends JpaRepository<Periods, Long>{

	@Query("SELECT p FROM Periods p WHERE p.periodId = :periodId")
	Periods findActivePeriod(@Param("periodId") Long periodId);

	@Query("SELECT p FROM Periods p WHERE p.periodNumber = :periodNumber")
    Optional<Periods> findByPeriodNumber(@Param("periodNumber") int periodNumber);
	
	@Query("SELECT COUNT(p) > 0 FROM Periods p WHERE p.startTime = :startTime AND p.endTime = :endTime AND p.periodNumber = :periodNumber")
    boolean existsPeriod(@Param("startTime") LocalTime startTime,
                         @Param("endTime") LocalTime endTime,
                         @Param("periodNumber") int periodNumber);

}
