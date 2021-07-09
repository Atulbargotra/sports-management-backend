package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Winners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnersRepository extends JpaRepository<Winners,Long> {
}
