package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.Money;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreditAccountDTO {
    @NotNull
    private Money balance;
    @NotNull
    private Long primaryAccountOwnerId;
    private Long secondaryAccountOwnerId;
    private Money creditLimit;
    @Digits(integer=1, fraction=1)
    private BigDecimal interestRate;

    public CreditAccountDTO(Money balance, Long primaryAccountOwnerId, Long secondaryAccountOwnerId, Money creditLimit, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryAccountOwnerId = primaryAccountOwnerId;
        this.secondaryAccountOwnerId = secondaryAccountOwnerId;
        this.creditLimit = creditLimit;
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

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
