package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    //asuming id is always increasing i can search ast transfer by id group by account
    @Query(value = "SELECT last FROM (SELECT from_account_id, MAX(date) as last FROM transfer GROUP BY from_account_id) as last_per_acc WHERE from_account_id = :id", nativeQuery = true)
    LocalDateTime findLastTransferDateByAccountId(@Param("id") Long accountId);

    @Query(value = "SELECT MAX(amount_sum) FROM (SELECT t.from_account_id, t.date, SUM(c.amount)AS amount_sum FROM transfer t JOIN (SELECT from_account_id, date, amount FROM transfer) c ON c.from_account_id = t.from_account_id AND c.date BETWEEN t.date - INTERVAL 1 DAY AND t.date GROUP BY t.from_account_id, t.date HAVING t.from_account_id = :id ORDER BY t.date DESC) p", nativeQuery = true)
    BigDecimal findMaxDailyTransferAmount(@Param("id") Long accountId);

    @Query(value = "SELECT SUM(amount) FROM transfer WHERE date BETWEEN :date - INTERVAL 1 DAY AND :date AND from_account_id = :id", nativeQuery = true)
    BigDecimal findAmountTransferedLastDayFromNow(@Param("date") LocalDateTime date, @Param("id") Long accountId);




}
