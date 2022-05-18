package com.ironhack.midtermproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

@Entity
@DiscriminatorValue(value="CreditCard")
@Data
public class CreditCard extends Account{


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="credit_limit_amount")),
            @AttributeOverride(name="currency", column=@Column(name="credit_limit_currency"))
    })
    private Money creditLimit;

    @Digits(integer=5, fraction=4)  //entero es 1 o 0???
    private BigDecimal interestRate;

    private LocalDate addedInterest = null;

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }


    public CreditCard() {
    }


    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        if(creditLimit.getAmount().compareTo(new BigDecimal(100)) == -1 || creditLimit.getAmount().compareTo(new BigDecimal(100000)) == 1 || creditLimit.getAmount() == null){
            this.creditLimit = new Money(new BigDecimal(100),Currency.getInstance("EUR"));
        } else {
            this.creditLimit = creditLimit;
        }
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if(interestRate.compareTo(BigDecimal.valueOf(0.2)) == 1 || interestRate.compareTo(BigDecimal.valueOf(0.1)) == -1 || interestRate == null) {
            this.interestRate = BigDecimal.valueOf(0.2);
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
