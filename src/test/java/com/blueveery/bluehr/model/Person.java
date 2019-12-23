package com.blueveery.bluehr.model;


import com.blueveery.core.model.BaseEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@Table(name = "persons")
public class Person extends BaseEntity {
    private String firstName;
    @Nullable
    private String secondName;

    @Version
    private int version;

    public int getVersion() {
        return version;
    }

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PersonRole> personRoles = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public List<PersonRole> getPersonRoles() {
        return personRoles;
    }
}
