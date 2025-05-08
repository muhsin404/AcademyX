package com.academyx.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academyx.attendance.model.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long>{

}
