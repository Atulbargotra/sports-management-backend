package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Event;
import com.atul.sportsmanagement.model.EventType;
import com.atul.sportsmanagement.model.User;
import com.atul.sportsmanagement.model.Winners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
//    List<Event> findEventByEventId(Long id);
//    List<Event> findEventByCreatedDateGreaterThanEqualAndLastDateLessThanEqual(Instant startDate, Instant endDate);
//    List<Event> findEventByType(EventType type);
//    List<Event> findEventByCategory(String category);
//    List<Event> findEventByVenue(String venue);
    List<Event> findAllByOrderByCreatedDateDesc();
//    @Query("select e from Event e where e.eventDate > :now")
//    List<Event> ongoingEvents(@Param("now") Instant now);



}
