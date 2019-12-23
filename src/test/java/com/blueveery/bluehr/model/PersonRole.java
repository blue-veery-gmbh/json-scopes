package com.blueveery.bluehr.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.*;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@Table(name = "person_roles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type")
public abstract class PersonRole extends BaseEntity {

    @Version
    private int version;

    public PersonRole() {
    }

    public int getVersion() {
        return version;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
