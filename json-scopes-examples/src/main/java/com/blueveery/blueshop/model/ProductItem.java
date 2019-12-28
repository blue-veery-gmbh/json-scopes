package com.blueveery.blueshop.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.*;

/**
 * Created by tomek on 08.09.16.
 */

@Entity
@Table(name = "product_items")
public class ProductItem extends BaseEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }
}
