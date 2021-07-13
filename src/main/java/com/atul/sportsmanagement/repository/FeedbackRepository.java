package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Feedback;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FeedbackRepository extends CrudRepository<Feedback,Long> {
    Set<Feedback> findAllByEventId(Long id);
    Optional<Feedback> findByEventIdAndAndUserId(Long eventId, Long userId);
}
