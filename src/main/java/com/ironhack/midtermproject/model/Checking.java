package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.enums.Status;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.util.Date;

//When creating a new Checking account, if the primaryOwner is less than 24, a StudentChecking account should be created otherwise a regular Checking Account should be created.

@Entity
@DiscriminatorValue(value="Checking")
public class Checking extends Account {
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    private final BigDecimal minimumBalance = new BigDecimal("250");
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal("12");

    public Checking() {
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Date creationDate, String secretKey, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
    }

    public Checking(Money balance, AccountHolder primaryOwner, Date creationDate, String secretKey, Status status) {
        super(balance, primaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

}
