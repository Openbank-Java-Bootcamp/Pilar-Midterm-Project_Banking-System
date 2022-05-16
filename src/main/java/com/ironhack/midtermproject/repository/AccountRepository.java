package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
        Optional<Account> findByPrimaryOwnerId(Long id);
        Optional<Account> findBySecondaryOwnerId(Long id);
}
