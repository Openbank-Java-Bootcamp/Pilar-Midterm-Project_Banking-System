package com.ironhack.midtermproject.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String streetAddress;
    private String postalCode;
    private String city;

    public Address() {
    }

    public Address(String address, String postalCode, String city) {
        this.streetAddress = address;
        this.postalCode = postalCode;
        this.city = city;
    }

    public String getAddress() {
        return streetAddress;
    }

    public void setAddress(String address) {
        this.streetAddress = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
