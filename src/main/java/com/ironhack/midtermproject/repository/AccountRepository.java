package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
