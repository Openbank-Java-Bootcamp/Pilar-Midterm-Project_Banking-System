package com.ironhack.midtermproject.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue(value="Admin")
@Data
public class Admin extends User{

    public Admin() {
    }

    public Admin(String name, String username, String password) {
        super(name, username, password);
    }


}
