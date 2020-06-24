package com.blueveery.blueshop.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "location")
public class Location extends BaseEntity {

    private int flatNumber;
    private String streetName;
    private String postalCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Person person;

    public Location() {
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
