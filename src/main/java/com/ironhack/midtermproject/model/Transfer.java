package com.ironhack.midtermproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromAccountId;
    private BigDecimal amount;
    private LocalDateTime date;

    public Transfer() {
    }

    public Transfer(Long fromAccountId, BigDecimal amount, LocalDateTime date) {
        this.fromAccountId = fromAccountId;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromAccount() {
        return fromAccountId;
    }

    public void setFromAccount(Long fromAccount) {
        this.fromAccountId = fromAccount;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}


