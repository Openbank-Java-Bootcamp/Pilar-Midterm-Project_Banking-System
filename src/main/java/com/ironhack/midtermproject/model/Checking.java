package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

//When creating a new Checking account, if the primaryOwner is less than 24, a StudentChecking account should be created otherwise a regular Checking Account should be created.

@Entity
@DiscriminatorValue(value="Checking")
@Data
public class Checking extends Account {
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="checking_minimum_balance_amount")),
            @AttributeOverride(name="currency", column=@Column(name="checking_minimum_balance_currency"))
    })
    private Money minimumBalance = new Money(new BigDecimal("250"), Currency.getInstance("EUR"));

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="checking_maintenance_fee_amount")),
            @AttributeOverride(name="currency", column=@Column(name="checking_maintenance_fee_currency"))
    })
    private Money monthlyMaintenanceFee = new Money(new BigDecimal("12"),Currency.getInstance("EUR"));

    private LocalDate maintenanceCharged = null;
    public Checking() {
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
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

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public LocalDate getMaintenanceCharged() {
        return maintenanceCharged;
    }

    public void setMaintenanceCharged(LocalDate maintenanceCharged) {
        this.maintenanceCharged = maintenanceCharged;
    }
}
