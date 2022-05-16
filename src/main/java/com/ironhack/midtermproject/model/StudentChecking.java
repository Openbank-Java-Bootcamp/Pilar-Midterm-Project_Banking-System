package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.enums.Status;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;

import java.util.Date;


@Entity
@DiscriminatorValue(value="StudentChecking")
public class StudentChecking extends Account{
    private String secretKey;

    @Enumerated
    private Status status;

    public StudentChecking() {
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Date creationDate, String secretKey, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, Date creationDate, String secretKey, Status status) {
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
}
