package com.ironhack.midtermproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Entity
@DiscriminatorValue(value="AccountHolder")
@Data
public class AccountHolder extends User{

    //@Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Date format must be YYYY-MM-DD")
    private LocalDate dateOfBirth;
    @Embedded
    private Address primaryAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="streetAddress", column=@Column(name="mailing_street_address")),
            @AttributeOverride(name="city", column=@Column(name="mailing_city")),
            @AttributeOverride(name="postalCode", column=@Column(name="mailing_postal_code"))
    })
    private Address mailingAddress; //este tiene que ser opcional

    public AccountHolder() {
    }

    public AccountHolder(String name, String username, String password, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name, username, password);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
