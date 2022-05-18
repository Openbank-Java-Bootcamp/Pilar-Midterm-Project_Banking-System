package com.ironhack.midtermproject.model;

import com.ironhack.midtermproject.enums.Status;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;


@Entity
@DiscriminatorValue(value="StudentChecking")
@Data
public class StudentChecking extends Account{
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public StudentChecking() {
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
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
