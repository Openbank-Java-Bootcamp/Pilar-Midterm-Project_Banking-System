package com.ironhack.midtermproject.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value="ThirdParty")
public class ThirdParty extends User{
    private String hashedKey;

    public ThirdParty() {
    }

    public ThirdParty(String name, String username, String password, Role role, String hashedKey) {
        super(name, username, password, role);
        this.hashedKey = hashedKey;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
