package com.ironhack.midtermproject.DTO;

import com.ironhack.midtermproject.model.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CheckingAccountDTO {
    @NotNull
    private Money balance;
    @NotNull
    private Long primaryAccountOwnerId;
    private Long secondaryAccountOwnerId;
    @NotNull
    private String secretKey;

    public CheckingAccountDTO(Money balance, Long primaryAccountOwnerId, Long secondaryAccountOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryAccountOwnerId = primaryAccountOwnerId;
        this.secondaryAccountOwnerId = secondaryAccountOwnerId;
        this.secretKey = secretKey;
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
}
