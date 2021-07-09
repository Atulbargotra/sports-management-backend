package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    Optional<Schedule> findByEventId(Long eventId);
}
