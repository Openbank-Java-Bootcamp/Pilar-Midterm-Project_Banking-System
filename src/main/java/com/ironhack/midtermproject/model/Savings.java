package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue(value="Savings")
public class Savings extends Account{
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(columnDefinition = "double default 100") //so there's also a default value in the database table
    @DecimalMin(value = "100")
    @DecimalMax(value = "1000")
    @Digits(integer=4, fraction=1)
    private BigDecimal minimumBalance = new BigDecimal("1000");

    @Column(columnDefinition = "double default 0.0025") //so there's also a default value in the database table
    @DecimalMax(value = "0.5")
    @Digits(integer=1, fraction=4)
    private BigDecimal interestRate= new BigDecimal("0.0025");

    public Savings() {
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Date creationDate, String secretKey, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public Savings(Money balance, AccountHolder primaryOwner, Date creationDate, String secretKey, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
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

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
