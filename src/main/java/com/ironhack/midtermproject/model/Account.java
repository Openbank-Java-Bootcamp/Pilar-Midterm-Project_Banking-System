package com.ironhack.midtermproject.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public abstract class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="balance_amount")),
            @AttributeOverride(name="currency", column=@Column(name="balance_currency"))
    })
    @NotNull
    private Money balance;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="primary_owner_id")
    @NotNull
    private AccountHolder primaryOwner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="secondary_owner_id")
    private AccountHolder secondaryOwner; //este tiene que ser opcional

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="penalty_fee_amount")),
            @AttributeOverride(name="currency", column=@Column(name="penalty_fee_currency"))
    })
    private Money penaltyFee;

    private LocalDate creationDate;


    public Account() {
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creationDate = LocalDate.now();
        this.penaltyFee = new Money(new BigDecimal("40"), Currency.getInstance("EUR"));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public Money getPenaltyFee() {
        return penaltyFee;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

}
