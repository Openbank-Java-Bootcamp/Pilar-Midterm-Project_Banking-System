package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

@Entity
@DiscriminatorValue(value="Savings")
@Data
public class Savings extends Account{
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="saving_minimum_balance_amount")),
            @AttributeOverride(name="currency", column=@Column(name="saving_minimum_balance_currency"))
    })
    private Money minimumBalance;

    @Digits(integer=5, fraction=4) //entero es 1 o 0???
    @Column(precision=5,scale=4)
    private BigDecimal interestRate;

    private LocalDate addedInterest = null;

    public Savings() {
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Money minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
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

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        if(minimumBalance.getAmount().compareTo(new BigDecimal(100)) == -1 || minimumBalance.getAmount().compareTo(new BigDecimal(1000)) == 1 || minimumBalance.getAmount()==null){
            this.minimumBalance = new Money(new BigDecimal(1000),Currency.getInstance("EUR"));
        } else {
            this.minimumBalance = minimumBalance;
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if(interestRate.compareTo(BigDecimal.valueOf(0.5000)) == 1 || interestRate.compareTo(BigDecimal.ZERO) == 0 || interestRate.compareTo(BigDecimal.ZERO) == -1 || interestRate == null){
            this.interestRate = BigDecimal.valueOf(0.0025);
        } else {
            this.interestRate = interestRate;
        }
    }

    public void setAddedInterest(LocalDate addedInterest) {
        this.addedInterest = addedInterest;
    }

    public LocalDate getAddedInterest() {
        return addedInterest;
    }
}
