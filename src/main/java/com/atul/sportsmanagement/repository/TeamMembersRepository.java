package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMembersRepository extends JpaRepository<TeamMembers,Long> {
}
