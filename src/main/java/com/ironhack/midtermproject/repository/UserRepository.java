package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
