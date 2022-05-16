package com.ironhack.midtermproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;


import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue(value="CreditCard")
public class CreditCard extends Account{

    @Column(columnDefinition = "double default 100") //so there's also a default value in the database table
    @DecimalMax(value = "100000")
    @Digits(integer=6, fraction=2)
    private BigDecimal creditLimit = new BigDecimal("100");

    @Column(columnDefinition = "double default 0.2") //so there's also a default value in the database table
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "0.2")
    @Digits(integer=1, fraction=1)
    private BigDecimal interestRate = new BigDecimal("0.2");

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }


    public CreditCard() {
    }


    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
