package com.blueveery.blueshop.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@DiscriminatorValue(value = "customer")
public class Customer extends PersonRole{

    private String email;
    private String mobilePhone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orderList = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<Order> getOrderList() {
        return orderList;
    }
}
