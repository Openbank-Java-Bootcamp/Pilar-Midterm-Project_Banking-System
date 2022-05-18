package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.AccountHolder;
import com.ironhack.midtermproject.model.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingAccountDTO {
    @NotNull
    private Money balance;
    @NotNull
    private Long primaryAccountOwnerId;
    private Long secondaryAccountOwnerId;
    @NotNull
    private String secretKey;
    private Money minimumBalance;
    @Digits(integer=1, fraction=1)
    private BigDecimal interestRate;

    public SavingAccountDTO(Money balance, Long primaryAccountOwnerId, Long secondaryAccountOwnerId, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryAccountOwnerId = primaryAccountOwnerId;
        this.secondaryAccountOwnerId = secondaryAccountOwnerId;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Long getPrimaryAccountOwnerId() {
        return primaryAccountOwnerId;
    }

    public void setPrimaryAccountOwnerId(Long primaryAccountOwnerId) {
        this.primaryAccountOwnerId = primaryAccountOwnerId;
    }

    public Long getSecondaryAccountOwnerId() {
        return secondaryAccountOwnerId;
    }

    public void setSecondaryAccountOwnerId(Long secondaryAccountOwnerId) {
        this.secondaryAccountOwnerId = secondaryAccountOwnerId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
