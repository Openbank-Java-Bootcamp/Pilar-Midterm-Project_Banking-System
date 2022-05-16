package com.ironhack.midtermproject.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value="Admin")
public class Admin extends User{

    public Admin() {
    }

    public Admin(String name, String username, String password, Role role) {
        super(name, username, password, role);
    }


}
