package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
