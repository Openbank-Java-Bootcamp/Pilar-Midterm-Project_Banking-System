package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Account;
import com.ironhack.midtermproject.model.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
        Optional<Account> findByPrimaryOwnerId(Long id);
        Optional<Account> findBySecondaryOwnerId(Long id);

        @Query("SELECT secretKey FROM Account WHERE id = :id")
        String findSecretKeyByAccountId(@Param("id") Long id);
}
