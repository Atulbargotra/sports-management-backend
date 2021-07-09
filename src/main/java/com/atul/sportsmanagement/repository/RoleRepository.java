package com.atul.sportsmanagement.repository;

import com.atul.sportsmanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String rolename);
}
