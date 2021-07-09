package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TeamsRepository extends JpaRepository<Teams,Long> {
    Optional<Set<Teams>> findTeamsByEventId(Long eventId);
    Optional<Teams> findTeamsByAdminUserIdAndEventId(Long userId,Long eventId);
    Optional<Set<Teams>> findTeamsByAdminUserId(Long id);
}
