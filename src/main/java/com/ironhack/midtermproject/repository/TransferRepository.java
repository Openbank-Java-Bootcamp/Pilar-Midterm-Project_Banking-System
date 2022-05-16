package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
